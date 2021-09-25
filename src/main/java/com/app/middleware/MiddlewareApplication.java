package com.app.middleware;

import com.app.middleware.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.app.middleware.persistence"})
@EntityScan(basePackages = {"com.app.middleware.persistence"})
@ComponentScan(basePackages = {"com.app.middleware.*"})
@EnableJpaAuditing
@EnableAsync
@EnableTransactionManagement
@EnableScheduling
@EnableConfigurationProperties(AppProperties.class)
public class MiddlewareApplication {

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(MiddlewareApplication.class, args);
    }

}
