package com.hh.springtodo.todolist.service;

import com.hh.springtodo.todolist.dto.TodoDto;
import com.hh.springtodo.todolist.entity.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    Long save(TodoDto todoDto);

    List<Todo> findAll();

    Optional<Todo> findById(Long id);

    int deleteById(Long id);

    void updateTodos(Todo originTodo, TodoDto todoDto);

    List<Todo> searchAll(String searchType, String keyword);
}
