package com.lishuaihua.imageloader.svg

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.nio.charset.Charset
import java.util.*

/**
 * A sample implementation of [SVGExternalFileResolver] that retrieves files from
 * an application's "assets" folder.
 */
class SimpleAssetResolver(private val assetManager: AssetManager) : SVGExternalFileResolver() {
    companion object {
        private const val TAG = "SimpleAssetResolver"
        private val supportedFormats: MutableSet<String?> = HashSet(8)

        // Static initialiser
        init {
            // PNG, JPEG and SVG are required by the SVG 1.2 spec
            supportedFormats.add("image/svg+xml")
            supportedFormats.add("image/jpeg")
            supportedFormats.add("image/png")
            // Other image formats supported by Android BitmapFactory
            supportedFormats.add("image/pjpeg")
            supportedFormats.add("image/gif")
            supportedFormats.add("image/bmp")
            supportedFormats.add("image/x-windows-bmp")
            // .webp supported in 4.0+ (ICE_CREAM_SANDWICH)
            if (Build.VERSION.SDK_INT >= 14) {
                supportedFormats.add("image/webp")
            }
        }
    }

    /**
     * Attempt to find the specified font in the "assets" folder and return a Typeface object.
     * For the font name "Foo", first the file "Foo.ttf" will be tried and if that fails, "Foo.otf".
     */
    override fun resolveFont(
        fontFamily: String?,
        fontWeight: Float,
        fontStyle: String?,
        fontStretch: Float
    ): Typeface? {
        Log.i(TAG, "resolveFont('$fontFamily',$fontWeight,'$fontStyle',$fontStretch)")

        // Try font name with suffix ".ttf"
        try {
            return Typeface.createFromAsset(assetManager, "$fontFamily.ttf")
        } catch (ignored: RuntimeException) {
        }

        // That failed, so try ".otf"
        try {
            return Typeface.createFromAsset(assetManager, "$fontFamily.otf")
        } catch (e: RuntimeException) {
        }

        // That failed, so try ".ttc" (Truetype collection), if supported on this version of Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder = Typeface.Builder(assetManager, "$fontFamily.ttc")
            // Get the first font file in the collection
            builder.setTtcIndex(0)
            return builder.build()
        }
        return null
    }

    /**
     * Attempt to find the specified image file in the `assets` folder and return a decoded Bitmap.
     */
    override fun resolveImage(filename: String?): Bitmap? {
        Log.i(TAG, "resolveImage($filename)")
        return try {
            val istream = assetManager.open(filename!!)
            BitmapFactory.decodeStream(istream)
        } catch (e1: IOException) {
            null
        }
    }

    /**
     * Returns true when passed the MIME types for SVG, JPEG, PNG or any of the
     * other bitmap image formats supported by Android's BitmapFactory class.
     */
    override fun isFormatSupported(mimeType: String?): Boolean {
        return supportedFormats.contains(mimeType)
    }

    /**
     * Attempt to find the specified stylesheet file in the "assets" folder and return its string contents.
     *
     * @since 1.3
     */
    override fun resolveCSSStyleSheet(url: String?): String? {
        Log.i(TAG, "resolveCSSStyleSheet($url)")
        return getAssetAsString(url)
    }

    /*
     * Read the contents of the asset whose name is given by "url" and return it as a String.
     */
    private fun getAssetAsString(url: String?): String? {
        var `is`: InputStream? = null
        return try {
            `is` = assetManager.open(url!!)
            val r: Reader = InputStreamReader(`is`, Charset.forName("UTF-8"))
            val buffer = CharArray(4096)
            val sb = StringBuilder()
            var len = r.read(buffer)
            while (len > 0) {
                sb.append(buffer, 0, len)
                len = r.read(buffer)
            }
            sb.toString()
        } catch (e: IOException) {
            null
        } finally {
            try {
                `is`?.close()
            } catch (e: IOException) {
                // Do nothing
            }
        }
    }
}