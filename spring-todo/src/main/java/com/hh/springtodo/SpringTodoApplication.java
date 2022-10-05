package com.hh.springtodo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringTodoApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringTodoApplication.class);
        app.run(args);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
