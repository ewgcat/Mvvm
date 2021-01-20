@file:JvmName("ImageLoaders")
@file:Suppress("unused")

package com.lishuaihua.imageloader

import androidx.annotation.WorkerThread
import com.lishuaihua.imageloader.request.ImageRequest
import com.lishuaihua.imageloader.request.ImageResult
import kotlinx.coroutines.runBlocking

/**
 * Execute the [request] and block the current thread until it completes.
 *
 * @see ImageLoader.execute
 */
@WorkerThread
fun ImageLoader.executeBlocking(request: ImageRequest): ImageResult {
    return runBlocking { execute(request) }
}
