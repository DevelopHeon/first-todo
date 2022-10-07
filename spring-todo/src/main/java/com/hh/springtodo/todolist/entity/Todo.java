package com.hh.springtodo.todolist.entity;

import lombok.*;

import java.time.LocalDate;

@Getter @AllArgsConstructor
@Builder @NoArgsConstructor
@Setter
public class Todo {

    private Long id;

    private String title;

    private String content;

    private Boolean status;

    private LocalDate createDate;
}
