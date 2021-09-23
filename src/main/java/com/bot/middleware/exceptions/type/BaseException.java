package com.bot.middleware.exceptions.type;


import com.bot.middleware.exceptions.error.ApplicationErrorType;
import com.bot.middleware.exceptions.error.ErrorEnum;
import com.bot.middleware.exceptions.logging.GenericLog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends Exception {

    @Autowired
    private GenericLog log = new GenericLog();

    private Integer errorCode;
    private String errorMessage;

    public BaseException() {
        this.errorCode = ApplicationErrorType.SOMETHING_UNEXPECTED_HAPPENED.getErrorCode();
        this.errorMessage = ApplicationErrorType.SOMETHING_UNEXPECTED_HAPPENED.getErrorMessage();
    }

    public BaseException(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BaseException(Exception exception, ErrorEnum<? extends Enum<?>> errorEnum) {
        super(exception);
        this.errorCode = errorEnum.getErrorCode();
        this.errorMessage = errorEnum.getErrorMessage();
        log.error("Exception : " + errorEnum.toString() + ". Error Detail : ", exception);
    }

    public BaseException(ErrorEnum<? extends Enum<?>> errorEnum) {
        this.errorCode = errorEnum.getErrorCode();
        this.errorMessage = errorEnum.getErrorMessage();
    }
}
