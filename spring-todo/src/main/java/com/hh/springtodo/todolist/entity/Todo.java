package com.hh.springtodo.todolist.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;

@Getter @AllArgsConstructor
@Builder @NoArgsConstructor
@Setter
public class Todo {
    @ApiModelProperty(value="아이디 고유 번호", example = "1")
    private Long id;

    private String title;

    private String content;

    private Boolean status;

    private LocalDate createDate;
}
