package com.hh.springtodo.todolist.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hh.springtodo.todolist.dto.TodoDto;
import com.hh.springtodo.todolist.entity.Todo;
import com.hh.springtodo.todolist.repository.TodoMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private TodoMapper todoMapper;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("생성 test")
    void createTodo() throws Exception {
        // given
        Todo newTodo = buildTodo(100);
        // when then
        this.mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTodo)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("_links.self").exists());
    }
    @Test
    @DisplayName("잘못된 입력 값으로 생성")
    void createTodo400() throws Exception {
        Todo newTodo = Todo.builder()
                .content("잘못 생성")
                .build();

        this.mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTodo)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].field").exists());
    }

    @Test
    @DisplayName("전체 조회 test")
    void queryTodos() throws Exception{
        for(int i=0; i<31; i++){
            Todo todo = buildTodo(i);
            todoMapper.save(todo);
        }
        this.mockMvc.perform(get("/api/todos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.todoList[0]._links.self").exists());
    }

    @Test
    @DisplayName("검색 테스트")
    void searchTodos() throws Exception{
        for(int i=0; i<31; i++){
            Todo todo = buildTodo(i);
            todoMapper.save(todo);
        }
        String searchType = "T";
        String keyword = "11";

        this.mockMvc.perform(get("/api/todos")
                        .param("searchType", searchType)
                        .param("keyword", keyword))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.todoList[0]._links.self").exists());

    }
    @Test
    @DisplayName("없는 값 검색")
    void searchTodos_notFound() throws Exception {
        for(int i=0; i<31; i++){
            Todo todo = buildTodo(i);
            todoMapper.save(todo);
        }
        String searchType = "T";
        String keyword = "123232";

        this.mockMvc.perform(get("/api/todos")
                        .param("searchType", searchType)
                        .param("keyword", keyword))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("todo 상세 조회")
    void getTodos() throws Exception{
        // given
        Todo todo = buildTodo(100);
        todoMapper.save(todo);

        // when then
        this.mockMvc.perform(get("/api/todos/{id}", todo.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("content").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.updateTodos").exists())
                .andExpect(jsonPath("_links.deleteTodos").exists())
                .andExpect(jsonPath("_links.queryTodos").exists());
    }

    @Test
    @DisplayName("없는 todo 상세 조회")
    void getTodos_isEmpty() throws Exception{
        Todo todo = Todo.builder().build();
        this.mockMvc.perform(get("/api/todos/1231232"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("todo 정상 수정하기 테스트")
    void putTodos() throws Exception{
        // given
        Todo todo = buildTodo(10);
        todoMapper.save(todo);
        TodoDto todoDto = modelMapper.map(todo, TodoDto.class);
        String newTitle = "수정 테스트";
        todoDto.setTitle(newTitle);

        // when then
        this.mockMvc.perform(put("/api/todos/{id}", todo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self").exists());
    }

    @Test
    @DisplayName("없는 todo 수정")
    void putTodos_isEmpty() throws Exception{
        Todo todo = buildTodo(10);
        todoMapper.save(todo);
        TodoDto todoDto = modelMapper.map(todo, TodoDto.class);

        this.mockMvc.perform(put("/api/todos/123222")
                .content(objectMapper.writeValueAsString(todoDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("입력 값 없이 수정")
    void putTodos_Input_Empty() throws Exception{
        Todo todo = buildTodo(10);
        todoMapper.save(todo);
        TodoDto updateTodo = new TodoDto();
        // when then
        this.mockMvc.perform(put("/api/todos/{id}", todo.getId())
                        .content(objectMapper.writeValueAsString(updateTodo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("정상적으로 todo 삭제하기")
    void deleteTodos() throws Exception{
        // given
        Todo todo = buildTodo(10);
        todoMapper.save(todo);

        this.mockMvc.perform(delete("/api/todos/{id}", todo.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("존재하지 않는 todo 삭제 오류")
    void deleteTodos_isEmpty() throws Exception{
        Todo todo = buildTodo(10);
        todoMapper.save(todo);

        this.mockMvc.perform(delete("/api/todos/123232"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("완료 상태로 변경 테스트")
    void statusUpdate() throws Exception{
        Todo todo = buildTodo(10);
        todoMapper.save(todo);
        boolean status = true;

        this.mockMvc.perform(patch("/api/todos")
                        .param("id", todo.getId().toString())
                        .param("status", String.valueOf(status)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 게시글 상태 변경 테스트")
    void statusUpdate_notFound() throws Exception {

        this.mockMvc.perform(patch("/api/todos")
                .param("id", "12321")
                .param("status", "true"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    private static Todo buildTodo(int index) {
        return Todo.builder()
                .title("테스트 제목 " + index)
                .content("테스트 내용")
                .build();
    }
}