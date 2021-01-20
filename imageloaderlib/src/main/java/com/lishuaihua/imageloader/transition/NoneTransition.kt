package com.lishuaihua.imageloader.transition

import com.lishuaihua.imageloader.annotation.ExperimentalCoilApi
import com.lishuaihua.imageloader.request.ErrorResult
import com.lishuaihua.imageloader.request.ImageResult
import com.lishuaihua.imageloader.request.SuccessResult

/**
 * A transition that applies the [ImageResult] on the [TransitionTarget] without animating.
 */
@ExperimentalCoilApi
internal object NoneTransition : Transition {

    override suspend fun transition(target: TransitionTarget, result: ImageResult) {
        when (result) {
            is SuccessResult -> target.onSuccess(result.drawable)
            is ErrorResult -> target.onError(result.drawable)
        }
    }

    override fun toString() = "coil.transition.NoneTransition"
}
