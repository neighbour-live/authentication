package com.bot.middleware;

import com.bot.middleware.config.AppProperties;
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
@EnableJpaRepositories(basePackages = {"com.bot.middleware.persistence"})
@EntityScan(basePackages = {"com.bot.middleware.persistence"})
@ComponentScan(basePackages = {"com.bot.middleware.*"})
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
