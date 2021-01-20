package com.lishuaihua.imageloader.map

import android.content.ContentResolver
import android.net.Uri
import androidx.core.net.toFile
import com.lishuaihua.imageloader.fetch.AssetUriFetcher
import com.lishuaihua.imageloader.util.firstPathSegment
import java.io.File

internal class FileUriMapper : Mapper<Uri, File> {

    override fun handles(data: Uri): Boolean {
        return data.scheme == ContentResolver.SCHEME_FILE &&
            data.firstPathSegment.let { it != null && it != AssetUriFetcher.ASSET_FILE_PATH_ROOT }
    }

    override fun map(data: Uri) = data.toFile()
}
