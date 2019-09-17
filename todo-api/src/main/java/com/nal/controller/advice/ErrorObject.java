package com.nal.controller.advice;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@JsonPropertyOrder({"errorMsg", "status"})
public class ErrorObject implements Serializable {

    private String errorMsg;

    private Integer status;

    public ErrorObject(HttpStatus status, String errorMsg) {
        this.status = status.value();
        this.errorMsg = errorMsg;
    }
}
