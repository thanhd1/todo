package com.nal.exception;

public class TodoInternalServerException extends TodoException {

    public TodoInternalServerException() {
        super();
    }

    public TodoInternalServerException(String errorMsg) {
        super(errorMsg);
    }
}
