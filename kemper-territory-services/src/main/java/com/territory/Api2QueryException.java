package com.territory;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class Api2QueryException extends RuntimeException {

    ErrorCode errorCode;
    HttpStatus httpStatus;
    String debugInfo;
    public Api2QueryException(
            ErrorCode errorCode,
            String message
    ) {
        super(message);
        this.errorCode = errorCode;
        setHttpStatus();
    }
    public String getErrorCode() {
        return errorCode.toString();
    }

    private void setHttpStatus() {
        if (errorCode.toString().equals("NOT_FOUND")) {
            this.httpStatus = HttpStatus.NOT_FOUND;
        } else {
            this.httpStatus = HttpStatus.BAD_REQUEST;
        }
    }
    @Override
    public String toString() {
        return "Api2QueryException{" +
                "errorCode=" + errorCode +
                ", message=" + getMessage() +
                '}';
    }
}


