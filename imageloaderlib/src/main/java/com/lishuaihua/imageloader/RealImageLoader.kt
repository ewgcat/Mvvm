package com.lishuaihua.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.MainThread
import com.lishuaihua.imageloader.bitmap.BitmapPool
import com.lishuaihua.imageloader.bitmap.BitmapReferenceCounter
import com.lishuaihua.imageloader.decode.BitmapFactoryDecoder
import com.lishuaihua.imageloader.decode.DrawableDecoderService
import com.lishuaihua.imageloader.fetch.AssetUriFetcher
import com.lishuaihua.imageloader.fetch.BitmapFetcher
import com.lishuaihua.imageloader.fetch.ContentUriFetcher
import com.lishuaihua.imageloader.fetch.DrawableFetcher
import com.lishuaihua.imageloader.fetch.FileFetcher
import com.lishuaihua.imageloader.fetch.HttpUriFetcher
import com.lishuaihua.imageloader.fetch.HttpUrlFetcher
import com.lishuaihua.imageloader.fetch.ResourceUriFetcher
import com.lishuaihua.imageloader.intercept.EngineInterceptor
import com.lishuaihua.imageloader.intercept.RealInterceptorChain
import com.lishuaihua.imageloader.map.FileUriMapper
import com.lishuaihua.imageloader.map.ResourceIntMapper
import com.lishuaihua.imageloader.map.ResourceUriMapper
import com.lishuaihua.imageloader.map.StringMapper
import com.lishuaihua.imageloader.memory.DelegateService
import com.lishuaihua.imageloader.memory.MemoryCacheService
import com.lishuaihua.imageloader.memory.RealMemoryCache
import com.lishuaihua.imageloader.memory.RequestService
import com.lishuaihua.imageloader.memory.StrongMemoryCache
import com.lishuaihua.imageloader.memory.TargetDelegate
import com.lishuaihua.imageloader.memory.WeakMemoryCache
import com.lishuaihua.imageloader.request.BaseTargetDisposable
import com.lishuaihua.imageloader.request.DefaultRequestOptions
import com.lishuaihua.imageloader.request.Disposable
import com.lishuaihua.imageloader.request.ErrorResult
import com.lishuaihua.imageloader.request.ImageRequest
import com.lishuaihua.imageloader.request.ImageResult
import com.lishuaihua.imageloader.request.NullRequestData
import com.lishuaihua.imageloader.request.NullRequestDataException
import com.lishuaihua.imageloader.request.SuccessResult
import com.lishuaihua.imageloader.request.ViewTargetDisposable
import com.lishuaihua.imageloader.size.Size
import com.lishuaihua.imageloader.target.ViewTarget
import com.lishuaihua.imageloader.util.Emoji
import com.lishuaihua.imageloader.util.Logger
import com.lishuaihua.imageloader.util.SystemCallbacks
import com.lishuaihua.imageloader.util.Utils.REQUEST_TYPE_ENQUEUE
import com.lishuaihua.imageloader.util.Utils.REQUEST_TYPE_EXECUTE
import com.lishuaihua.imageloader.util.awaitStarted
import com.lishuaihua.imageloader.util.decrement
import com.lishuaihua.imageloader.util.emoji
import com.lishuaihua.imageloader.util.job
import com.lishuaihua.imageloader.util.log
import com.lishuaihua.imageloader.util.metadata
import com.lishuaihua.imageloader.util.requestManager
import com.lishuaihua.imageloader.util.toDrawable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.coroutineContext

