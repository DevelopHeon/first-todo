package com.hh.springtodo.todolist.service;

import com.hh.springtodo.todolist.dto.TodoDto;
import com.hh.springtodo.todolist.entity.Todo;
import com.hh.springtodo.todolist.error.PostNotFoundException;
import com.hh.springtodo.todolist.repository.TodoMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
    public Todo save(TodoDto todoDto) {
        Todo todo = modelMapper.map(todoDto, Todo.class);
        todoMapper.save(todo);
        if(!ObjectUtils.isEmpty(todo.getId())){
            Optional<Todo> optionalTodo = todoMapper.findById(todo.getId());
            todo = optionalTodo.get();
        }
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
    public void deleteById(Long id) {
        int result = todoMapper.deleteById(id);
        if(result < 1){
            throw new PostNotFoundException("게시글 삭제 실패");
        }
    }

    @Override
    public void updateTodos(Long id, TodoDto todoDto) {
        Todo todo = Todo.builder()
                .id(id)
                .title(todoDto.getTitle())
                .content(todoDto.getContent())
                .build();
       int result = this.todoMapper.updateTodos(todo);
        if(result < 1){
            throw new PostNotFoundException("업데이트 실패");
        }
    }

    @Override
    public List<Todo> searchAll(String searchType, String keyword) {
        Map<String, String> map = new HashMap<>();
        map.put("searchType", searchType);
        map.put("keyword", keyword);
        return todoMapper.searchAll(map);
    }

    @Override
    public void updateStatus(Long id, boolean status) {
        Todo todo = Todo.builder()
                .id(id)
                .status(status)
                .build();
        int result = todoMapper.updateStatus(todo);
        if(result < 1){
            throw new PostNotFoundException("상태 변경 실패");
        }
    }

}
