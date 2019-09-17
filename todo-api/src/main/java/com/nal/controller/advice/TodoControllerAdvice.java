package com.nal.controller.advice;

import com.nal.exception.TodoBadRequestException;
import com.nal.exception.TodoDataNotFoundException;
import com.nal.exception.TodoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@org.springframework.web.bind.annotation.ControllerAdvice
public class TodoControllerAdvice {

    @ResponseBody
    @ExceptionHandler(TodoException.class)
    public ErrorObject todoExceptionHandler(TodoException ex) {
        HttpStatus status;

        if (ex instanceof TodoDataNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof TodoBadRequestException) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ErrorObject(status, ex.getErrorMsg());
    }

}
