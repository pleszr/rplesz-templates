package com.territory;


import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {
    private  String exceptionId;

    @ExceptionHandler(Api2QueryException.class)
    ProblemDetail handleApi2QueryException(Api2QueryException api2QueryException) {
        exceptionId = UUID.randomUUID().toString();
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(api2QueryException.getHttpStatus(), api2QueryException.getMessage());
        problemDetail.setProperty("exceptionId", exceptionId);
        problemDetail.setProperty("errorCode", api2QueryException.getErrorCode());

        log.error(
                "exceptionId: {}, debugInfo: {}",
                exceptionId,
                api2QueryException.getDebugInfo(),
                api2QueryException
        );

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        exceptionId = UUID.randomUUID().toString();
        BindingResult result = methodArgumentNotValidException.getBindingResult();

        String errorMessage = result.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
        problemDetail.setProperty("exceptionId", exceptionId);
        problemDetail.setProperty("errorCode", ErrorCode.MISSING_FIELD);
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
        ProblemDetail handleGenericException(Exception exception) {
        exceptionId = UUID.randomUUID().toString();
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,"Internal server error" );
        problemDetail.setProperty("exceptionId", exceptionId);

        log.error(
                "exceptionId: {}",
                exceptionId,
                exception
        );

        return problemDetail;
    }




}



