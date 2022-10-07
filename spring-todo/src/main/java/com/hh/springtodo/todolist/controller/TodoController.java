package com.hh.springtodo.todolist.controller;

import com.hh.springtodo.todolist.dto.TodoDto;
import com.hh.springtodo.todolist.entity.Todo;
import com.hh.springtodo.todolist.resource.ErrorResource;
import com.hh.springtodo.todolist.resource.TodoResource;
import com.hh.springtodo.todolist.service.TodoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService todoService;

    @ApiOperation(value = "TodoList 생성")
    @PostMapping
    public ResponseEntity createTodos(@RequestBody @Valid TodoDto todoDto,
                                      Errors errors) {
        if(errors.hasErrors()){
            return badRequest(errors);
        }

        Todo todo = todoService.save(todoDto);
        WebMvcLinkBuilder selfLink = linkTo(TodoController.class).slash(todo.getId());
        URI uri = selfLink.toUri();

        TodoResource todoResource = new TodoResource(todo);
        todoResource.add(linkTo(TodoController.class).withRel("queryTodos"));
        todoResource.add(selfLink.withRel("updateTodos"));
        todoResource.add(selfLink.withRel("deleteTodos"));

        return ResponseEntity.created(uri).body(todoResource);
    }


    @ApiOperation(value = "TodoList 전체 조회 및 조건 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchType", value = "검색 조건(T = 제목, C = 내용)"),
            @ApiImplicitParam(name = "keyword", value = "검색어")
    })
    @GetMapping
    public ResponseEntity queryTodos(@RequestParam(required = false) String searchType,
                                     @RequestParam(required = false) String keyword){
        List<EntityModel<Todo>> result = new ArrayList<>();
        List<Todo> todoList = null;
        if(keyword == null){
            todoList = todoService.findAll();
        }else{
            todoList = todoService.searchAll(searchType, keyword);
        }
        for(Todo todo : todoList){
            EntityModel<Todo> entityModel = EntityModel.of(todo);
            entityModel.add(linkTo(TodoController.class).slash(todo.getId()).withSelfRel());
            entityModel.add(linkTo(TodoController.class).withRel("updateStatus"));
            result.add(entityModel);
        }
        return ResponseEntity.ok(CollectionModel.of(result, linkTo(TodoController.class).withSelfRel()));
    }

    @ApiOperation(value = "TodoList 단일 조회")
    @ApiImplicitParam(name = "id", value = "TodoList 고유 번호", example = "1")
    @GetMapping("/{id}")
    public ResponseEntity getTodos(@PathVariable Long id) {
        Todo todo = this.todoService.findById(id);

        TodoResource todoResource = new TodoResource(todo);
        todoResource.add(linkTo(TodoController.class).slash(todo.getId()).withRel("updateTodos"));
        todoResource.add(linkTo(TodoController.class).slash(todo.getId()).withRel("deleteTodos"));
        todoResource.add(linkTo(TodoController.class).withRel("queryTodos"));

        return ResponseEntity.ok(todoResource);
    }

    @ApiOperation(value = "TodoList 수정")
    @ApiImplicitParam(name = "id", value = "TodoList 고유 번호", example = "1")
    @PutMapping("/{id}")
    public ResponseEntity updateTodos(@PathVariable Long id,
                                      @RequestBody @Valid TodoDto todoDto,
                                      Errors errors) {
        if(errors.hasErrors()){
            return badRequest(errors);
        }
        this.todoService.updateTodos(id, todoDto);
        Todo todo = todoService.findById(id);

        TodoResource todoResource = new TodoResource(todo);
        return ResponseEntity.ok(todoResource);
    }

    @ApiOperation(value = "TodoList Status 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "TodoList 고유 번호", example = "1"),
            @ApiImplicitParam(name = "status", value = "TodoList 완료 상태")
    })
    @PatchMapping
    public ResponseEntity updateStatus(@RequestParam Long id,
                                       @RequestParam Boolean status){
        this.todoService.updateStatus(id, status);
        return ResponseEntity.ok("상태 변경 성공");
    }

    @ApiOperation(value = "TodoList 삭제")
    @ApiImplicitParam(name = "id", value = "TodoList 고유 번호", example = "1")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteTodos(@PathVariable Long id){
        this.todoService.deleteById(id);
        return ResponseEntity.ok("게시글 삭제 성공");
    }
    private static ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }
}
