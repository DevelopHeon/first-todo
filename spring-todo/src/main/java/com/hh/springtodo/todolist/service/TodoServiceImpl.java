package com.hh.springtodo.todolist.service;

import com.hh.springtodo.todolist.dto.TodoDto;
import com.hh.springtodo.todolist.entity.Todo;
import com.hh.springtodo.todolist.error.FindBadRequestException;
import com.hh.springtodo.todolist.error.PostNotFoundException;
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
    public Todo save(TodoDto todoDto) {
        Todo todo = modelMapper.map(todoDto, Todo.class);
        todoMapper.save(todo);
        todo = findById(todo.getId());
        return todo;
    }

    @Override
    public List<Todo> findAll() {
        return todoMapper.findAll();
    }

    @Override
    public Todo findById(Long id) {
        Optional<Todo> todo = todoMapper.findById(id);
        if(!todo.isPresent()){
            throw new PostNotFoundException("게시글 조회 실패");
        }
        return todo.get();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        findById(id);
        todoMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void updateTodos(Long id, TodoDto todoDto) {
        findById(id);
        Todo todo = Todo.builder()
                .id(id)
                .title(todoDto.getTitle())
                .content(todoDto.getContent())
                .build();
       this.todoMapper.updateTodos(todo);
    }

    @Override
    public List<Todo> searchAll(String searchType, String keyword) {
        if(!(searchType.equals("T") || searchType.equals("C"))){
            throw new FindBadRequestException("잘못된 요청입니다.");
        }
        Map<String, String> map = new HashMap<>();
        map.put("searchType", searchType);
        map.put("keyword", keyword);

        return todoMapper.searchAll(map);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Boolean status) {
        findById(id);
        Todo todo = Todo.builder()
                .id(id)
                .status(status)
                .build();
        todoMapper.updateStatus(todo);
    }

}
