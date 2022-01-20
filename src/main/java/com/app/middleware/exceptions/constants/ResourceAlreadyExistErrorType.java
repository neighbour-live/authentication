package com.app.middleware.exceptions.constants;

import com.app.middleware.exceptions.error.ErrorEnum;

public enum ResourceAlreadyExistErrorType implements ErrorEnum {

    // ------- ResourceAlreadyExistErrorType : 6000 -------- //
    RESOURCE_ALREADY_EXIST(6000, "Resource Already Exist"),

    // USER : 6050
    USER_ALREADY_EXIST_WITH_PHONE_NUMBER(6051, "User already exist with this Phone Number : %s"),
    USER_REFERRED_USER_ALREADY_EXISTS(6052, "User has a referral code applied already"),
    ;


    private Integer errorCode;
    private String errorMessage;
    private String frontEndMessage;
    private String value;

    ResourceAlreadyExistErrorType(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.value = null;
    }

    ResourceAlreadyExistErrorType(Integer errorCode, String errorMessage, String frontEndMessage) {
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
        if(value == null) return this.errorMessage;
        return String.format(this.errorMessage,this.value);
    }

    public String getMessage() {
        if(value == null) return this.errorMessage;
        return String.format(this.errorMessage,this.value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() { return this.value; }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}