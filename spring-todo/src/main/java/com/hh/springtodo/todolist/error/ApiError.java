package com.hh.springtodo.todolist.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter @Builder
public class ApiError {

    private String message;
    private String code;
}
