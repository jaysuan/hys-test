package com.suan.client.apiclient;

import org.springframework.http.HttpStatus;

public class ServerApiException extends RuntimeException {
    private final HttpStatus status;

    public ServerApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public final HttpStatus getStatus() {
        return status;
    }

}
