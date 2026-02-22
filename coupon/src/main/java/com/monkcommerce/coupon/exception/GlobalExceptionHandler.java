package com.monkcommerce.coupon.exception;


import com.monkcommerce.coupon.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handelRuntimeException(RuntimeException runtimeException){
         ErrorResponse errorResponse = new ErrorResponse(false,
                                     runtimeException.getMessage());
         return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handelException(Exception ex){
        ErrorResponse errorResponse = new ErrorResponse(false,
                    "Something went wrong");
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
