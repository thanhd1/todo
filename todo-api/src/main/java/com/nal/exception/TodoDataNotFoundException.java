package com.nal.exception;

public class TodoDataNotFoundException extends TodoException {

    public TodoDataNotFoundException() {
        super();
    }

    public TodoDataNotFoundException(String errorMsg) {
        super(errorMsg);
    }
}
