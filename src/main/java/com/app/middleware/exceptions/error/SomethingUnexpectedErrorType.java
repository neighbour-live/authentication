package com.app.middleware.exceptions.error;

public enum SomethingUnexpectedErrorType implements ErrorEnum {

    // ------- SomethingUnexpectedErrorType : 3000 -------- //
    SOMETHING_UNEXPECTED_ERROR(3000, "Something unexpected happened. Please contact the administrator."),
    DUPLICATE_PUBLIC_KEY(3001, "Duplicate Public Key.","Something unexpected happened. Please contact the administrator."),


    // --------  FIREBASE NOTIFICATION ERROR : 3100  ---------
    FIREBASE_CONFIGURATION_ERROR(3101, "Firebase Configuration Error"),
    FIREBASE_NOTIFICATION_ERROR(3102, "Firebase Notification Error"),


    // --------  CREATE USER REPOSITORY ERROR : 3300 ---------
    CREATE_USER_ERROR(3301, "Create User Error"),


    // ------- TOKEN ERROR : 3400 ----------
    INVALID_TOKEN(3401, "Invalid Token");


    private Integer errorCode;
    private String errorMessage;
    private String frontEndMessage;


    SomethingUnexpectedErrorType(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    SomethingUnexpectedErrorType(Integer errorCode, String errorMessage, String frontEndMessage) {
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
        if(this.frontEndMessage != null) return this.frontEndMessage;
        return this.errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return this.errorMessage;
    }

}
