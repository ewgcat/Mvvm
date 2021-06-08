package com.lishuaihua.baselib.util

import android.content.res.Resources
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class GlideRoundTransform @JvmOverloads constructor(dp: Int = 8) : BitmapTransformation() {
    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        return roundCrop(pool, toTransform)!!
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}

    companion object {
        private  var radius = 0f
        private fun roundCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
            if (source == null) return null
            var result: Bitmap? = pool[source.width, source.height, Bitmap.Config.ARGB_8888]
            if (result == null) {
                result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
            }
            val canvas = Canvas(result!!)
            val paint = Paint()
            paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.isAntiAlias = true
            val rectF = RectF(0f, 0f, source.width.toFloat(), source.height.toFloat())
            canvas.drawRoundRect(rectF, radius, radius, paint)
            return result
        }
    }

    init {
        radius = Resources.getSystem().displayMetrics.density * dp
    }
}