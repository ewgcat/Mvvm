package com.lishuaihua.imageloader.fetch

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.lishuaihua.imageloader.bitmap.BitmapPool
import com.lishuaihua.imageloader.decode.DataSource
import com.lishuaihua.imageloader.decode.Options
import com.lishuaihua.imageloader.size.Size
import com.lishuaihua.imageloader.util.firstPathSegment
import com.lishuaihua.imageloader.util.getMimeTypeFromUrl
import okio.buffer
import okio.source

internal class AssetUriFetcher(private val context: Context) : Fetcher<Uri> {

    override fun handles(data: Uri): Boolean {
        return data.scheme == ContentResolver.SCHEME_FILE && data.firstPathSegment == ASSET_FILE_PATH_ROOT
    }

    override fun key(data: Uri) = data.toString()

    override suspend fun fetch(
        pool: BitmapPool,
        data: Uri,
        size: Size,
        options: Options
    ): FetchResult {
        val path = data.pathSegments.drop(1).joinToString("/")

        return SourceResult(
            source = context.assets.open(path).source().buffer(),
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromUrl(path),
            dataSource = DataSource.DISK
        )
    }

    companion object {
        const val ASSET_FILE_PATH_ROOT = "android_asset"
    }
}
