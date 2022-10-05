package com.hh.springtodo.todolist.service;

import com.hh.springtodo.todolist.dto.TodoDto;
import com.hh.springtodo.todolist.entity.Todo;
import com.hh.springtodo.todolist.repository.TodoMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    @Autowired
    private final TodoMapper todoMapper;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public Long save(TodoDto todoDto) {
        Todo todo = Todo.builder()
                .title(todoDto.getTitle())
                .content(todoDto.getContent())
                .build();
        todoMapper.save(todo);
        return todo.getId();
    }

    @Override
    public List<Todo> findAll() {
        return todoMapper.findAll();
    }

    @Override
    public Optional<Todo> findById(Long id) {
        return todoMapper.findById(id);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return todoMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void updateTodos(Todo originTodo, TodoDto todoDto) {
        Todo todo = Todo.builder()
                .id(originTodo.getId())
                .title(todoDto.getTitle())
                .content(todoDto.getContent())
                .build();
        this.todoMapper.updateTodos(todo);
    }

    @Override
    public List<Todo> searchAll(String searchType, String keyword) {
        Map<String, String> map = new HashMap<>();
        map.put("searchType", searchType);
        map.put("keyword", keyword);
        return todoMapper.searchAll(map);
    }

}
