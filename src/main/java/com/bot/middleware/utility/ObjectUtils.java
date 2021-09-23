package com.bot.middleware.utility;

public class ObjectUtils {

    private ObjectUtils() {
    }

    public static boolean isEmpty(String string) {
        return (string == null || string.trim().length() == 0);
    }

    public static boolean isNull(Object object) {
        return (object instanceof String && isEmpty(((String) object))) || (object == null);
    }

}
