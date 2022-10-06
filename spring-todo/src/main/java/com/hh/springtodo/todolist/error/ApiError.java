package com.hh.springtodo.todolist.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter @Builder
public class ApiError {

    private String message;
    private HttpStatus httpStatus;
    private String code;
}
