package com.app.middleware.exceptions.type;

import com.app.middleware.exceptions.error.ApplicationErrorType;
import com.app.middleware.exceptions.error.SomethingUnexpectedErrorType;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SomethingUnexpectedException extends BaseException {

    private SomethingUnexpectedErrorType somethingUnexpectedErrorType;

    public SomethingUnexpectedException() {
        super(ApplicationErrorType.SOMETHING_UNEXPECTED_HAPPENED);
        this.somethingUnexpectedErrorType = SomethingUnexpectedErrorType.SOMETHING_UNEXPECTED_ERROR;
    }

    public SomethingUnexpectedException(Exception e) {
        super(e, ApplicationErrorType.SOMETHING_UNEXPECTED_HAPPENED);
        this.somethingUnexpectedErrorType = SomethingUnexpectedErrorType.SOMETHING_UNEXPECTED_ERROR;
    }

    public SomethingUnexpectedException(Exception e, SomethingUnexpectedErrorType somethingUnexpectedErrorType) {
        super(e, ApplicationErrorType.SOMETHING_UNEXPECTED_HAPPENED);
        this.somethingUnexpectedErrorType = somethingUnexpectedErrorType;
    }

    public SomethingUnexpectedException(SomethingUnexpectedErrorType somethingUnexpectedErrorType) {
        super(ApplicationErrorType.SOMETHING_UNEXPECTED_HAPPENED);
        this.somethingUnexpectedErrorType = somethingUnexpectedErrorType;
    }
}

