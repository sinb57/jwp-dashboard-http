package org.apache.coyote.http11.response;

import static org.apache.coyote.http11.response.HttpResponseHeader.CONTENT_LENGTH;
import static org.apache.coyote.http11.response.HttpResponseHeader.CONTENT_TYPE;
import static org.apache.coyote.http11.response.HttpResponseHeader.LOCATION;
import static org.apache.coyote.http11.response.HttpResponseHeader.SET_COOKIE;

import org.apache.catalina.session.Session;

public class HttpResponse {

    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String CRLF = "\r\n";
    private static final String EMPTY = "";

    private final StringBuilder headers;
    private final String body;

    private HttpResponse(final StringBuilder headers, final String body) {
        this.headers = headers;
        this.body = body;
    }

    private HttpResponse(final StringBuilder headers) {
        this(headers, EMPTY);
    }

    private HttpResponse() {
        this(new StringBuilder(), EMPTY);
    }

    public static HttpResponse status(final HttpStatus status) {
        final HttpResponse response = new HttpResponse();

        final String statusLine = response.asStatusLine(status);

        response.appendLineWithCRLF(statusLine);

        return new HttpResponse(response.headers);
    }

    public static HttpResponse ok() {
        return HttpResponse.status(HttpStatus.OK);
    }

    public static HttpResponse found() {
        return HttpResponse.status(HttpStatus.FOUND);
    }

    private String asStatusLine(final HttpStatus status) {
        final int statusCode = status.getStatusCode();
        final String reasonPhrase = status.getReasonPhrase();

        final String statusDelimiter = " ";
        return String.join(statusDelimiter, HTTP_VERSION, String.valueOf(statusCode), reasonPhrase);
    }

    public HttpResponse setCookie(final Session session) {
        final String line = session.asLine();

        appendLineWithCRLF(SET_COOKIE.asLine(line));

        return new HttpResponse(headers);
    }

    public HttpResponse location(final String location) {
        appendLineWithCRLF(LOCATION.asLine(location));

        return new HttpResponse(headers);
    }

    public HttpResponse body(final String newBody) {
        return body(ContentType.HTML, newBody);
    }

    public HttpResponse body(final Resource resource) {
        final String fileName = resource.getName();
        final ContentType contentType = ContentType.from(fileName);

        final String newBody = resource.read();

        return body(contentType, newBody);
    }

    private HttpResponse body(final ContentType contentType, final String newBody) {
        final StringBuilder newHeaders = this
                .contentType(contentType)
                .contentLength(newBody)
                .headers;
        return new HttpResponse(newHeaders, newBody);
    }

    private HttpResponse contentType(final ContentType contentType) {
        final String value = ContentType.concat(contentType);

        appendLineWithCRLF(CONTENT_TYPE.asLine(value));

        return new HttpResponse(headers);
    }

    private HttpResponse contentLength(final String body) {
        final String value = String.valueOf(body.getBytes().length);

        appendLineWithCRLF(CONTENT_LENGTH.asLine(value));

        return new HttpResponse(headers, body);
    }

    public String asFormat() {
        return concatHeaderAndBody();
    }

    private String concatHeaderAndBody() {
        return headers + CRLF + body;
    }

    private void appendLineWithCRLF(final String line) {
        final String endOfLine = " ";
        headers.append(line);
        headers.append(endOfLine);
        headers.append(CRLF);
    }
}
