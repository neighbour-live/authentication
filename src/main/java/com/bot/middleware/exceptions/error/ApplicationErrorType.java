package com.bot.middleware.exceptions.error;
import com.bot.middleware.utility.StatusCode;

public enum ApplicationErrorType implements ErrorEnum {

    // ------- ApplicationErrorType -----

    SOMETHING_UNEXPECTED_HAPPENED(StatusCode.SOMETHING_UNEXPECTED_HAPPENED, "Something unexpected happened. Please contact the administrator."),
    RESOURCE_NOT_FOUND(StatusCode.NOT_FOUND, "Resource does not exist"),
    RESOURCE_ALREADY_EXISTS(StatusCode.ALREADY_EXISTS, "Resource already exists"),
    VALIDATION_ERROR(StatusCode.VALIDATION_ERROR, "Validation Error"),
    UNAUTHORIZED(StatusCode.UNAUTHORIZED, "Unauthorized Error"),
    INVALID_FORMAT_EXCEPTION(StatusCode.PARSING_ERROR, "Invalid Input"),
    ALADDIN(StatusCode.OK, "GENERAL Error");


    private Integer errorCode;
    private String errorMessage;

    ApplicationErrorType(Integer errorCode, String errorMessage) {
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
}





