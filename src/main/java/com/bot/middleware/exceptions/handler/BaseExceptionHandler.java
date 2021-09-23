package com.bot.middleware.exceptions.handler;

import com.bot.middleware.exceptions.logging.GenericLog;
import com.bot.middleware.exceptions.response.ErrorResponse;
import com.bot.middleware.exceptions.response.ExceptionResponse;
import com.bot.middleware.exceptions.type.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class BaseExceptionHandler {

    @Autowired
    private GenericLog log;

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ExceptionResponse resourceNotFoundExceptionHandler(HttpServletRequest req, ResourceNotFoundException e) {

        log.accessLoggerError("Response | URL:"+ req.getRequestURL() +" | Request failed with message : "
                + e.getErrorMessage() + " and Error Status: " + e.getErrorCode() + " -- Error Details -- "
                + " | Error Code : " + e.getResourceNotFoundErrorType().getErrorCode()
                + " | Error Name : " + e.getResourceNotFoundErrorType().toString()
                + " | Error Message : " + e.getResourceNotFoundErrorType().getMessage()
                + " | Front-End Message : " + e.getResourceNotFoundErrorType().getErrorMessage());

        return ExceptionResponse.builder()
                .status(e.getResourceNotFoundErrorType().getErrorCode())
                .error_name(e.getResourceNotFoundErrorType() == null ? null : e.getResourceNotFoundErrorType().toString())
                .message(e.getResourceNotFoundErrorType() == null ? null : e.getResourceNotFoundErrorType().getErrorMessage())
                .build();
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ExceptionResponse ResourceAlreadyExistsExceptionHandler(HttpServletRequest req, ResourceAlreadyExistsException e) {

        log.accessLoggerError("Response | URL:"+ req.getRequestURL() +" | Request failed with message : "
                + e.getErrorMessage() + " and Error Status: " + e.getErrorCode() + " -- Error Details -- "
                + " | Error Code : " + e.getResourceAlreadyExistErrorType().getErrorCode()
                + " | Error Name : " + e.getResourceAlreadyExistErrorType().toString()
                + " | Error Message : " + e.getResourceAlreadyExistErrorType().getMessage()
                + " | Front-End Message : " + e.getResourceAlreadyExistErrorType().getErrorMessage());

        return ExceptionResponse.builder()
                .status(e.getResourceAlreadyExistErrorType().getErrorCode())
                .error_name(e.getResourceAlreadyExistErrorType() == null ? null : e.getResourceAlreadyExistErrorType().toString())
                .message(e.getResourceAlreadyExistErrorType() == null ? null : e.getResourceAlreadyExistErrorType().getErrorMessage())
                .build();
    }

    @ExceptionHandler(SomethingUnexpectedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse SomethingUnexpectedExceptionHandler(HttpServletRequest req, SomethingUnexpectedException e) {

        log.accessLoggerError("Response | URL:"+ req.getRequestURL() +" | Request failed with message : "
                + e.getErrorMessage() + " and Error Status: " + e.getErrorCode() + " -- Error Details -- "
                + " | Error Code : " + e.getSomethingUnexpectedErrorType().getErrorCode()
                + " | Error Name : " + e.getSomethingUnexpectedErrorType().toString()
                + " | Error Message : " + e.getSomethingUnexpectedErrorType().getMessage()
                + " | Front-End Message : " + e.getSomethingUnexpectedErrorType().getErrorMessage());

        return ExceptionResponse.builder()
                .status(e.getSomethingUnexpectedErrorType().getErrorCode())
                .error_name(e.getSomethingUnexpectedErrorType() == null ? null : e.getSomethingUnexpectedErrorType().toString())
                .message(e.getSomethingUnexpectedErrorType() == null ? null : e.getSomethingUnexpectedErrorType().getErrorMessage())
                .build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse UnauthorizedExceptionHandler(HttpServletRequest req, UnauthorizedException e) {

        log.accessLoggerError("Response | URL:"+ req.getRequestURL() +" | Request failed with message : "
                + e.getErrorMessage() + " and Error Status: " + e.getErrorCode() + " -- Error Details -- "
                + " | Error Code : " + e.getUnauthorizedExceptionErrorType().getErrorCode()
                + " | Error Name : " + e.getUnauthorizedExceptionErrorType().toString()
                + " | Error Message : " + e.getUnauthorizedExceptionErrorType().getMessage()
                + " | Front-End Message : " + e.getUnauthorizedExceptionErrorType().getErrorMessage());

        return ExceptionResponse.builder()
                .status(e.getUnauthorizedExceptionErrorType().getErrorCode())
                .error_name(e.getUnauthorizedExceptionErrorType() == null ? null : e.getUnauthorizedExceptionErrorType().toString())
                .message(e.getUnauthorizedExceptionErrorType() == null ? null : e.getUnauthorizedExceptionErrorType().getErrorMessage())
                .build();
    }

    @ExceptionHandler(GenericException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ExceptionResponse aladdinExceptionHandler(HttpServletRequest req, GenericException e) {

        log.accessLoggerError("Response | URL:"+ req.getRequestURL() +" | Request failed with message : "
                + e.getErrorMessage() + " and Error Status: " + e.getErrorCode() + " -- Error Details -- "
                + " | Error Code : " + e.getAladdinErrorType().getErrorCode()
                + " | Error Name : " + e.getAladdinErrorType().toString()
                + " | Error Message : " + e.getAladdinErrorType().getMessage()
                + " | Front-End Message : " + e.getAladdinErrorType().getErrorMessage());

        return ExceptionResponse.builder()
                .status(e.getAladdinErrorType().getErrorCode())
                .error_name(e.getAladdinErrorType() == null ? null : e.getAladdinErrorType().toString())
                .message(e.getAladdinErrorType() == null ? null : e.getAladdinErrorType().getErrorMessage())
                .build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse handleValidationException(HttpServletRequest req, ValidationException e) {



        List<ErrorResponse> listOfErrors = new ArrayList<>();
        if (!e.getListOfErrors().isEmpty())

            for (FieldError error : e.getListOfErrors()) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setErrorField(error.getField());
                errorResponse.setErrorMessage(error.getDefaultMessage());
                listOfErrors.add(errorResponse);
            }

        log.accessLoggerError("Response | URL:"+ req.getRequestURL() +" | Request failed with message : "
                + e.getErrorMessage() + " and Error Status: " + e.getErrorCode() + " | "
                + " Errors List : " + Arrays.toString(new List[]{listOfErrors}));

        return ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getErrorMessage() == null ? null : e.getErrorMessage())
                .errors(listOfErrors)
                .build();
    }
}