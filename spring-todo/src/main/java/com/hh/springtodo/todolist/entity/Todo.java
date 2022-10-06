package com.hh.springtodo.todolist.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter @AllArgsConstructor
@Builder @NoArgsConstructor
@Setter
public class Todo {

    private Long id;

    private String title;

    private String content;

    private boolean status;

    private LocalDateTime createDate;

}
