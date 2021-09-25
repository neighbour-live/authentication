package com.app.middleware.exceptions.error;

public enum ResourceNotFoundErrorType implements ErrorEnum {

    // ------- ResourceNotFoundErrorType : 10000 -------- //

    // User : 10001 - 10050
//    USER_NOT_FOUND_WITH_PUBLIC_ID(10001, "User not found with this Public Id : %s", "User not found"),
//    USER_NOT_FOUND_WITH_PHONE_NO(10001, "User not found with this Phone Number : %s"),
    USER_NOT_FOUND_WITH(10000, "User not found with this : %s", "User not found"),
    USER_NOT_FOUND_WITH_PUBLIC_ID(10000, "User not found with this Public Id : %s", "User not found"),
    USER_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_ADDRESS(10001, "User not found with this PublicId : %s in User Addresses"),
    USER_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_AWARD(10002, "User not found with this PublicId : %s in User Awards"),
    USER_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_PAYMENT_CARD(10003, "User not found with this PublicId : %s in User Payment Cards"),
    USER_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_SKILL(10004, "User not found with this PublicId : %s in User Skills"),
    AWARD_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_AWARD(10005, "Award not found with this PublicId : %s in User Awards"),
    SKILL_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_SKILL(10006, "SKill not found with this PublicId : %s in User Skills"),
    BLOCKED_BY_USER_NOT_FOUND_WITH_PUBLIC_ID_IN_TASK(10007, "blocked By User not found with this PublicId : %s in Tasks"),
    TASK_NOT_FOUND_WITH_PUBLIC_ID_IN_REVIEW(10008, "Task not found with this PublicId : %s in Reviews"),
    SUPPLIER_USER_NOT_FOUND_WITH_PUBLIC_ID_IN_TASK(10009, "Supplier not found with this PublicId : %s in Tasks"),
    POSTER_USER_NOT_FOUND_WITH_PUBLIC_ID_IN_REVIEW(10010, "Poster not found with this PublicId : %s in Reviews"),
    TASKER_NOT_FOUND_WITH_PUBLIC_ID_IN_REVIEW(10011, "Tasker not found with this PublicId : %s in Reviews"),
    TASKER_USER_NOT_FOUND_WITH_PUBLIC_ID_IN_TASK(10012, "Tasker not found with this PublicId : %s in Tasks"),



    SKILL_NOT_FOUND_WITH_PUBLIC_ID(10013, "Skill not found with this PublicId : %s","Skill not found with publicId"),
    USER_CERTIFICATION_NOT_FOUND_PUBLIC_ID(10014, "User certification not found with this PublicId : %s","User Certification not found with publicId"),
    USER_SKILL_NOT_FOUND_WITH_PUBLIC_ID(10015, "User Skill not found with this PublicId : %s","User Skill not found with publicId"),
    USER_NOT_FOUND_WITH_ID_IN_USER_CERTIFICATIONS(10016, "User not found with this Id","User not found with this Id in User Certifications"),
    USER_ADDRESS_NOT_FOUND_PUBLIC_ID(10017, "User address not found with public Id: %s", "User Address not found with publicId"),
    AWARD_NOT_FOUND_WITH_PUBLIC_ID(10018, "Award not found with this PublicId : %s","Award not found with publicId"),
    USER_BANK_ACCOUNT_NOT_FOUND_WITH_USER(10019, "User Bank Account not found with user. %s", "User bank account not found."),
    USER_PAYMENT_CARD_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_PAYMENT_CARD(10020, "User Payment Card not found with publicId. %s", "User Card account not found."),
    USER_BANK_ACCOUNT_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_BANK_ACCOUNT(10021, "User Bank Account not found with publicId. %s", "User Bank Account account not found."),
    TASK_NOT_FOUND_WITH_PUBLIC_ID(10022, "Task not found with public Id. %s", "Task not found with publicId"),
    USER_BID_NOT_FOUND_WITH_PUBLIC_ID(10023,"Bid not found with public Id. %s","Bid not found with publicId"),
    REVIEW_NOT_FOUND_WITH_PUBLIC_ID(10024,"Review not found with publicId %s", "Review not found with publicId"),
    USER_TRANSACTION_NOT_FOUND_WITH_USER_PUBLIC_ID(10025,"User Transaction not found with publicId %s","Transaction not found"),


    USER_NOT_FOUND_WITH_OTP(10026, "User not found with this OTP: %s", "User not found with OTP"),

    USER_ADDRESS_NOT_FOUND_WITH_PUBLIC_ID_IN_TASK(10027, "User Address not found with this PublicId : %s in Tasks"),
    TASK_TRANSACTION_NOT_FOUND_WITH_PUBLIC_ID_IN_TASK(10028, "Task transaction not found with this PublicId : %s in Tasks"),
    USER_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_BANK_ACCOUNT(10029, "User not found with this PublicId : %s in User Bank Account"),
    USER_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_WALLET(10030, "User not found with this PublicId : %s in User Wallet"),
    USER_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_TRANSACTIONS(10031, "User not found with this PublicId : %s in User Transactions"),
    USER_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_CERTIFICATIONS(10032, "User not found with this PublicId : %s in User Certifications"),
    USER_NOT_FOUND_WITH_PUBLIC_ID_IN_SUPPORT_USER(10033, "User not found with this PublicId : %s in Support User"),
    REPORTER_USER_NOT_FOUND_WITH_PUBLIC_ID_IN_REPORT_USER(10034, "Reporter User not found with this PublicId : %s in Report Report"),
    REPORTED_USER_NOT_FOUND_WITH_PUBLIC_ID_IN_REPORT_USER(10035, "Reported not found with this PublicId : %s in Report Report"),
    TASK_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_BIDS(10036, "Task not found with this PublicId : %s in User Bids"),
    USER_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_BIDS(10037, "User not found with this PublicId : %s in User Bids"),
    TASK_CATEGORY_NOT_FOUND_WITH_PUBLIC_ID(10038, "Task Category not found with this PublicId : %s in Task Categories"),
    USER_TEMPORARY_NOT_FOUND_WITH_PUBLIC_ID(10039, "Temporary User not found with this PublicId : %s in Temporary Users"),
    NOTIFICATION_TYPE_NOT_FOUND_PUBLIC_ID(10039, "Notification type not found with PublicID : %s in Notification Types", "Notification type not found"),
    NOTIFICATION_NOT_FOUND_PUBLIC_ID(10040, "Notification not found with PublicID : %s in Notifications", "Notification not found"),
    CONVERSATION_NOT_FOUND_WITH_TASK_PUBLIC_ID(10041,"Conversation not found with Task PublicID : %s in Conversations", "Conversation not found"),
    CHAT_NOT_FOUND_WITH_PUBLIC_ID(10042,"Chat not found with PublicID : %s in Chats", "Chat not found"),
    //Already Exist
    USER_ALREADY_EXIST_WITH_EMAIL(11001, "User already exist with this : %s", "User already exist with email"),
    USER_ALREADY_EXIST_WITH_PHONE(11002, "User already exist with this : %s", "User already exist with phone"),
//    USER_ALREADY_PINNED(10009, "User already pinned","User already pinned"),
//    USER_ALREADY_UNPINNED(10010, "User already unpinned","User already unpinned"),
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

