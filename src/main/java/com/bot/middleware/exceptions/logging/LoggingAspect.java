package com.bot.middleware.exceptions.logging;

import com.bot.middleware.exceptions.logging.helper.HttpRequestHelper;
import com.bot.middleware.exceptions.logging.helper.LogIdGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private GenericLog log;

    private List<String> sensitiveFields = Arrays.asList("password", "pin", "preAuthToken", "refreshToken", "auth");
    private List<String> restrictedURL = Arrays.asList("public/login", "public/signup", "public/validate", "public/refresh");

    @Before(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) && args(..)")
    public void requestAccessLog() {
        try {
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String url = !StringUtils.isEmpty(req.getRequestURL()) ? req.getRequestURL().toString() : "";

            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse().setHeader("logId", LogIdGenerator.generateLogId());

            StringBuilder input = new StringBuilder();
            input.append(String.format("Request | Method: %s", req.getMethod()));
            input.append(String.format(" | URL: %s", url));
            input.append(String.format(" | Requester: %s", req.getRemoteAddr()));
            input.append(String.format(" | Headers: %s", HttpRequestHelper.getRequestHeaders(req)));
            input.append(String.format(" | QueryStringParams:%s", HttpRequestHelper.getRequestParams(req)));
            String requestBody = removeSensitiveData(HttpRequestHelper.extractRequestBody(req));
            input.append(String.format(" | RequestBody:%s", requestBody));
            log.accessLogger(input.toString());
        } catch (Throwable e) {
            log.error(e.toString());
        }
    }

    private String removeSensitiveData(String requestBody) {
        if (requestBody != null && !requestBody.equals("")) {
            JsonObject jsonObject = new Gson().fromJson(requestBody, JsonObject.class);
            sensitiveFields.stream().forEach(s -> jsonObject.remove(s));
            return jsonObject.toString();
        }
        return null;
    }

    @AfterReturning(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) && args(..)", returning = "response")
    public void responseAccessLog(JoinPoint joinPoint, Object response) {
        try {
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            StringBuilder output = new StringBuilder();
            String url = !StringUtils.isEmpty(req.getRequestURL()) ? req.getRequestURL().toString() : "";
            output.append(String.format("Response | URL: %s", url));
            String responseData = response.toString();
            StringBuilder res = new StringBuilder();

            if (restrictedURL.stream().filter(s -> url.contains(s)).findAny().isPresent()) {
                int startIndex = responseData.indexOf("auth");
                int endIndex = responseData.indexOf("user");
                if (startIndex > 0) {
                    res.append(responseData, 0, startIndex);
                }
                if (endIndex > 0) {
                    res.append(responseData, endIndex, responseData.length() - 1);
                }
            } else {
                res.append(responseData);
            }

            output.append(String.format(" ResponseBody | : %s", res.toString()));
            log.accessLogger(output.toString());

            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse().setHeader("logId", "");
        } catch (Throwable e) {
            log.error(e.toString());
        }
    }

}