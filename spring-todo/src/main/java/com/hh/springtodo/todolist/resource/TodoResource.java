package com.hh.springtodo.todolist.resource;

import com.hh.springtodo.todolist.controller.TodoController;
import com.hh.springtodo.todolist.entity.Todo;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class TodoResource extends EntityModel<Todo> {
    public TodoResource(Todo todo) {
        super(todo);
        add(linkTo(TodoController.class).slash(todo.getId()).withSelfRel());
        add(linkTo(TodoController.class).slash(todo.getId()).withRel("updateTodos"));
        add(linkTo(TodoController.class).slash(todo.getId()).withRel("deleteTodos"));
        add(linkTo(TodoController.class).withRel("queryTodos"));
    }

}
