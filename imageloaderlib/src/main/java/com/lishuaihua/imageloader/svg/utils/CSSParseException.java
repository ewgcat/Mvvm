package com.lishuaihua.imageloader.svg.utils;

/*
 * Thrown by the CSS parser if a problem is found while parsing a CSS file.
 */
public class CSSParseException extends Exception {
    public CSSParseException(String msg) {
        super(msg);
    }

    public CSSParseException(String msg, Exception cause) {
        super(msg, cause);
    }
}
