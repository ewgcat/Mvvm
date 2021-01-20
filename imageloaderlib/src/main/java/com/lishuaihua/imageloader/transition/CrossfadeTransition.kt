@file:Suppress("NEWER_VERSION_IN_SINCE_KOTLIN", "unused")

package com.lishuaihua.imageloader.transition

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.lishuaihua.imageloader.annotation.ExperimentalCoilApi
import com.lishuaihua.imageloader.decode.DataSource
import com.lishuaihua.imageloader.drawable.CrossfadeDrawable
import com.lishuaihua.imageloader.request.ErrorResult
import com.lishuaihua.imageloader.request.ImageResult
import com.lishuaihua.imageloader.request.SuccessResult
import com.lishuaihua.imageloader.size.Scale
import com.lishuaihua.imageloader.util.scale
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * A [Transition] that crossfades from the current drawable to a new one.
 *
 * @param durationMillis The duration of the animation in milliseconds.
 * @param preferExactIntrinsicSize See [CrossfadeDrawable.preferExactIntrinsicSize].
 */
@ExperimentalCoilApi
class CrossfadeTransition(
    val durationMillis: Int = CrossfadeDrawable.DEFAULT_DURATION,
    val preferExactIntrinsicSize: Boolean = false
) : Transition {

    init {
        require(durationMillis > 0) { "durationMillis must be > 0." }
    }

    override suspend fun transition(target: TransitionTarget, result: ImageResult) {
        // Don't animate if the request was fulfilled by the memory cache.
        if (result is SuccessResult && result.metadata.dataSource == DataSource.MEMORY_CACHE) {
            target.onSuccess(result.drawable)
            return
        }

        // Don't animate if the view is not visible as CrossfadeDrawable.onDraw
        // won't be called until the view becomes visible.
        if (!target.view.isVisible) {
            when (result) {
                is SuccessResult -> target.onSuccess(result.drawable)
                is ErrorResult -> target.onError(result.drawable)
            }
            return
        }

        // Animate the drawable and suspend until the animation completes.
        var outerCrossfade: CrossfadeDrawable? = null
        try {
            suspendCancellableCoroutine<Unit> { continuation ->
                val crossfade = CrossfadeDrawable(
                    start = target.drawable,
                    end = result.drawable,
                    scale = (target.view as? ImageView)?.scale ?: Scale.FILL,
                    durationMillis = durationMillis,
                    fadeStart = result !is SuccessResult || !result.metadata.isPlaceholderMemoryCacheKeyPresent,
                    preferExactIntrinsicSize = preferExactIntrinsicSize
                )
                outerCrossfade = crossfade
                crossfade.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        crossfade.unregisterAnimationCallback(this)
                        continuation.resume(Unit)
                    }
                })
                when (result) {
                    is SuccessResult -> target.onSuccess(crossfade)
                    is ErrorResult -> target.onError(crossfade)
                }
            }
        } catch (throwable: Throwable) {
            // Ensure exceptions are handled on the main thread.
            outerCrossfade?.stop()
            throw throwable
        }
    }

    override fun equals(other: Any?): Boolean {
        return (this === other) || (other is CrossfadeTransition && durationMillis == other.durationMillis)
    }

    override fun hashCode() = durationMillis.hashCode()

    override fun toString() = "CrossfadeTransition(durationMillis=$durationMillis)"

    @SinceKotlin("999.9") // Kept for binary compatibility.
    constructor() : this()

    @SinceKotlin("999.9") // Kept for binary compatibility.
    constructor(durationMillis: Int = CrossfadeDrawable.DEFAULT_DURATION) : this(durationMillis)
}
