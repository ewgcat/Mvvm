package com.lishuaihua.imageloader.fetch

import android.webkit.MimeTypeMap
import com.lishuaihua.imageloader.bitmap.BitmapPool
import com.lishuaihua.imageloader.decode.DataSource
import com.lishuaihua.imageloader.decode.Options
import com.lishuaihua.imageloader.size.Size
import okio.buffer
import okio.source
import java.io.File

internal class FileFetcher(private val addLastModifiedToFileCacheKey: Boolean) : Fetcher<File> {

    override fun key(data: File): String {
        return if (addLastModifiedToFileCacheKey) "${data.path}:${data.lastModified()}" else data.path
    }

    override suspend fun fetch(
        pool: BitmapPool,
        data: File,
        size: Size,
        options: Options
    ): FetchResult {
        return SourceResult(
            source = data.source().buffer(),
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(data.extension),
            dataSource = DataSource.DISK
        )
    }
}
