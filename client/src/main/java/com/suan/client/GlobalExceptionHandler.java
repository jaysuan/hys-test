package com.suan.client;

import com.suan.client.apiclient.ServerApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public ErrorMessage handleClientErrors(ServerApiException ex) {
        LOGGER.error("Server API error", ex);
        return new ErrorMessage(ex.getStatus().value(), ex.getStatus().getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage uncaughtExceptionHandler(Throwable ex) {
        LOGGER.error("Uncaught exception occured", ex);
        return new ErrorMessage(500, "Internal Server Error");
    }

    static class ErrorMessage {
        private int status;
        private String message;

        public ErrorMessage(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
