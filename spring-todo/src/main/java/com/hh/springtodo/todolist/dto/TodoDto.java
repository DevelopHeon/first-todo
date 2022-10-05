package com.hh.springtodo.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class TodoDto {

    @NotNull
    private String title;
    @NotNull
    private String content;

    private String status;
}
