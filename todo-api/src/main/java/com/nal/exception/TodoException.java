package com.nal.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TodoException extends RuntimeException {

    public TodoException() {
    }

    public TodoException(String errorMsg) {
        super(errorMsg);
    }

    public static TodoException.Builder dataNotFound(String message) {
        return new TodoException.Builder(TodoDataNotFoundException.class).error(message);
    }

    public static TodoException.Builder badRequest(String message) {
        return new TodoException.Builder(TodoBadRequestException.class).error(message);
    }

    public static TodoException.Builder internalServerError(String message) {
        return new TodoException.Builder(TodoInternalServerException.class).error(message);
    }

    public static class Builder {
        private String errorMsg;
        private Class<? extends TodoException> exceptionClass;

        public Builder(Class<? extends TodoException> exceptionClass) {
            this.exceptionClass = exceptionClass;
        }

        public Builder error(String errorMsg) {
            this.errorMsg = errorMsg;
            return Builder.this;
        }

        public TodoException build() {
            if (exceptionClass == TodoDataNotFoundException.class) {
                return new TodoDataNotFoundException(errorMsg);
            }

            if (exceptionClass == TodoBadRequestException.class) {
                return new TodoBadRequestException(errorMsg);
            }

            if (exceptionClass == TodoInternalServerException.class) {
                return new TodoInternalServerException(errorMsg);
            }

            // TODO: Add more exception...

            throw new UnsupportedOperationException(exceptionClass.getSimpleName() + " is not supported");
        }
    }
}
