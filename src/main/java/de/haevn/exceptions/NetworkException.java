package de.haevn.exceptions;

import java.net.http.HttpResponse;

public class NetworkException extends Exception {
    private final String url;
    private final int statusCode;
    private final String content;

    public NetworkException(Throwable other) {
        super(other);
        this.url = "";
        this.statusCode = 0;
        this.content = "";
    }

    public NetworkException(HttpResponse<?> response) {
        this(response.statusCode(), response.body().toString(), response.uri().toString());
    }

    public NetworkException(int statusCode, String content, String url) {
        super(String.format("NetworkException was thrown with status (%s) from \"%s\" with content \"%s\"", statusCode, url, content));
        this.url = url;
        this.statusCode = statusCode;
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
