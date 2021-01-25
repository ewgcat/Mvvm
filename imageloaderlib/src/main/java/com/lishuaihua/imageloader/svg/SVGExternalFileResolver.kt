package com.lishuaihua.imageloader.svg

import android.graphics.Bitmap
import android.graphics.Typeface

/**
 * Resolver class used by the renderer when resolving font, image, and external CSS references.
 *
 *
 * When AndroidSVG encounters a reference to an external object, such as an image, it will call the
 * associated method on this class in an attempt to load it.
 *
 *
 * The default behaviour of each method is to tell AndroidSVG that the reference could not be found.
 * Extend this class and override the methods if you want to customise how AndroidSVG treats font, image, and external CSS references.
 *
 * <h3>Example usage</h3>
 *
 * <pre class="code-block">
 * `public class MyResolver {
 * // Override the default method implementations with your own.
 * // See the code for SimpleAssetResolver class, for examples of how to do that.
 * }
 *
 * // Register your resolver with AndroidSVG
 * SVG.registerExternalFileResolver(new MyResolver());
 *
 * // Your resolver will now be used when an SVG is parsed or rendered,
 * SVG mySVG = SVG.getFromX();
` *
</pre> *
 */
open class SVGExternalFileResolver {
    /**
     * Called by renderer to resolve font references in &lt;text&gt; elements.
     *
     *
     * An implementation of this method should return a `Typeface` instance, or null
     * if you want the renderer to ignore this font request.
     *
     *
     * Note that AndroidSVG does not attempt to cache Typeface references.  If you want
     * them cached, for speed or memory reasons, you should do so yourself.
     *
     *
     * If you return are using Android O or later, and return a variable Truetype or Opentype font,
     * then AndroidSVG will automatically set the weight, stretch and oblique slant for you. Note that
     * it is quite rare for variable fonts to include the italic variant. Commonly, there will be two
     * files, one with the regular glyphs and one with the italic ones.  In those cases, use the
     * {code fontStyle} parameter to choose between those two font files, and leave AndroidSVG to do
     * the rest.
     *
     *
     * Note: Prior to version 1.5 of AndroidSVG, this method did not take a `fontStretch`
     * parameter.  Also, as of 1.5, the `fontStyle` parameter will now pass lower-case style
     * names that match the CSS `font-style` values. For example "italic". Prior to 1.5, you
     * would have received "Italic" (capital 'I') instead.
     *
     * @param fontFamily  Font family name, as specified in a font-family style attribute.
     * @param fontWeight  Font weight as specified in a font-weight style attribute (typically 100 - 900).
     * @param fontStyle   Font style as specified in a font-style style attribute ("normal",
     * "italic", "oblique").
     * @param fontStretch Font stretch as specified in a font-stretch style attribute. It is treated
     * as a percentage value, where 100 maps to "normal". The typical range is
     * between 50 ("ultra-condensed") and 200 ("ultra-expanded").
     * @return an Android Typeface instance, or null
     */
    open fun resolveFont(
        fontFamily: String?,
        fontWeight: Float,
        fontStyle: String?,
        fontStretch: Float
    ): Typeface? {
        return null
    }

    /**
     * Called by renderer to resolve image file references in &lt;image&gt; elements.
     *
     *
     * An implementation of this method should return a `Bitmap` instance, or null if
     * you want the renderer to ignore this image.
     *
     *
     * Note that AndroidSVG does not attempt to cache Bitmap references.  If you want
     * them cached, for speed or memory reasons, you should do so yourself.
     *
     * @param filename the filename as provided in the xlink:href attribute of a &lt;image&gt; element.
     * @return an Android Bitmap object, or null if the image could not be found.
     */
    open fun resolveImage(filename: String?): Bitmap? {
        return null
    }

    /**
     * Called by the parser to resolve CSS stylesheet file references in &lt;?xml-stylesheet?&gt;
     * processing instructions.
     *
     *
     * An implementation of this method should return a `String` whose contents
     * correspond to the URL passed in.
     *
     *
     * Note that AndroidSVG does not attempt to cache stylesheet references.  If you want
     * them cached, for speed or memory reasons, you should do so yourself.
     *
     * @param url the URL of the CSS file as it appears in the SVG file.
     * @return a AndroidSVG CSSStyleSheet object, or null if the stylesheet could not be found.
     * @since 1.3
     */
    open fun resolveCSSStyleSheet(url: String?): String? {
        return null
    }

    /**
     * Called by renderer to determine whether a particular format is supported.  In particular,
     * this method is used in &lt;switch&gt; elements when processing `requiredFormats`
     * conditionals.
     *
     * @param mimeType A MIME type (such as "image/jpeg").
     * @return true if your `resolveImage()` implementation supports this file format.
     */
    open fun isFormatSupported(mimeType: String?): Boolean {
        return false
    }
}