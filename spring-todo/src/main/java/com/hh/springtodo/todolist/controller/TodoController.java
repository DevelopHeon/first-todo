package com.hh.springtodo.todolist.controller;

import com.hh.springtodo.todolist.dto.TodoDto;
import com.hh.springtodo.todolist.entity.Todo;
import com.hh.springtodo.todolist.resource.TodoResource;
import com.hh.springtodo.todolist.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private final TodoService todoService;

    private Logger log = LoggerFactory.getLogger(TodoController.class);

    @PostMapping
    public ResponseEntity createTodos(@RequestBody @Valid TodoDto todoDto,
                                      Errors errors) {
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        log.debug(todoDto.toString());
        Long todoId = todoService.save(todoDto);
        log.info("ID : " + todoId.toString());

        WebMvcLinkBuilder selfLink = linkTo(TodoController.class).slash(todoId);
        URI uri = selfLink.toUri();
        return ResponseEntity.created(uri).body(todoDto);
    }

    @GetMapping
    public ResponseEntity queryTodos(@RequestParam(required = false, defaultValue = "T") String searchType,
                                     @RequestParam(required = false) String keyword){
        List<Todo> todoList = null;
        if(keyword == null){
            todoList = todoService.findAll();
        }else{
            todoList = todoService.searchAll(searchType, keyword);
        }

        if(todoList.size() == 0){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(todoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity getTodos(@PathVariable Long id){
        Optional<Todo> optionalTodo = this.todoService.findById(id);
        if(!optionalTodo.isPresent()){
            log.info("없는값 조회");
            return ResponseEntity.notFound().build();
        }
        Todo todo = optionalTodo.get();
        TodoResource todoResource = new TodoResource(todo);

        return ResponseEntity.ok(todoResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTodos(@PathVariable Long id,
                                      @RequestBody @Valid TodoDto todoDto,
                                      Errors errors){
        Optional<Todo> optionalTodo = this.todoService.findById(id);
        if(!optionalTodo.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Todo originTodo = optionalTodo.get();
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        this.todoService.updateTodos(originTodo, todoDto);
        return ResponseEntity.ok(todoDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTodos(@PathVariable Long id){
        int result = this.todoService.deleteById(id);
        log.info("result : " + result);
        if(result < 1){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("정상적으로 삭제 되었습니다.");
    }
}
