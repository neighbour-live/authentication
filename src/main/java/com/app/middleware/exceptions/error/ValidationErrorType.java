package com.app.middleware.exceptions.error;

public enum ValidationErrorType implements ErrorEnum {

    // ------- ValidationErrorType : 4000 -------- //

    VALIDATION_ERROR(4001, "Validation Error");

    private Integer errorCode;
    private String errorMessage;

    ValidationErrorType(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public Integer getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}