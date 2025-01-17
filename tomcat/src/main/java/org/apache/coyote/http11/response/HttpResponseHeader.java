package org.apache.coyote.http11.response;

public enum HttpResponseHeader {

    LOCATION("Location"),
    SET_COOKIE("Set-Cookie"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    ;

    private static final String COLON = ": ";

    private final String header;

    HttpResponseHeader(final String header) {
        this.header = header;
    }

    public String asLine(final String value) {
        return header + COLON + value;
    }
}
