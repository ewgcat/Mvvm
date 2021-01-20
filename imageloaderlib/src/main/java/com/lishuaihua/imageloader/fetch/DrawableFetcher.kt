package com.lishuaihua.imageloader.fetch

import android.graphics.drawable.Drawable
import com.lishuaihua.imageloader.bitmap.BitmapPool
import com.lishuaihua.imageloader.decode.DataSource
import com.lishuaihua.imageloader.decode.DrawableDecoderService
import com.lishuaihua.imageloader.decode.Options
import com.lishuaihua.imageloader.size.Size
import com.lishuaihua.imageloader.util.isVector
import com.lishuaihua.imageloader.util.toDrawable

internal class DrawableFetcher(private val drawableDecoder: DrawableDecoderService) : Fetcher<Drawable> {

    override fun key(data: Drawable): String? = null

    override suspend fun fetch(
        pool: BitmapPool,
        data: Drawable,
        size: Size,
        options: Options
    ): FetchResult {
        val isVector = data.isVector
        return DrawableResult(
            drawable = if (isVector) {
                drawableDecoder.convert(
                    drawable = data,
                    config = options.config,
                    size = size,
                    scale = options.scale,
                    allowInexactSize = options.allowInexactSize
                ).toDrawable(options.context)
            } else {
                data
            },
            isSampled = isVector,
            dataSource = DataSource.MEMORY
        )
    }
}
