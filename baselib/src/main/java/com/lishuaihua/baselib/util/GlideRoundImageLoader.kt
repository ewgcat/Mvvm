package com.lishuaihua.baselib.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.lishuaihua.banner.loader.ImageLoader
import com.lishuaihua.baselib.R

class GlideRoundImageLoader : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView) {
        /**
         * 注意：
         * 1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         * 2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         * 传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         * 切记不要胡乱强转！
         */
        var options = RequestOptions()
        options = options.transform(GlideRoundTransform(8))
        options.diskCacheStrategy(DiskCacheStrategy.NONE)
        options = options.placeholder(R.mipmap.ic_default_logo_four)
        options = options.error(R.mipmap.ic_default_logo_four)
        Glide.with(context!!.applicationContext).load(path as String).apply(options).into(imageView)
    }

}