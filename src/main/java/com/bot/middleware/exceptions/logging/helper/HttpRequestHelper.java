package com.bot.middleware.exceptions.logging.helper;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpRequestHelper {

    public static String extractRequestBody(HttpServletRequest request) {

        StringBuilder jb = new StringBuilder();
        try {
            jb.append(request.getAttribute(CustomJsonHttpMessageConverter.REQUEST_BODY_ATTRIBUTE_NAME).toString());
        } catch (Exception ex) {}
        return jb.toString();
    }

    public static Map getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headersMap = new HashMap<>();
        Enumeration headerEnumeration = request.getHeaderNames();
        while (headerEnumeration.hasMoreElements()) {
            String header = (String) headerEnumeration.nextElement();
            if(header.equalsIgnoreCase("authorization")) {
                continue;
            }
            headersMap.put(header, request.getHeader(header));
        }
        return headersMap;
    }

    public static Map getRequestParams(HttpServletRequest request) {
        Map<String, Object> paramsMap = new HashMap<>();
        Enumeration attrEnumeration = request.getAttributeNames();
        while (attrEnumeration.hasMoreElements()) {
            String param = (String) attrEnumeration.nextElement();
            if(param.equals("key.to.requestBody")) {
                continue;
            }
            if (!ignoreFrameworkParams(requestParamsToSkip, param)) {
                paramsMap.put(param, request.getAttribute(param));
            }
        }
        return paramsMap;
    }

    public static boolean ignoreFrameworkParams(String[] haystack, String needle) {
        try {
            for (String hay : haystack) {
                if (Objects.equals(needle.substring(0, hay.length()), hay)) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    private static final String[] requestParamsToSkip = {"org.springframework"};
}