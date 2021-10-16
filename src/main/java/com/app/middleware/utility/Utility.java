package com.app.middleware.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Longs;
import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class Utility {
    public static String getUsernameFromToken(Map<String, Object> token) throws IOException {
        String[] split_string = ((String) token.get(AuthConstants.KEY_ACCESS_TOKEN)).split("\\.");
        String base64EncodedBody = split_string[1];
        String body = new String(Base64.getDecoder().decode(base64EncodedBody));
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(body, Map.class);
        if (map.containsKey("preferred_username")) {
            String username = map.get("preferred_username").toString();
            return username;
        }
        return "INVALID ";

    }

    public static boolean isLess(BigDecimal number, BigDecimal comparisonNumber) {
        return number.compareTo(comparisonNumber) < 0;
    }

    public static boolean isLessOrEqual(BigDecimal number, BigDecimal comparisonNumber) {
        return number.compareTo(comparisonNumber) <= 0;
    }

    public static boolean isGreater(BigDecimal number, BigDecimal comparisonNumber) {
        return number.compareTo(comparisonNumber) > 0;
    }

    public static boolean isGreaterOrEqual(BigDecimal number, BigDecimal comparisonNumber) {
        return number.compareTo(comparisonNumber) >= 0;
    }

    public static boolean isEqual(BigDecimal number, BigDecimal comparisonNumber) {
        return number.compareTo(comparisonNumber) == 0;
    }

    public static boolean isBefore(String date, String comparisonDate) {
        return date.compareTo(comparisonDate) < 0;
    }

    public static boolean isBeforeOrEqual(String date, String comparisonDate) {
        return date.compareTo(comparisonDate) <= 0;
    }

    public static boolean isAfter(String date, String comparisonDate) {
        return date.compareTo(comparisonDate) > 0;
    }

    public static boolean isAfterOrEqual(String date, String comparisonDate) {
        return date.compareTo(comparisonDate) >= 0;
    }


    public static int getIntegerValue(Integer data) {
        return data == null ? 0 : data;
    }

    public static boolean isNotEmpty(String data) {
        return !StringUtils.isEmpty(data);
    }

    public static String generateOTP() {
        return RandomStringUtils.randomNumeric(6);
    }

    public static String generateSafeToken() {
        StringBuilder tokenBuilder = new StringBuilder();
//        tokenBuilder.append(new String(Longs.toByteArray(System.currentTimeMillis())));
        tokenBuilder.append(RandomStringUtils.randomAlphanumeric(100));
        return tokenBuilder.toString();
    }

    public static int getPageNo(Integer pageNo) {
        return pageNo == null ? 0 : pageNo - 1;
    }

    public static boolean isSuccessful(Map<String, Object> resultMap) {
        return (boolean) resultMap.get(Constants.KEY_IS_SUCCESSFUL);
    }

    public static <T> T getDataFromResultMap(Map<String, Object> resultMap) {
        return (T) resultMap.get(Constants.KEY_DATA);

    }

    public static List<?> getErrorsFromResultMap(Map<String, Object> resultMap) {
        return (List<?>) resultMap.get(Constants.KEY_ERROR);
    }

    public static String convertDateToFormat(LocalDateTime dateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }

    public static ZonedDateTime convertToZonedDateTimeFromString(String dateTimeString, String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date = LocalDate.parse(dateTimeString, formatter);
        ZonedDateTime zonedDateTime = date.atStartOfDay(ZoneId.systemDefault());
        return zonedDateTime;
    }

    public static Long getDaysBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return Math.abs(ChronoUnit.DAYS.between(startDate, endDate));
    }

    public static Long getDays(LocalDateTime endDate) {
        return Math.abs(ChronoUnit.DAYS.between(LocalDateTime.now(), endDate));
    }

    public static String formatDate(String inputFormat, String outputFormat, String inputData) {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
        try {
            return outputDateFormat.format(inputDateFormat.parse(inputData));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFirstName(String name) {
        if (StringUtils.isEmpty(name)) return "";
        return org.apache.commons.lang3.StringUtils.capitalize(name.split(" ")[0].toLowerCase());
    }

    public static String toJson(Object o) {
        return o == null ? null : new Gson().toJson(o);
    }

    public static String convertValueToTwoDecimalPlaces(BigDecimal amount) {
        return String.format("%,.0f", amount);
    }

    public static String convertToTwoDigits(Number number) {
        if (number.longValue() < 10) return "0" + number.longValue();
        else return String.valueOf(number.longValue());
    }

    public static String addCommasToString(Number num)  {
        NumberFormat formatter = NumberFormat.getInstance();
        if (num != null) return formatter.format(num);
        else return "";
    }

    public static int getCurrentDayOfMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getAddedDayOfMonth(int days) {
        return getAddedDayOfMonth(days, Calendar.getInstance());
    }

    public static int getAddedDayOfMonth(int days, Calendar calendar) {
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
