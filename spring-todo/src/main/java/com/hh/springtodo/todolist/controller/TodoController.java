package com.hh.springtodo.todolist.controller;

import com.hh.springtodo.todolist.dto.TodoDto;
import com.hh.springtodo.todolist.entity.Todo;
import com.hh.springtodo.todolist.resource.TodoResource;
import com.hh.springtodo.todolist.service.TodoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    private Logger log = LoggerFactory.getLogger(TodoController.class);

    @PostMapping
    @ApiOperation(value="Todo 생성", notes = "성공 시 link 반환")
    public ResponseEntity createTodos(@RequestBody @Valid TodoDto todoDto,
                                      Errors errors) {
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        Long todoId = todoService.save(todoDto);

        WebMvcLinkBuilder selfLink = linkTo(TodoController.class).slash(todoId);
        URI uri = selfLink.toUri();
        Todo todo = todoService.findById(todoId).get();

        TodoResource todoResource = new TodoResource(todo);
        todoResource.add(linkTo(TodoController.class).withRel("query-todos"));
        todoResource.add(linkTo(TodoController.class).slash(todoId).withRel("update-todo"));
        todoResource.add(linkTo(TodoController.class).slash(todoId).withRel("delete-todo"));

        return ResponseEntity.created(uri).body(todoResource);
    }

    @GetMapping
    @ApiOperation(value="TodoList 전체 조회 및 조건 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchType", value = "검색할 종류", dataType = "string"),
            @ApiImplicitParam(name = "keyword", value = "검색어", dataType = "string")
    })
    public ResponseEntity queryTodos(@RequestParam(required = false, defaultValue = "T") String searchType,
                                      @RequestParam(required = false) String keyword){
        List<EntityModel<Todo>> result = new ArrayList<>();
        List<Todo> todoList = null;
        if(keyword == null){
            todoList = todoService.findAll();
        }else{
            todoList = todoService.searchAll(searchType, keyword);
        }
        if(todoList.size() < 1){
            return ResponseEntity.notFound().build();
        }
        for(Todo todo : todoList){
            EntityModel<Todo> entityModel = EntityModel.of(todo);
            entityModel.add(linkTo(TodoController.class).slash(todo.getId()).withSelfRel());
            result.add(entityModel);
        }
        return ResponseEntity.ok(CollectionModel.of(result, linkTo(TodoController.class).withSelfRel()));
    }

    @GetMapping("/{id}")
    @ApiOperation(value="TodoList 하나만 조회")
    public ResponseEntity getTodos(@PathVariable Long id){
        Optional<Todo> optionalTodo = this.todoService.findById(id);
        if(!optionalTodo.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Todo todo = optionalTodo.get();
        TodoResource todoResource = new TodoResource(todo);
        todoResource.add(linkTo(TodoController.class).slash(todo.getId()).withRel("update-todo"));
        todoResource.add(linkTo(TodoController.class).slash(todo.getId()).withRel("delete-todo"));
        todoResource.add(linkTo(TodoController.class).withRel("query-todo"));

        return ResponseEntity.ok(todoResource);
    }

    @PutMapping("/{id}")
    @ApiOperation(value="TodoList 수정", notes = "성공 시 수정 된 todolist 반환")
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
        optionalTodo = todoService.findById(originTodo.getId());
        Todo updateTodo = optionalTodo.get();

        TodoResource todoResource = new TodoResource(updateTodo);
        return ResponseEntity.ok(todoResource);
    }

    @PutMapping
    @ApiOperation(value="TodoList 완료 상태 변경")
    public ResponseEntity updateStatus(@RequestParam Long id,
                                       @RequestParam String status){
        this.todoService.updateStatus(id, status);
        return new ResponseEntity("상태 변경 성공", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="TodoList 삭제하기")
    public ResponseEntity deleteTodos(@PathVariable Long id){
        int result = this.todoService.deleteById(id);
        if(result < 1){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity("삭제 성공", HttpStatus.OK);
    }
    private static ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(errors);
    }
}
