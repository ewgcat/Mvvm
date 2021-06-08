package com.lishuaihua.baselib.util

import android.content.Context
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.RequestOptions
import com.lishuaihua.baselib.R

object ImageLoader {
    /**
     * @param context
     * @param resId
     * @param placeholderResId
     * @param errorResId
     * @param transformation
     * @param isCached         是否使用缓存
     * @param imageView
     */
    @JvmStatic
    fun setImageResourceCenterCrop(context: Context?, resId: Int, placeholderResId: Int, errorResId: Int, transformation: BitmapTransformation?, isCached: Boolean, imageView: ImageView?) {
        var options = RequestOptions()
        options = options.centerCrop()
        if (placeholderResId != -1) {
            options = options.placeholder(placeholderResId)
        }
        if (errorResId != -1) {
            options = options.error(errorResId)
        }
        if (transformation != null) {
            options = options.transform(transformation)
        }
        options = if (isCached) {
            options.priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        } else {
            options.priority(Priority.HIGH).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        Glide.with(context!!).load(resId).apply(options).into(imageView!!)
    }

    /**
     * @param context
     * @param imagePath
     * @param placeholderResId
     * @param errorResId
     * @param transformation
     * @param isCached         是否使用缓存
     * @param imageView
     */
    @JvmStatic
    fun setImageResourceCenterCrop(context: Context?, imagePath: String?, placeholderResId: Int, errorResId: Int, transformation: BitmapTransformation?, isCached: Boolean, imageView: ImageView?) {
        var options = RequestOptions()
        options = options.centerCrop()
        if (placeholderResId != -1) {
            options = options.placeholder(placeholderResId)
        }
        if (errorResId != -1) {
            options = options.error(errorResId)
        }
        if (transformation != null) {
            options = options.transform(transformation)
        }
        options = if (isCached) {
            options.priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        } else {
            options.priority(Priority.HIGH).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        Glide.with(context!!).load(imagePath).apply(options).into(imageView!!)
    }

    /**
     * @param context
     * @param resId
     * @param placeholderResId
     * @param errorResId
     * @param transformation
     * @param isCached         是否使用缓存
     * @param imageView
     */
    @JvmStatic
    fun setImageResource(context: Context?, resId: Int, placeholderResId: Int, errorResId: Int, transformation: BitmapTransformation?, isCached: Boolean, imageView: ImageView?) {
        var isCached = isCached
        isCached = true
        var options = RequestOptions()
        if (placeholderResId != -1) {
            options = options.placeholder(placeholderResId)
        }
        if (errorResId != -1) {
            options = options.error(errorResId)
        }
        if (transformation != null) {
            options = options.transform(transformation)
        }
        options = if (isCached) {
            options.priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        } else {
            options.priority(Priority.HIGH).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        Glide.with(context!!).load(resId).apply(options).into(imageView!!)
    }

    /**
     * @param context
     * @param imgPath
     * @param placeholderResId
     * @param errorResId
     * @param transformation
     * @param isCached         是否使用缓存
     * @param imageView
     */
    @JvmStatic
    fun setImageResource(context: Context?, imgPath: String?, placeholderResId: Int, errorResId: Int, transformation: BitmapTransformation?, isCached: Boolean, imageView: ImageView?) {
        var isCached = isCached
        isCached = true
        var options = RequestOptions()
        if (placeholderResId != -1) {
            options = options.placeholder(placeholderResId)
        }
        if (errorResId != -1) {
            options = options.error(errorResId)
        }
        if (transformation != null) {
            options = options.transform(transformation)
        }
        options = if (isCached) {
            options.priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        } else {
            options.priority(Priority.HIGH).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        if (!TextUtils.isEmpty(imgPath)) {
            Glide.with(context!!).load(imgPath).apply(options).into(imageView!!)
        } else {
            Glide.with(context!!).load(R.mipmap.ic_default_logo).apply(options).into(imageView!!)
        }
    }

    /**
     * @param context
     * @param imgPath
     * @param placeholderResId
     * @param width
     * @param height
     * @param imageView
     */
    @JvmStatic
    fun setImageResource(context: Context?, imgPath: String?, placeholderResId: Int, width: Int, height: Int, imageView: ImageView?) {
        var options = RequestOptions()
        if (placeholderResId != -1) {
            options = options.placeholder(placeholderResId)
            options = options.error(placeholderResId)
        }
        options = options.override(width, height)
        options = options.priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        if (!TextUtils.isEmpty(imgPath)) {
            Glide.with(context!!).load(imgPath).apply(options).into(imageView!!)
        } else {
            Glide.with(context!!).load(R.mipmap.ic_default_logo).apply(options).into(imageView!!)
        }
    }

    /**
     * @param context
     * @param imgPath
     * @param width
     * @param height
     * @param imageView
     */
    @JvmStatic
    fun setImageResource(context: Context?, imgPath: String?, width: Int, height: Int, imageView: ImageView?) {
        var options = RequestOptions()
        options = options.override(width, height)
        options = options.priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        if (!TextUtils.isEmpty(imgPath)) {
            Glide.with(context!!).load(imgPath).apply(options).into(imageView!!)
        } else {
            Glide.with(context!!).load(R.mipmap.ic_default_logo).apply(options).into(imageView!!)
        }
    }

    fun setImageResource(context: Context?, imgPath: String?, width: Int, height: Int, transformation: BitmapTransformation?, imageView: ImageView?) {
        var options = RequestOptions()
        options = options.override(width, height)
        options = options.priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        if (transformation != null) {
            options = options.transform(transformation)
        }
        if (!TextUtils.isEmpty(imgPath)) {
            Glide.with(context!!).load(imgPath).apply(options).into(imageView!!)
        } else {
            Glide.with(context!!).load(R.mipmap.ic_default_logo).apply(options).into(imageView!!)
        }
    }

    /**
     * @param context
     * @param imgPath
     * @param placeholderResId
     * @param width
     * @param height
     * @param imageView
     */
    @JvmStatic
    fun setImageResource(context: Context?, imgPath: Int, placeholderResId: Int, width: Int, height: Int, transformation: BitmapTransformation?, imageView: ImageView?) {
        var options = RequestOptions()
        if (placeholderResId != -1) {
            options = options.placeholder(placeholderResId)
            options = options.error(placeholderResId)
        }
        options = options.override(width, height)
        options = options.priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(context!!).load(imgPath).apply(options).into(imageView!!)
    }

    fun setImageResource(context: Context?, imgPath: Int, width: Int, height: Int, imageView: ImageView?) {
        var options = RequestOptions()
        options = options.override(width, height)
        options = options.priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(context!!).load(imgPath).apply(options).into(imageView!!)
    }

    /**
     * @param context
     * @param imgPath
     * @param placeholderResId
     * @param width
     * @param height
     * @param imageView
     */
    @JvmStatic
    fun setImageResource(context: Context?, imgPath: String?, placeholderResId: Int, width: Int, height: Int, transformation: BitmapTransformation?, imageView: ImageView?) {
        var options = RequestOptions()
        if (placeholderResId != -1) {
            options = options.placeholder(placeholderResId)
            options = options.error(placeholderResId)
        }
        if (transformation != null) {
            options = options.transform(transformation)
        }
        options = options.override(width, height)
        options = options.priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(context!!).load(imgPath).apply(options).into(imageView!!)
    }

    /**
     * @param context
     * @param resId
     * @param placeholderResId
     * @param errorResId
     * @param isCached         是否使用缓存
     * @param imageView
     */
    @JvmStatic
    fun setImageResource(context: Context?, resId: Int, placeholderResId: Int, errorResId: Int, isCached: Boolean, imageView: ImageView?) {
        setImageResource(context, resId, placeholderResId, errorResId, null, isCached, imageView)
    }

    /**
     * @param context
     * @param resId
     * @param imageView
     */
    @JvmStatic
    fun setImageResource(context: Context?, resId: Int, imageView: ImageView?) {
        setImageResource(context, resId, -1, -1, null, false, imageView)
    }

    /**
     * @param context
     * @param imgPath
     * @param imageView
     */
    @JvmStatic
    fun setImageResource(context: Context?, imgPath: String?, imageView: ImageView?) {
        setImageResource(context, imgPath, -1, -1, null, false, imageView)
    }

    /**
     * @param context
     * @param imgPath
     * @param placeholderResId
     * @param errorResId
     * @param isCached         是否使用缓存
     * @param imageView
     */
    @JvmStatic
    fun setImageResource(context: Context?, imgPath: String?, placeholderResId: Int, errorResId: Int, isCached: Boolean, imageView: ImageView?) {
        setImageResource(context, imgPath, placeholderResId, errorResId, null, isCached, imageView)
    }

    /**
     * @param context
     * @param imgPath
     * @param imageView
     */
    @JvmStatic
    fun setImageResource(context: Context?, imgPath: String?, transformation: BitmapTransformation?, imageView: ImageView?) {
        setImageResource(context, imgPath, -1, -1, transformation, imageView)
    }

    fun setImageResource(context: Context?, imgPath: String?, isCached: Boolean, imageView: ImageView?) {
        setImageResource(context, imgPath, -1, -1, null, isCached, imageView)
    }

    /**
     * @param context
     * @param resId
     * @param transformation
     * @param isCached       是否使用缓存
     * @param imageView
     */
    @JvmStatic
    fun setImageResource(context: Context?, resId: Int, transformation: BitmapTransformation?, isCached: Boolean, imageView: ImageView?) {
        setImageResource(context, resId, -1, -1, transformation, isCached, imageView)
    }

    /**
     * @param context
     * @param imgPath
     * @param transformation
     * @param isCached       是否使用缓存
     * @param imageView
     */
    @JvmStatic
    fun setImageResource(context: Context?, imgPath: String?, transformation: BitmapTransformation?, isCached: Boolean, imageView: ImageView?) {
        setImageResource(context, imgPath, -1, -1, transformation, isCached, imageView)
    }

    /**
     * @param context
     * @param imgPath
     * @param transformation
     * @param isCached       是否使用缓存
     * @param imageView
     */
    @JvmStatic
    fun setImageResource(context: Context?, imgPath: String?, placeholderResId: Int, transformation: BitmapTransformation?, isCached: Boolean, imageView: ImageView?) {
        setImageResource(context, imgPath, placeholderResId, -1, transformation, isCached, imageView)
    }
}