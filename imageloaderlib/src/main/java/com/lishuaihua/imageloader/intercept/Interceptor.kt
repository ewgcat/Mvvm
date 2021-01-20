package com.lishuaihua.imageloader.intercept

import com.lishuaihua.imageloader.ImageLoader
import com.lishuaihua.imageloader.annotation.ExperimentalCoilApi
import com.lishuaihua.imageloader.request.ImageRequest
import com.lishuaihua.imageloader.request.ImageResult
import com.lishuaihua.imageloader.size.Size

/**
 * Observe, transform, short circuit, or retry requests to an [ImageLoader]'s image engine.
 *
 * NOTE: The interceptor chain is launched from the main thread by default.
 * See [ImageLoader.Builder.launchInterceptorChainOnMainThread] for more information.
 */
@ExperimentalCoilApi
interface Interceptor {

    suspend fun intercept(chain: Chain): ImageResult

    interface Chain {

        val request: ImageRequest

        val size: Size

        /**
         * Set the requested [Size] to load the image at.
         *
         * @param size The requested size for the image.
         */
        fun withSize(size: Size): Chain

        /**
         * Continue executing the chain.
         *
         * @param request The request to proceed with.
         */
        suspend fun proceed(request: ImageRequest): ImageResult
    }
}
