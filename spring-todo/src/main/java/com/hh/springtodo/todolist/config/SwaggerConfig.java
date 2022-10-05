package com.hh.springtodo.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.http.HttpHeaders;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final String API_NAME = "study_swagger";
    private final String API_DISCRIPTION = "sample 명세";

    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }

    @Bean
    public Docket swaggerApi(){
        Parameter parameter = new ParameterBuilder()
                .name(HttpHeaders.AUTHORIZATION)
                .description(API_DISCRIPTION)
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .build();

        List<Parameter> globalParamters = new ArrayList<>();
        globalParamters.add(parameter);

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .produces(getProducesTypes())
                .consumes(getConsumes())
                .apiInfo(sgInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hh.springtodo.todolist.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo sgInfo() {
        return new ApiInfoBuilder()
                .title("TODO API")
                .description("TODO API 문서")
                .build();

    }

    private Set<String> getConsumes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json:charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        return consumes;
    }

    private Set<String> getProducesTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json:charset=UTF-8");
        return produces;
    }
}
