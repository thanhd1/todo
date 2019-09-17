package com.nal.exception;

public class TodoBadRequestException extends TodoException {

    public TodoBadRequestException() {
        super();
    }

    public TodoBadRequestException(String errorMsg) {
        super(errorMsg);
    }
}
