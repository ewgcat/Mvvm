package com.lishuaihua.imageloader.svg

import org.xml.sax.SAXException

/**
 * Thrown by the parser if a problem is found in the SVG file.
 */
class SVGParseException : SAXException {
    constructor(msg: String?) : super(msg) {}
    constructor(msg: String?, cause: Exception?) : super(msg, cause) {}
}