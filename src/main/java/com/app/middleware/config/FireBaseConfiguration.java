package com.app.middleware.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class FireBaseConfiguration {

    @Value("${firebase.database}")
    private String databaseUrl;

}
