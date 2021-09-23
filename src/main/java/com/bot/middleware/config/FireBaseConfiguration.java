package com.bot.middleware.config;

import com.bot.middleware.exceptions.error.SomethingUnexpectedErrorType;
import com.bot.middleware.exceptions.type.SomethingUnexpectedException;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@Slf4j
@Configuration
public class FireBaseConfiguration {

    @Value("https://bidontask.firebaseio.com")
    private String databaseUrl;

//    @Bean
//    public FirebaseApp init() throws SomethingUnexpectedException {
//        try {
//            InputStream inputStream = new ClassPathResource("confidential/bot-firebase-configuration.json").getInputStream();
//
//            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(inputStream))
//                    .setDatabaseUrl(databaseUrl)
//                    .build();
//
//            log.info("FireBase Database URL:" + databaseUrl);
//            FirebaseApp firebaseApp = FirebaseApp.initializeApp(firebaseOptions);
//            log.info("FireBase initialized successfully.");
//
//            return firebaseApp;
//
//        } catch (IOException e) {
//            log.error("Stack Trace: ",e);
//            throw new SomethingUnexpectedException(e, SomethingUnexpectedErrorType.FIREBASE_CONFIGURATION_ERROR);
//        }
//    }
}
