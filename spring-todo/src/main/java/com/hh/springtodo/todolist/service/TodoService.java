package com.hh.springtodo.todolist.service;

import com.hh.springtodo.todolist.dto.TodoDto;
import com.hh.springtodo.todolist.entity.Todo;

import java.util.List;

public interface TodoService {
    Todo save(TodoDto todoDto);

    List<Todo> findAll();

    Todo findById(Long id);

    void deleteById(Long id);

    void updateTodos(Long id, TodoDto todoDto);

    List<Todo> searchAll(String searchType, String keyword);

    void updateStatus(Long id, Boolean status);
}
