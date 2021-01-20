package com.lishuaihua.imageloader

import android.app.Application

/**
 * A factory that creates new [ImageLoader] instances.
 *
 * To configure how the singleton [ImageLoader] is created **either**:
 * - Implement [ImageLoaderFactory] in your [Application].
 * - **Or** call [ImageLoaderUtil.setImageLoader] with your [ImageLoaderFactory].
 */
fun interface ImageLoaderFactory {

    /**
     * Return a new [ImageLoader].
     */
    fun newImageLoader(): ImageLoader
}
