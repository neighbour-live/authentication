package com.bot.middleware.exceptions.error;

public enum AladdinErrorType implements ErrorEnum {


    INVALID_APP_VERSION(2001, "You are using an outdated version of the app, please update."),

    ;


    private Integer errorCode;
    private String errorMessage;
    private String value;
    private String frontEndMessage;

    AladdinErrorType(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    AladdinErrorType(Integer errorCode, String errorMessage, String frontEndMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.frontEndMessage = frontEndMessage;
    }

    @Override
    public Integer getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMessage() {
        if (this.frontEndMessage != null) return this.frontEndMessage;
        if (value == null) return this.errorMessage;
        return String.format(this.errorMessage, this.value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        if (value == null) return this.errorMessage;
        return String.format(this.errorMessage, this.value);
    }
}
