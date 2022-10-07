package com.hh.springtodo.todolist.repository;

import com.hh.springtodo.todolist.entity.Todo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface TodoMapper {
    void save(Todo todo);

    List<Todo> findAll();
    Optional<Todo> findById(Long id);
    void deleteById(Long id);

    void updateTodos(Todo todo);

    List<Todo> searchAll(Map<String, String> find);

    void updateStatus(Todo todo);
}