internal class RealImageLoader(
    context: Context,
    override val defaults: DefaultRequestOptions,
    override val bitmapPool: BitmapPool,
    private val referenceCounter: BitmapReferenceCounter,
    private val strongMemoryCache: StrongMemoryCache,
    private val weakMemoryCache: WeakMemoryCache,
    callFactory: Call.Factory,
    private val eventListenerFactory: EventListener.Factory,
    componentRegistry: ComponentRegistry,
    addLastModifiedToFileCacheKey: Boolean,
    private val launchInterceptorChainOnMainThread: Boolean,
    val logger: Logger?
) : ImageLoader {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate +
        CoroutineExceptionHandler { _, throwable -> logger?.log(TAG, throwable) })
    private val delegateService = DelegateService(this, referenceCounter, logger)
    private val memoryCacheService = MemoryCacheService(referenceCounter, strongMemoryCache, weakMemoryCache)
    private val requestService = RequestService(logger)
    override val memoryCache = RealMemoryCache(strongMemoryCache, weakMemoryCache, referenceCounter)
    private val drawableDecoder = DrawableDecoderService(bitmapPool)
    private val systemCallbacks = SystemCallbacks(this, context)
    private val registry = componentRegistry.newBuilder()
        // Mappers
        .add(StringMapper())
        .add(FileUriMapper())
        .add(ResourceUriMapper(context))
        .add(ResourceIntMapper(context))
        // Fetchers
        .add(HttpUriFetcher(callFactory))
        .add(HttpUrlFetcher(callFactory))
        .add(FileFetcher(addLastModifiedToFileCacheKey))
        .add(AssetUriFetcher(context))
        .add(ContentUriFetcher(context))
        .add(ResourceUriFetcher(context, drawableDecoder))
        .add(DrawableFetcher(drawableDecoder))
        .add(BitmapFetcher())
        // Decoders
        .add(BitmapFactoryDecoder(context))
        .build()
    private val interceptors = registry.interceptors + EngineInterceptor(registry, bitmapPool, referenceCounter,
        strongMemoryCache, memoryCacheService, requestService, systemCallbacks, drawableDecoder, logger)
    private val isShutdown = AtomicBoolean(false)

    override fun enqueue(request: ImageRequest): Disposable {
        // Start executing the request on the main thread.
        val job = scope.launch {
            val result = executeMain(request, REQUEST_TYPE_ENQUEUE)
            if (result is ErrorResult) throw result.throwable
        }

        // Update the current request attached to the view and return a new disposable.
        return if (request.target is ViewTarget<*>) {
            val requestId = request.target.view.requestManager.setCurrentRequestJob(job)
            ViewTargetDisposable(requestId, request.target)
        } else {
            BaseTargetDisposable(job)
        }
    }

    override suspend fun execute(request: ImageRequest): ImageResult {
        // Update the current request attached to the view synchronously.
        if (request.target is ViewTarget<*>) {
            request.target.view.requestManager.setCurrentRequestJob(coroutineContext.job)
        }

        // Start executing the request on the main thread.
        return withContext(Dispatchers.Main.immediate) {
            executeMain(request, REQUEST_TYPE_EXECUTE)
        }
    }

    @MainThread
    private suspend fun executeMain(initialRequest: ImageRequest, type: Int): ImageResult {
        // Ensure this image loader isn't shutdown.
        check(!isShutdown.get()) { "The image loader is shutdown." }

        // Apply this image loader's defaults to this request.
        val request = initialRequest.newBuilder().defaults(defaults).build()

        // Create a new event listener.
        val eventListener = eventListenerFactory.create(request)

        // Wrap the target to support bitmap pooling.
        val targetDelegate = delegateService.createTargetDelegate(request.target, type, eventListener)

        // Wrap the request to manage its lifecycle.
        val requestDelegate = delegateService.createRequestDelegate(request, targetDelegate, coroutineContext.job)

        try {
            // Fail before starting if data is null.
            if (request.data == NullRequestData) throw NullRequestDataException()

            // Enqueued requests suspend until the lifecycle is started.
            if (type == REQUEST_TYPE_ENQUEUE) request.lifecycle.awaitStarted()

            // Set the placeholder on the target.
            val cached = memoryCacheService[request.placeholderMemoryCacheKey]?.bitmap
            try {
                targetDelegate.metadata = null
                targetDelegate.start(cached?.toDrawable(request.context) ?: request.placeholder, cached)
                eventListener.onStart(request)
                request.listener?.onStart(request)
            } finally {
                referenceCounter.decrement(cached)
            }

            // Resolve the size.
            eventListener.resolveSizeStart(request)
            val size = request.sizeResolver.size()
            eventListener.resolveSizeEnd(request, size)

            // Execute the interceptor chain.
            val result = executeChain(request, type, size, cached, eventListener)

            // Set the result on the target.
            when (result) {
                is SuccessResult -> onSuccess(result, targetDelegate, eventListener)
                is ErrorResult -> onError(result, targetDelegate, eventListener)
            }
            return result
        } catch (throwable: Throwable) {
            if (throwable is CancellationException) {
                onCancel(request, eventListener)
                throw throwable
            } else {
                // Create the default error result if there's an uncaught exception.
                val result = requestService.errorResult(request, throwable)
                onError(result, targetDelegate, eventListener)
                return result
            }
        } finally {
            requestDelegate.complete()
        }
    }

    /** Called by [SystemCallbacks.onTrimMemory]. */
    fun onTrimMemory(level: Int) {
        strongMemoryCache.trimMemory(level)
        weakMemoryCache.trimMemory(level)
        bitmapPool.trimMemory(level)
    }

    override fun shutdown() {
        if (isShutdown.getAndSet(true)) return

        // Order is important.
        scope.cancel()
        systemCallbacks.shutdown()
        strongMemoryCache.clearMemory()
        weakMemoryCache.clearMemory()
        bitmapPool.clear()
    }

    private suspend inline fun executeChain(
        request: ImageRequest,
        type: Int,
        size: Size,
        cached: Bitmap?,
        eventListener: EventListener
    ): ImageResult {
        val chain = RealInterceptorChain(request, type, interceptors, 0, request, size, cached, eventListener)
        return if (launchInterceptorChainOnMainThread) {
            chain.proceed(request)
        } else {
            withContext(request.dispatcher) {
                chain.proceed(request)
            }
        }
    }

    private suspend inline fun onSuccess(
        result: SuccessResult,
        targetDelegate: TargetDelegate,
        eventListener: EventListener
    ) {
        try {
            val request = result.request
            val metadata = result.metadata
            val dataSource = metadata.dataSource
            logger?.log(TAG, Log.INFO) { "${dataSource.emoji} Successful (${dataSource.name}) - ${request.data}" }
            targetDelegate.metadata = metadata
            targetDelegate.success(result)
            eventListener.onSuccess(request, metadata)
            request.listener?.onSuccess(request, metadata)
        } finally {
            referenceCounter.decrement(result.drawable)
        }
    }

    private suspend inline fun onError(
        result: ErrorResult,
        targetDelegate: TargetDelegate,
        eventListener: EventListener
    ) {
        val request = result.request
        logger?.log(TAG, Log.INFO) { "${Emoji.SIREN} Failed - ${request.data} - ${result.throwable}" }
        targetDelegate.metadata = null
        targetDelegate.error(result)
        eventListener.onError(request, result.throwable)
        request.listener?.onError(request, result.throwable)
    }

    private fun onCancel(request: ImageRequest, eventListener: EventListener) {
        logger?.log(TAG, Log.INFO) { "${Emoji.CONSTRUCTION} Cancelled - ${request.data}" }
        eventListener.onCancel(request)
        request.listener?.onCancel(request)
    }

    companion object {
        private const val TAG = "RealImageLoader"
    }
}
