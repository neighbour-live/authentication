package com.bot.middleware.exceptions.error;

import org.springframework.util.StringUtils;

public enum UnauthorizedExceptionErrorType implements ErrorEnum {

    // ------- UnauthorizedExceptionErrorType : 5000 -------- //
    UNAUTHORIZED_ACTION(5001, "User in not authorized to Perform this Action"),
    ;

    private Integer errorCode;
    private String errorMessage;
    private String value;
    private String frontEndMessage;

    UnauthorizedExceptionErrorType(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    UnauthorizedExceptionErrorType(Integer errorCode, String errorMessage, String frontEndMessage) {
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

    public String getMessage() {
        if (StringUtils.isEmpty(value)) return this.errorMessage;
        return String.format(this.errorMessage, this.value);
    }

    public void setValue(String value) {
        this.value = value;
    }
}
