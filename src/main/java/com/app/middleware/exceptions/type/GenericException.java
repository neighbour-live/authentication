package com.app.middleware.exceptions.type;


import com.app.middleware.exceptions.error.AppErrorType;
import com.app.middleware.exceptions.error.ApplicationErrorType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@NoArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class GenericException extends BaseException {

    private AppErrorType appErrorType;

    public GenericException(Exception e) {
        super(e, ApplicationErrorType.ALADDIN);
    }

    public GenericException(Exception e, AppErrorType appErrorType) {
        super(e, ApplicationErrorType.ALADDIN);
        this.appErrorType = appErrorType;
    }

    public GenericException(AppErrorType appErrorType) {
        super(ApplicationErrorType.ALADDIN);
        this.appErrorType = appErrorType;
    }

    public GenericException(AppErrorType appErrorType, String value) {
        super(ApplicationErrorType.ALADDIN);
        appErrorType.setValue(value);
        this.appErrorType = appErrorType;
    }

}