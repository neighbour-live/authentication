package com.bot.middleware.utility;

public class Constants {

    private Constants() {
    }

    public static final int DEVICE_LOCK_TIME_IN_MINUTES = 30;
    public static final int OTP_RETRY_LIMIT = 3;
    public static final int TOKEN_RETRY_LIMIT = 3;
    public static final int PAGE_SIZE = 100;
    public static final int TRUMP_YEAR = 1804;
    public static final int TOKEN_START_INDEX = 25;
    public static final int TOKEN_END_INDEX = 126;
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String KEY_IS_SUCCESSFUL = "is_successful";
    public static final String KEY_DATA = "data";
    public static final String KEY_ERROR = "error";
    public static final String ENV_LOCAL = "local";
    public static final String ENV_DEV = "dev";
    public static final String ENV_QA = "qa";
    public static final String ENV_PROD = "prod";
    public static final String SEND_GRID_FROM_EMAIL = "info@bidontask.com";

    public enum Sort {
        ASCENDING("asc"),
        DESCENDING("desc");

        private final String value;

        public String value(){
            return this.value;
        }

        Sort(String value) {
            this.value = value;
        }
    }

    public enum EmailTemplate {
        TEST_TEMPLATE("d-1ec1f7aaaa904b49b14e398a84548243"),
        WELCOME_TEMPLATE("d-c2b446dad02d4ec8abbe2e9e874a84e7"),
        TASK_COMPLETED_TEMPLATE("d-fce58b2692c74c82a8c70f6ffb73fc20"),
        WALLET_VERIFICATION_TEMPLATE("d-de202debf55447989663f4a0866ff004"),
        STERLING_VERIFICATION_TEMPLATE("d-c310846a8e5a457c88de9168b18d8d5d"),
        PHONE_VERIFICATION_TEMPLATE("d-35262f8b826c4f05a857ed7b5e1f04ac"),
        EMAIL_VERIFICATION_TEMPLATE("d-74a8471f0a95483d9d1a657601ee3d93"),
        TASK_ASSIGNED_TEMPLATE("d-f97e785552da4eba8160a01ec13991d4"),
        FUNDS_RECEIVED_TEMPLATE("d-6944fe497d1a476598e9a75e9feaccdd"),
        FUNDS_ISSUED_TEMPLATE("d-4228e9023ced48f4960a7d2c7daacca4"),
        DYNAMIC_EMAIL_VERIFICATION_TEMPLATE("d-2266b5273f1c43228d6ca1dbedc51bed");

        private final String value;

        public String value(){
            return this.value;
        }

        EmailTemplate(String value) {
            this.value = value;
        }
    }
}
