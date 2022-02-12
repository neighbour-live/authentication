package com.app.middleware.utility;

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
        TEST_TEMPLATE("d-1ec49b198a1f7aaaa904e34b84548243"),
        WELCOME_TEMPLATE("d-d24badf1e961410f86a974a6d51b9758"),
        GENERIC_EMAIL("d-74a83d9d1a471f0a9546576081ee3d93"),
        EMAIL_OTP_TEMPLATE("d-2261ca6b5273f1dbedc43228d6c51bed");

        private final String value;

        public String value(){
            return this.value;
        }

        EmailTemplate(String value) {
            this.value = value;
        }
    }
}
