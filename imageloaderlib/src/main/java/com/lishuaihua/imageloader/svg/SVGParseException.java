package com.lishuaihua.imageloader.svg;

import org.xml.sax.SAXException;

/**
 * Thrown by the parser if a problem is found in the SVG file.
 */
public class SVGParseException extends SAXException {
    public SVGParseException(String msg) {
        super(msg);
    }

    public SVGParseException(String msg, Exception cause) {
        super(msg, cause);
    }
}
