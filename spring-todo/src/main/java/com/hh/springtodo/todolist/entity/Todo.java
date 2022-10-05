package com.hh.springtodo.todolist.entity;

import lombok.*;

import java.sql.Date;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder @ToString
public class Todo {

    private Long id;

    private String title;

    private String content;

    private String status;

    private Date createDate;

}
