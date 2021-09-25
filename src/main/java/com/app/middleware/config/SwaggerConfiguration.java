package com.app.middleware.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
@Profile({"!qa"})
public class SwaggerConfiguration extends WebMvcConfigurerAdapter {
    public static final Contact DEFAULT_CONTACT = new Contact(
            "Abdul Sami Haroon",
            "www.neighbour.live",
            "abdulsami.se@gmail.com"
    );

    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
            "API Documentation",
            "This is swagger documentation API Resources.",
            "0.0.1",
            "Company Private property.",
            DEFAULT_CONTACT,
            "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0",
            Collections.emptyList()
    );

    private static final Set<String> DEFAULT_PRODUCERS_AND_CONSUMERS = new HashSet<String>(Arrays.asList("application/json"));
    Parameter headerParam = new ParameterBuilder()
            .name("Authorization")
            .defaultValue("Bearer ")
            .parameterType("header")
            .modelRef(new ModelRef("string"))
            .description("Tenant Identity")
            .required(true)
            .build();

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.
                addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("**/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/")
                .setViewName("forward:" + "/swagger-ui/index.html");
    }
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.app.middleware.resources.controller"))
                .paths(PathSelectors.any())
                .build();
//                .produces(DEFAULT_PRODUCERS_AND_CONSUMERS)
//                .consumes(DEFAULT_PRODUCERS_AND_CONSUMERS);
    }
}
