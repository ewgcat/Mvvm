package com.lishuaihua.imageloader.svg.utils

/**
 * Parse a SVG/CSS 'integer' or hex number from a String.
 * We use our own parser to gain a bit of speed.  This routine is
 * around twice as fast as the system one.
 */
internal class IntegerParser(
    private val value: Long, /*
     * Return the value of pos after the parse.
     */val endPos: Int
) {
    /*
     * Return the parsed value as an actual float.
     */
    fun value(): Int {
        return value.toInt()
    }

    companion object {
        /*
     * Scan the string for an SVG integer.
     * Assumes maxPos will not be greater than input.length().
     */
        @JvmStatic
        fun parseInt(input: String, startpos: Int, len: Int, includeSign: Boolean): IntegerParser? {
            var pos = startpos
            var isNegative = false
            var value: Long = 0
            var ch: Char
            if (pos >= len) return null // String is empty - no number found
            if (includeSign) {
                ch = input[pos]
                when (ch) {
                    '-' -> {
                        isNegative = true
                        pos++
                    }
                    '+' -> pos++
                }
            }
            val sigStart = pos
            while (pos < len) {
                ch = input[pos]
                if (ch >= '0' && ch <= '9') {
                    if (isNegative) {
                        value = value * 10 - (ch.toInt() - '0'.toInt())
                        if (value < Int.MIN_VALUE) return null
                    } else {
                        value = value * 10 + (ch.toInt() - '0'.toInt())
                        if (value > Int.MAX_VALUE) return null
                    }
                } else break
                pos++
            }

            // Have we seen anything number-ish at all so far?
            return if (pos == sigStart) {
                null
            } else IntegerParser(value, pos)
        }

        /*
     * Scan the string for an SVG hex integer.
     * Assumes maxPos will not be greater than input.length().
     */
        @JvmStatic
        fun parseHex(input: String, startpos: Int, len: Int): IntegerParser? {
            var pos = startpos
            var value: Long = 0
            var ch: Char
            if (pos >= len) return null // String is empty - no number found
            while (pos < len) {
                ch = input[pos]
                value = if (ch >= '0' && ch <= '9') {
                    value * 16 + (ch.toInt() - '0'.toInt())
                } else if (ch >= 'A' && ch <= 'F') {
                    value * 16 + (ch.toInt() - 'A'.toInt()) + 10
                } else if (ch >= 'a' && ch <= 'f') {
                    value * 16 + (ch.toInt() - 'a'.toInt()) + 10
                } else break
                if (value > 0xffffffffL) return null
                pos++
            }

            // Have we seen anything number-ish at all so far?
            return if (pos == startpos) {
                null
            } else IntegerParser(value, pos)
        }
    }
}