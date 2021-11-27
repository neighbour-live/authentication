package com.app.middleware.exceptions.error;

public enum ResourceAlreadyExistErrorType implements ErrorEnum {

    // ------- ResourceAlreadyExistErrorType : 1000 -------- //

    RESOURCE_ALREADY_EXIST(1000, "Resource Already Exist"),

    USER_ALREADY_EXIST_WITH_PUBLIC_ID(1001, "User already exist with public ID %s"),
    USER_ALREADY_EXIST_WITH_PHONE_NUMBER(1002, "User already exist with phone number %s"),
    USER_ALREADY_EXIST_WITH_EMAIL(1003, "User already exist with email %s"),
    USER_ALREADY_EXIST_WITH_ACCESS_TOKEN(1004, "User already exist with access token %s"),
    USER_ALREADY_EXIST_WITH_EMAIL_VERIFICATION_TOKEN(1005, "User already exist with email verification token token %s"),
    USER_ALREADY_EXIST_WITH_PHONE_VERIFICATION_TOKEN(1006, "User already exist with phone verification token %s"),
    USER_ALREADY_EXIST_WITH_USERNAME(1007, "User already exist with Username %s"),
    USER_ALREADY_EXIST_WITH_REFRESH_TOKEN(1007, "User already exist with refresh token %s"),

    AWARD_ALREADY_EXIST_WITH_PUBLIC_ID(1008, "Award already exist with public ID %s"),
    USER_AWARD_ALREADY_EXIST_WITH_PUBLIC_ID(1009, "User Award already exist with public ID %s"),

    USER_CONVERSATION_EXIST_WITH_PUBLIC_ID(1010, "User conversation already exist with public ID %s"),
    USER_CHAT_ALREADY_EXIST_WITH_PUBLIC_ID(1011, "User chat already exist with public ID %s"),

    USER_TEMPORARY_EXIST_WITH_PUBLIC_ID(1012, "Temporary user already exist with public ID %s"),
    USER_TEMPORARY_EXIST_WITH_PHONE(1013, "Temporary user already exist with phone %s"),
    USER_TEMPORARY_EXIST_WITH_EMAIL(1014, "Temporary user already exist with email %s"),
    USER_TEMPORARY_EXIST_WITH_USERNAME(1015, "Temporary user already exist with username %s"),
    USER_TEMPORARY_EXIST_WITH_EMAIL_VERIFICATION_CODE(1016, "Temporary user already exist with email verification token %s"),
    USER_TEMPORARY_EXIST_WITH_PHONE_VERIFICATION_CODE(1017, "Temporary user already exist with phone verification token %s"),

    USER_ADDRESS_ALREADY_EXIST_WITH_PUBLIC_ID(1018, "User address already exist with public ID %s"),
    USER_UPLOAD_ALREADY_EXIST_WITH_PUBLIC_ID(1019, "User uploaded file already exist with public ID %s"),
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
}
