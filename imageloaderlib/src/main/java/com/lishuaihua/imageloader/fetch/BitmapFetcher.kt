package com.lishuaihua.imageloader.fetch

import android.graphics.Bitmap
import com.lishuaihua.imageloader.bitmap.BitmapPool
import com.lishuaihua.imageloader.decode.DataSource
import com.lishuaihua.imageloader.decode.Options
import com.lishuaihua.imageloader.size.Size
import com.lishuaihua.imageloader.util.toDrawable

internal class BitmapFetcher : Fetcher<Bitmap> {

    override fun key(data: Bitmap): String? = null

    override suspend fun fetch(
        pool: BitmapPool,
        data: Bitmap,
        size: Size,
        options: Options
    ): FetchResult {
        return DrawableResult(
            drawable = data.toDrawable(options.context),
            isSampled = false,
            dataSource = DataSource.MEMORY
        )
    }
}
