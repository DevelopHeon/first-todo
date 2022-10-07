package com.hh.springtodo.todolist.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindBadRequestException extends RuntimeException{
    private String message;
}
