package com.requestManager.aspect;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.InputStream;

public class DecryptHttpInputMessage implements HttpInputMessage {
    private final HttpHeaders headers;
    private final InputStream body;

    public DecryptHttpInputMessage(InputStream body, HttpHeaders headers) {
        this.headers = headers;
        this.body = body;
    }

    @Override
    public InputStream getBody() {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
