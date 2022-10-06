package com.hh.springtodo.todolist.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionController {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity handlePostNotFoundException(PostNotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .httpStatus(httpStatus)
                .code("ERROR_404")
                .build();

        return ResponseEntity.status(httpStatus).body(apiError);
    }
}
