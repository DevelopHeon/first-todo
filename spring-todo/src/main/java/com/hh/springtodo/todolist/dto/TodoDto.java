package com.hh.springtodo.todolist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data @NoArgsConstructor
@AllArgsConstructor @Builder
@ApiModel(value = "TodoDto : Todo 내용")
public class TodoDto {

    @ApiModelProperty(value = "TodoList 제목", example = "오늘 할 일", required = true)
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @ApiModelProperty(value = "TodoList 본문 내용", example = "오후에 운동하기")
    @NotNull
    private String content;

    @ApiModelProperty(value = "TodoList 완료 상태")
    private boolean status;
}
