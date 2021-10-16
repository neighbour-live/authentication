package com.app.middleware.exceptions.handler;

import com.app.middleware.exceptions.error.ApplicationErrorType;
import com.app.middleware.exceptions.error.SomethingUnexpectedErrorType;
import com.app.middleware.exceptions.logging.GenericLog;
import com.app.middleware.exceptions.response.ExceptionResponse;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RuntimeExceptionHandler {

    @Autowired
    private GenericLog log;


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse SomethingUnexpectedExceptionHandler(HttpServletRequest req, Exception e) {

        if( e instanceof HttpMessageNotReadableException
                && e.getCause() != null && e.getCause() instanceof InvalidFormatException) {
            return invalidFormatException(req, (InvalidFormatException) e.getCause());
        }

        if( e instanceof HttpMessageConversionException
                && e.getCause() != null && e.getCause() instanceof InvalidDefinitionException) {
            return invalidDefinitionException(req, (InvalidDefinitionException) e.getCause());
        }

        SomethingUnexpectedErrorType somethingUnexpectedErrorType = SomethingUnexpectedErrorType.SOMETHING_UNEXPECTED_ERROR;
        log.accessLoggerError("Response | URL:"+ req.getRequestURL()
                + " | Error Code : " + somethingUnexpectedErrorType.getErrorCode()
                + " | Error Name : " + somethingUnexpectedErrorType.getErrorMessage()
                + " | Error Message : " + e.getMessage()
                + " | Front End Message : " + somethingUnexpectedErrorType.getErrorMessage()
                + " | Error Detail : ", e);

        return ExceptionResponse.builder()
                .status(somethingUnexpectedErrorType.getErrorCode())
                .error_name(somethingUnexpectedErrorType == null ? null : somethingUnexpectedErrorType.toString())
                .message(somethingUnexpectedErrorType == null ? null : somethingUnexpectedErrorType.getErrorMessage())
                .build();
    }


    public ExceptionResponse invalidFormatException(HttpServletRequest req, final InvalidFormatException exception) {
        ApplicationErrorType applicationErrorType = ApplicationErrorType.INVALID_FORMAT_EXCEPTION;
        String frontEndMsg = String.format("%s. %s : %s", applicationErrorType.getErrorMessage(),
                (CollectionUtils.isEmpty(exception.getPath()) ? " " : exception.getPath().get(0).getFieldName()), exception.getValue());
        log.accessLoggerError("Response | URL:"+ req.getRequestURL()
                + " | Error Code : " + applicationErrorType.getErrorCode()
                + " | Error Name : " + applicationErrorType.getErrorMessage()
                + " | Error Message : " + exception.getMessage()
                + " | Front End Message : " + frontEndMsg);
        return createExceptionResponse(applicationErrorType, frontEndMsg);
    }

    public ExceptionResponse invalidDefinitionException(HttpServletRequest req, final InvalidDefinitionException exception) {
        ApplicationErrorType applicationErrorType = ApplicationErrorType.INVALID_FORMAT_EXCEPTION;
        String frontEndMsg = String.format("%s for %s", applicationErrorType.getErrorMessage(),
                (CollectionUtils.isEmpty(exception.getPath()) ? " " : exception.getPath().get(0).getFieldName()));
        log.accessLoggerError("Response | URL:"+ req.getRequestURL()
                + " | Error Code : " + applicationErrorType.getErrorCode()
                + " | Error Name : " + applicationErrorType.getErrorMessage()
                + " | Error Message : " + exception.getMessage()
                + " | Front End Message : " + frontEndMsg);
        return createExceptionResponse(applicationErrorType, frontEndMsg);
    }

    private ExceptionResponse createExceptionResponse(ApplicationErrorType applicationErrorType, String frontEndMsg) {
        return ExceptionResponse.builder()
                .status(applicationErrorType.getErrorCode())
                .error_name(applicationErrorType.getErrorMessage())
                .message(frontEndMsg)
                .build();
    }
}
