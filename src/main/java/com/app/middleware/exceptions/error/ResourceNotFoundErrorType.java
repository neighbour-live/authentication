package com.app.middleware.exceptions.error;

public enum ResourceNotFoundErrorType implements ErrorEnum {

    // ------- ResourceNotFoundErrorType : 10000 -------- //

    // User : 10001 - 10050
    RESOURCE_NOT_FOUND(10000, "Resource not found with %s", "Resource not found"),

    USER_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_ADDRESS(10001, "User not found with this Public ID %s in User Address", "User not found with Public ID"),

    USER_NOT_FOUND_WITH_PUBLIC_ID(10000, "User not found with this Public Id : %s", "User not found"),

    USER_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_AWARD(10002, "User not found with this PublicId : %s in User Awards"),
    AWARD_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_AWARD(10004, "Award not found with this PublicId : %s in User Awards"),
    USER_FILE_NOT_FOUND_WITH_KEY_NAME(10005,"File not found with Key: %s"),
    NOT_FOUND_WITH(10000, "Not found with this : %s", "User not found"),

    USER_ADDRESS_NOT_FOUND_PUBLIC_ID(10017, "User address not found with public Id: %s", "User Address not found with publicId"),
    AWARD_NOT_FOUND_WITH_PUBLIC_ID(10018, "Award not found with this PublicId : %s","Award not found with publicId"),


    USER_NOT_FOUND_WITH_OTP(10026, "User not found with this OTP: %s", "User not found with OTP"),


    USER_TEMPORARY_NOT_FOUND_WITH_PUBLIC_ID(10039, "Temporary User not found with this PublicId : %s in Temporary Users"),
    NOTIFICATION_TYPE_NOT_FOUND_PUBLIC_ID(10039, "Notification type not found with PublicID : %s in Notification Types", "Notification type not found"),
    NOTIFICATION_NOT_FOUND_PUBLIC_ID(10040, "Notification not found with PublicID : %s in Notifications", "Notification not found"),
    CONVERSATION_NOT_FOUND_WITH_TASK_PUBLIC_ID(10041,"Conversation not found with Task PublicID : %s in Conversations", "Conversation not found"),
    CHAT_NOT_FOUND_WITH_PUBLIC_ID(10042,"Chat not found with PublicID : %s in Chats", "Chat not found"),

    //EXCEPTIONS
    AWARD_NOT_FOUND_WITH_AWARD_ID_IN_USER_AWARD(10050,"Award not found with PublicID %s in User Award", "Award not found with Public ID"),
    USER_NOT_FOUND_WITH_USER_ID_IN_USER_AWARD(10050,"User not found with PublicID %s in User Awards", "User not found with Public ID"),
    USER_NOT_FOUND_WITH_USER_ID_IN_USER_NOTIFICATION(10050,"User not found with PublicID %s in User Notifications", "User not found with Public ID"),
    NOTIFICATION_TYPE_NOT_FOUND_WITH_NOTIFICATION_TYPE_ID_IN_USER_NOTIFICATION(10050,"Notification type not found with notification_type Id %s in User notifications", "Notification type not found with ID"),
    USER_NOT_FOUND_WITH_USER_PUBLIC_ID_IN_USER_TEMPORARY(10050,"User not found with PublicID %s in Temporary Users", "User not found with Public ID"),
    FIRST_USER_NOT_FOUND_WITH_FIRST_USER_ID_IN_CONVERSATION(10050,"First User not found with PublicID %s in Conversation", "First User not found with Public ID"),
    SECOND_USER_NOT_FOUND_WITH_SECOND_USER_ID_IN_CONVERSATION(10050,"Second User not found with PublicID %s in Conversation", "Second User not found with Public ID"),
    USER_CONVERSATION_NOT_FOUND_WITH_USER_ID_IN_FIREBASE_LOGS(10050,"User not found with PublicID %s in FLogs", "User not found with Public ID"),
    USER_NOTIFICATION_NOT_FOUND_WITH_USER_NOTIFICATION_ID_IN_FIREBASE_LOGS(10050,"Notification not found with PublicID %s in FLogs", "Notification not found with Public ID"),
    REQUEST_TYPE_NOT_FOUND_WITH_REQUEST_TYPE_ID_IN_FIREBASE_LOGS(10050,"Request type not found with ID %s in FLogs", "Request type not found with Public ID"),
    ROLE_NOT_FOUND_WITH_ROLE_ID_IN_ROLE_PERMISSION(10050,"Role not found with ID %s in Role Permissions", "Role not found with ID"),
    PERMISSION_NOT_FOUND_WITH_PERMISSION_ID_IN_ROLE_PERMISSION(10050,"Permission not found with ID %s in Role Permissions", "Permission not found with ID"),
    ROLE_NOT_FOUND_WITH_ROLE_ID_IN_USER(10050,"Role not found with ID %s in Users", "Role not found with ID"),
    USER_NOT_FOUND_WITH_USER_ID_IN_USER_ADDRESS(10050,"User not found with PublicID %s in User Address", "User not found with Public ID"),
    CONVERSATION_NOT_FOUND_WITH_CONVERSATION_ID_IN_USER_CHAT(10050,"Conversation not found with PublicID %s in User Chat", "Conversation not found with Public ID"),
    RECEIVER_NOT_FOUND_WITH_RECEIVER_ID_IN_USER_CHAT(10050,"Receiver not found with PublicID %s in User Chat", "Receiver not found with Public ID"),
    SENDER_NOT_FOUND_WITH_SENDER_ID_IN_USER_CHAT(10050,"Sender not found with PublicID %s in User Chat", "Sender not found with Public ID"),
   ;

    private Integer errorCode;
    private String errorMessage;
    private String value;
    private String frontEndMessage;

    ResourceNotFoundErrorType(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    ResourceNotFoundErrorType(Integer errorCode, String errorMessage, String frontEndMessage) {
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
        if (value == null) return this.errorMessage;
        return String.format(this.errorMessage, this.value);
    }

    public void setValue(String value) {
        this.value = value;
    }
}

