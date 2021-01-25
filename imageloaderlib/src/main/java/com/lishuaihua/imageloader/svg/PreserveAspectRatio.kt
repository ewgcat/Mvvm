package com.lishuaihua.imageloader.svg

import com.lishuaihua.imageloader.svg.PreserveAspectRatio
import com.lishuaihua.imageloader.svg.SVGParseException
import kotlin.Throws
import com.lishuaihua.imageloader.svg.utils.TextScanner
import java.lang.IllegalArgumentException
import java.util.HashMap

/**
 * The PreserveAspectRatio class tells the renderer how to scale and position the
 * SVG document in the current viewport.  It is roughly equivalent to the
 * `preserveAspectRatio` attribute of an `<svg>` element.
 *
 *
 * In order for scaling to happen, the SVG document must have a viewBox attribute set.
 * For example:
 *
 * <pre>
 * `<svg version="1.1" viewBox="0 0 200 100">
` *

 */
class PreserveAspectRatio  /*
     * Private constructor
     */ internal constructor(
    /**
     * Returns the alignment value of this instance.
     *
     * @return the alignment
     */
    val alignment: Alignment?,
    /**
     * Returns the scale value of this instance.
     *
     * @return the scale
     */
    val scale: Scale?
) {

    /**
     * Determines how the document is to me positioned relative to the viewport (normally the canvas).
     *
     *
     * For the value `none`, the document is stretched to fit the viewport dimensions. For all
     * other values, the aspect ratio of the document is kept the same but the document is scaled to
     * fit the viewport.
     *
     * @since 1.2.0
     */
    enum class Alignment {
        /**
         * Document is stretched to fit both the width and height of the viewport. When using this Alignment value, the value of Scale is not used and will be ignored.
         */
        none,

        /**
         * Document is positioned at the top left of the viewport.
         */
        xMinYMin,

        /**
         * Document is positioned at the centre top of the viewport.
         */
        xMidYMin,

        /**
         * Document is positioned at the top right of the viewport.
         */
        xMaxYMin,

        /**
         * Document is positioned at the middle left of the viewport.
         */
        xMinYMid,

        /**
         * Document is centred in the viewport both vertically and horizontally.
         */
        xMidYMid,

        /**
         * Document is positioned at the middle right of the viewport.
         */
        xMaxYMid,

        /**
         * Document is positioned at the bottom left of the viewport.
         */
        xMinYMax,

        /**
         * Document is positioned at the bottom centre of the viewport.
         */
        xMidYMax,

        /**
         * Document is positioned at the bottom right of the viewport.
         */
        xMaxYMax
    }

    /**
     * Determine whether the scaled document fills the viewport entirely or is scaled to
     * fill the viewport without overflowing.
     *
     * @since 1.2.0
     */
    enum class Scale {
        /**
         * The document is scaled so that it is as large as possible without overflowing the viewport.
         * There may be blank areas on one or more sides of the document.
         */
        meet,

        /**
         * The document is scaled so that entirely fills the viewport. That means that some of the
         * document may fall outside the viewport and will not be rendered.
         */
        slice
    }

    companion object {
        private val aspectRatioKeywords: MutableMap<String, Alignment> = HashMap(10)

        /**
         * Draw document at its natural position and scale.
         */
        val UNSCALED = PreserveAspectRatio(null, null)

        /**
         * Stretch horizontally and vertically to fill the viewport.
         *
         *
         * Equivalent to `preserveAspectRatio="none"` in an SVG.
         */
        @JvmField
        val STRETCH = PreserveAspectRatio(Alignment.none, null)

        /**
         * Keep the document's aspect ratio, but scale it so that it fits neatly inside the viewport.
         *
         *
         * The document will be centred in the viewport and may have blank strips at either the top and
         * bottom of the viewport or at the sides.
         *
         *
         * Equivalent to `preserveAspectRatio="xMidYMid meet"` in an SVG.
         */
        @JvmField
        val LETTERBOX = PreserveAspectRatio(Alignment.xMidYMid, Scale.meet)

        /**
         * Keep the document's aspect ratio, but scale it so that it fits neatly inside the viewport.
         *
         *
         * The document will be positioned at the top of tall and narrow viewports, and at the left of short
         * and wide viewports.
         *
         *
         * Equivalent to `preserveAspectRatio="xMinYMin meet"` in an SVG.
         */
        val START = PreserveAspectRatio(Alignment.xMinYMin, Scale.meet)

        /**
         * Keep the document's aspect ratio, but scale it so that it fits neatly inside the viewport.
         *
         *
         * The document will be positioned at the bottom of tall and narrow viewports, and at the right of short
         * and wide viewports.
         *
         *
         * Equivalent to `preserveAspectRatio="xMaxYMax meet"` in an SVG.
         */
        val END = PreserveAspectRatio(Alignment.xMaxYMax, Scale.meet)

        /**
         * Keep the document's aspect ratio, but scale it so that it fits neatly inside the viewport.
         *
         *
         * The document will be positioned at the top of tall and narrow viewports, and at the centre of
         * short and wide viewports.
         *
         *
         * Equivalent to `preserveAspectRatio="xMidYMin meet"` in an SVG.
         */
        val TOP = PreserveAspectRatio(Alignment.xMidYMin, Scale.meet)

        /**
         * Keep the document's aspect ratio, but scale it so that it fits neatly inside the viewport.
         *
         *
         * The document will be positioned at the bottom of tall and narrow viewports, and at the centre of
         * short and wide viewports.
         *
         *
         * Equivalent to `preserveAspectRatio="xMidYMax meet"` in an SVG.
         */
        val BOTTOM = PreserveAspectRatio(Alignment.xMidYMax, Scale.meet)

        /**
         * Keep the document's aspect ratio, but scale it so that it fills the entire viewport.
         * This may result in some of the document falling outside the viewport.
         *
         *
         * The document will be positioned so that the centre of the document will always be visible,
         * but the edges of the document may not.
         *
         *
         * Equivalent to `preserveAspectRatio="xMidYMid slice"` in an SVG.
         */
        val FULLSCREEN = PreserveAspectRatio(Alignment.xMidYMid, Scale.slice)

        /**
         * Keep the document's aspect ratio, but scale it so that it fills the entire viewport.
         * This may result in some of the document falling outside the viewport.
         *
         *
         * The document will be positioned so that the top left of the document will always be visible,
         * but the right hand or bottom edge may not.
         *
         *
         * Equivalent to `preserveAspectRatio="xMinYMin slice"` in an SVG.
         */
        val FULLSCREEN_START = PreserveAspectRatio(Alignment.xMinYMin, Scale.slice)

        /**
         * Parse the given SVG `preserveAspectRation` attribute value and return an equivalent
         * instance of this class.
         *
         * @param value a string in the same format as an SVG `preserveAspectRatio` attribute
         * @return a instance of this class
         */
        @JvmStatic
        fun of(value: String): PreserveAspectRatio {
            return try {
                parsePreserveAspectRatio(value)
            } catch (e: SVGParseException) {
                throw IllegalArgumentException(e.message)
            }
        }

        @Throws(SVGParseException::class)
        private fun parsePreserveAspectRatio(`val`: String): PreserveAspectRatio {
            val scan = TextScanner(`val`)
            scan.skipWhitespace()
            var word = scan.nextToken()
            if ("defer" == word) {    // Ignore defer keyword
                scan.skipWhitespace()
                word = scan.nextToken()
            }
            val align = aspectRatioKeywords[word]
            var scale: Scale? = null
            scan.skipWhitespace()
            if (!scan.empty()) {
                val meetOrSlice = scan.nextToken()
                scale = when (meetOrSlice) {
                    "meet" -> Scale.meet
                    "slice" -> Scale.slice
                    else -> throw SVGParseException("Invalid preserveAspectRatio definition: $`val`")
                }
            }
            return PreserveAspectRatio(align, scale)
        }

        init {
            aspectRatioKeywords["none"] = Alignment.none
            aspectRatioKeywords["xMinYMin"] =
                Alignment.xMinYMin
            aspectRatioKeywords["xMidYMin"] =
                Alignment.xMidYMin
            aspectRatioKeywords["xMaxYMin"] = Alignment.xMaxYMin
            aspectRatioKeywords["xMinYMid"] = Alignment.xMinYMid
            aspectRatioKeywords["xMidYMid"] =
                Alignment.xMidYMid
            aspectRatioKeywords["xMaxYMid"] = Alignment.xMaxYMid
            aspectRatioKeywords["xMinYMax"] = Alignment.xMinYMax
            aspectRatioKeywords["xMidYMax"] =
                Alignment.xMidYMax
            aspectRatioKeywords["xMaxYMax"] = Alignment.xMaxYMax
        }
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as PreserveAspectRatio
        return alignment == other.alignment && scale == other.scale
    }

    override fun toString(): String {
        return alignment.toString() + " " + scale
    }
}