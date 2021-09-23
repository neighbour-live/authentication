package com.bot.middleware.exceptions.error;

public enum ResourceAlreadyExistErrorType implements ErrorEnum {

    // ------- ResourceAlreadyExistErrorType : 1000 -------- //
    RESOURCE_ALREADY_EXIST(1000, "Resource Already Exist"),
    //User : 1001 - 1100
    USER_ALREADY_EXIST_WITH_PUBLIC_ID(1001, "User already exist with public ID : %s"),
    USER_ALREADY_EXIST_WITH_PHONE_NUMBER(1002, "User already exist with phone number : %s"),
    USER_ALREADY_EXIST_WITH_EMAIL(1003, "User already exist with email : %s"),
    USER_ALREADY_EXIST_WITH_ACCESS_TOKEN(1004, "User already exist with access token : %s"),
    USER_ALREADY_EXIST_WITH_EMAIL_VERIFICATION_TOKEN(1005, "User already exist with email verification token token : %s"),
    USER_ALREADY_EXIST_WITH_PHONE_VERIFICATION_TOKEN(1006, "User already exist with phone verification token : %s"),
    USER_ALREADY_EXIST_WITH_STRIPE_ID(1007, "User already exist with Stripe ID : %s"),
    USER_ALREADY_EXIST_WITH_CONNECT_ID(1007, "User already exist with Connect ID : %s"),
    //1101-above
    AWARD_ALREADY_EXIST_WITH_PUBLIC_ID(1008, "Award already exist with public ID : %s"),
    USER_AWARD_ALREADY_EXIST_WITH_PUBLIC_ID(1009, "User Award already exist with public ID : %s"),

    SKILL_EXIST_WITH_PUBLIC_ID(1010, "Skill already exist with public ID : %s"),
    USER_SKILL_ALREADY_EXIST_WITH_PUBLIC_ID(1011, "User skill already exist with public ID : %s"),

    PAYMENT_CARD_EXIST_WITH_PUBLIC_ID(1012, "Payment card already exist with public ID : %s"),
    PAYMENT_CARD_NUMBER_ALREADY_EXIST(1013, "Payment card already exist with number : %s"),

    USER_ADDRESS_ALREADY_EXIST_WITH_PUBLIC_ID(1014, "User address already exist with public ID : %s"),

    REVIEW_AWARD_ALREADY_EXIST_WITH_PUBLIC_ID(1015, "Review Award already exist with public ID : %s"),

    TASK_EXIST_WITH_PUBLIC_ID(1016, "Task already exist with public ID : %s"),
    TASK_CATEGORY_EXIST_WITH_PUBLIC_ID(1017,"Task category already exist with public ID : %s"),

    BID_EXIST_WITH_PUBLIC_ID(1018, "Bid already exist with public ID : %s"),

    USER_BANK_EXIST_WITH_PUBLIC_ID(1019, "Bank Account already exist with public ID : %s"),
    USER_BANK_EXIST_WITH_ACCOUNT_NUMBER(1020, "Bank Account already exist with Account Number : %s"),

    USER_TRANSACTION_EXIST_WITH_PUBLIC_ID(1021, "Transaction already exist with public ID : %s"),
    USER_TRANSACTION_EXIST_WITH_PAYMENT_ID(1022, "Transaction already exist with payment ID : %s"),

    USER_WALLET_EXIST_WITH_PUBLIC_ID(1023, "Wallet already exist with public ID : %s"),
    SUPPORT_TICKET_EXIST_WITH_PUBLIC_ID(1024, "Support Ticket already exist with public ID : %s"),
    REPORT_TICKET_EXIST_WITH_PUBLIC_ID(1025, "Report Ticket already exist with public ID : %s"),

    REPORTER_AND_REPORTED_USER_CANNOT_BE_SAME(1026, "Reporter and reported user cannot be same"),

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
