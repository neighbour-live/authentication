package com.app.middleware.exceptions.logging.helper;

import java.time.Instant;
import java.util.Random;

public class LogIdGenerator {

    public static String generateLogId(){
        Random rand = new Random();
        return Instant.now().toEpochMilli() + "_" + Long.valueOf(rand.nextInt(899999) + 100000L).toString();
    }
}
