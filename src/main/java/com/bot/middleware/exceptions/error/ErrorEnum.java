package com.bot.middleware.exceptions.error;

public interface ErrorEnum<E extends Enum<E>> {

    Integer getErrorCode();

    String getErrorMessage();


}


