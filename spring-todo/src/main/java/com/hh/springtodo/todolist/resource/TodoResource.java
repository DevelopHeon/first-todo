package com.hh.springtodo.todolist.resource;

import com.hh.springtodo.todolist.controller.TodoController;
import com.hh.springtodo.todolist.entity.Todo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class TodoResource extends EntityModel<Todo> {

    public TodoResource(Todo todo, Link... links) {
        super(todo, Arrays.asList(links));
        add(linkTo(TodoController.class).slash(todo.getId()).withSelfRel());
    }

}
