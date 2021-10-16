package com.app.middleware.exceptions.type;
import com.app.middleware.exceptions.error.ValidationErrorType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends BaseException {

    private List<FieldError> listOfErrors;

    public ValidationException(){
        super(ValidationErrorType.VALIDATION_ERROR);
    }

    public ValidationException(List<FieldError> listOfErrors) {
        super(ValidationErrorType.VALIDATION_ERROR);
        this.listOfErrors = listOfErrors;
    }

    public ValidationException(FieldError listOfErrors) {
        super(ValidationErrorType.VALIDATION_ERROR);
        this.listOfErrors = Arrays.asList(listOfErrors);
    }

}

