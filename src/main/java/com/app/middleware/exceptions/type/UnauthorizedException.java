package com.app.middleware.exceptions.type;
import com.app.middleware.exceptions.error.ApplicationErrorType;
import com.app.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedException extends BaseException {

    private UnauthorizedExceptionErrorType unauthorizedExceptionErrorType;

    public UnauthorizedException(UnauthorizedExceptionErrorType unauthorizedExceptionErrorType) {
        super(ApplicationErrorType.UNAUTHORIZED);
        this.unauthorizedExceptionErrorType = unauthorizedExceptionErrorType;
    }

    public UnauthorizedException(UnauthorizedExceptionErrorType unauthorizedExceptionErrorType, String id) {
        super(ApplicationErrorType.UNAUTHORIZED);
        unauthorizedExceptionErrorType.setValue(id);
        this.unauthorizedExceptionErrorType = unauthorizedExceptionErrorType;
    }

    public UnauthorizedException(Exception exception, UnauthorizedExceptionErrorType unauthorizedExceptionErrorType ) {
        super(exception, ApplicationErrorType.UNAUTHORIZED);
        this.unauthorizedExceptionErrorType = unauthorizedExceptionErrorType;
    }

}