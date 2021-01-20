package com.lishuaihua.imageloader.memory

import android.graphics.Bitmap
import android.os.Parcelable
import com.lishuaihua.imageloader.ImageLoader
import com.lishuaihua.imageloader.request.ImageRequest
import com.lishuaihua.imageloader.request.ImageResult
import com.lishuaihua.imageloader.request.SuccessResult
import com.lishuaihua.imageloader.size.Size
import kotlinx.parcelize.Parcelize

/**
 * An in-memory cache of recently loaded images.
 */
interface MemoryCache {

    /** The current size of the cache in bytes. */
    val size: Int

    /** The maximum size of the cache in bytes. */
    val maxSize: Int

    /** Get the [Bitmap] associated with [key]. */
    operator fun get(key: Key): Bitmap?

    /** Set the [Bitmap] associated with [key]. */
    operator fun set(key: Key, bitmap: Bitmap)

    /**
     * Remove the [Bitmap] referenced by [key].
     *
     * @return True if [key] was present in the cache. Else, return false.
     */
    fun remove(key: Key): Boolean

    /** Remove all values from the memory cache. */
    fun clear()

    /** The cache key for an image in the memory cache. */
    sealed class Key : Parcelable {

        companion object {
            /** Create a simple memory cache key. */
            @JvmStatic
            @JvmName("create")
            operator fun invoke(value: String): Key = Simple(value)
        }

        /** A simple memory cache key that wraps a [String]. Create new instances using [invoke]. */
        @Parcelize
        internal data class Simple(val value: String) : Key()

        /**
         * A complex memory cache key. Instances cannot be created directly as they often cannot be created
         * synchronously. Instead they are created by an [ImageLoader]'s image pipeline and are returned as part
         * of a successful image request's [ImageResult.Metadata].
         *
         * A request's metadata is accessible through [ImageRequest.Listener.onSuccess] and [SuccessResult].
         *
         * This class is an implementation detail and its fields may change in future releases.
         */
        @Parcelize
        internal data class Complex(
            val base: String,
            val transformations: List<String>,
            val size: Size?,
            val parameters: Map<String, String>
        ) : Key()
    }
}
