package com.hh.springtodo.todolist.entity;

import lombok.*;

import java.sql.Date;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder @ToString
public class Todo {

    private Long id;

    private String title;

    private String content;

    private boolean isComplated;

    private Date createDate;

}
