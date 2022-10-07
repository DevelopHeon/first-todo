package com.hh.springtodo.todolist.error;

import com.hh.springtodo.todolist.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestControllerAdvice
public class ApiExceptionController {

    @ExceptionHandler(PostNotFoundException.class)
    protected ResponseEntity handlePostNotFoundException(PostNotFoundException e) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .code("ERROR_404")
                .build();

        EntityModel<ApiError> errorResource = getIndex(apiError);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResource);
    }

    @ExceptionHandler(FindBadRequestException.class)
    protected ResponseEntity handleFindBadRequestException(FindBadRequestException e){
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .code("ERROR_400")
                .build();
        EntityModel<ApiError> errorResource = getIndex(apiError);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResource);
    }

    private static EntityModel<ApiError> getIndex(ApiError apiError) {
        return EntityModel.of(apiError, linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }
}
