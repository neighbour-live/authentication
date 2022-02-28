package com.app.middleware.exceptions.error;

public interface ErrorEnum<E extends Enum<E>> {

    Integer getErrorCode();

    String getErrorMessage();

    void setErrorMessage(String errorMessage);

}


