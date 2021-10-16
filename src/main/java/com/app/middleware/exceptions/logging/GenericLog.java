package com.app.middleware.exceptions.logging;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
public class GenericLog {
    private final static Logger ACCESS_LOGGER = LoggerFactory.getLogger("accessLogger");
    private final static Logger CRON_LOGGER = LoggerFactory.getLogger("cronLogger");

    public void info(String msg) {
        log.info(createLogMsg(msg));
    }

    public void error(String msg) {
        log.error(createLogMsg(msg));
    }

    public void error(String msg, Throwable throwable) {
        log.error(createLogMsg(msg), throwable);
    }

    public void warn(String msg) {
        log.warn(createLogMsg(msg));
    }

    public void debug(String msg) {
        log.debug(createLogMsg(msg));
    }

    public void trace(String msg) {
        log.trace(createLogMsg(msg));
    }

    public void accessLogger(String msg) {
        ACCESS_LOGGER.info(createAccessLogMsg(msg));
    }

    public void accessLoggerError(String msg) {
        ACCESS_LOGGER.error(createAccessLogMsg(msg));
    }

    public void accessLoggerError(String msg, Exception e) {
        ACCESS_LOGGER.error(createAccessLogMsg(msg), e);
    }

    public void cronLogger(String msg) { CRON_LOGGER.info(msg); }

    public void cronLoggerError(String msg) { CRON_LOGGER.error(msg); }

    public void cronLoggerError(String msg, Throwable throwable) { CRON_LOGGER.error(msg, throwable); }

    public static void hibernateLog(String msg) {
        StringBuilder str = new StringBuilder();
        str.append("[ Hibernate Log ]");
        str.append(String.format(" Log ID : [ %s ] ", getStaticLogID()));
        str.append(msg);
        log.info(str.toString());
    }

    private static String getStaticLogID() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse().getHeader("logId");
        } catch (Exception e) {
            return "FailLog";
        }
    }

    public String getLogID() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse().getHeader("logId");
        } catch (Exception e) {
            return "FailLog";
        }
    }

    private String createAccessLogMsg(String msg) {
        StringBuilder str = new StringBuilder();
        str.append((String.format("Log ID : [ %s ] ", getLogID())));
        str.append(msg);
        return str.toString();
    }

    private String createLogMsg(String msg) {
        StringBuilder str = new StringBuilder();
        str.append((String.format("Log ID : [ %s ]", getLogID())));
        str.append((String.format(" [ %s ", Thread.currentThread().getStackTrace()[3].getClassName())));
        str.append((String.format("- %s ] ", Thread.currentThread().getStackTrace()[3].getLineNumber())));
        str.append(msg);
        return str.toString();
    }
}
