package com.WilsonQdop.Chamadas.exceptions.messageException;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class PatternMessageException {
    private final String message;
    private final HttpStatus httpStatus;
    private final LocalDateTime timeStamp;

    public PatternMessageException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timeStamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
