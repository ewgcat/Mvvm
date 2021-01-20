@file:JvmName("Contexts")
@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.lishuaihua.imageloader

import android.content.Context

/**
 * Get the singleton [ImageLoader]. This is an alias for [ImageLoaderUtil.imageLoader].
 */
inline val Context.imageLoader: ImageLoader
    @JvmName("imageLoader") get() = ImageLoaderUtil.imageLoader(this)
