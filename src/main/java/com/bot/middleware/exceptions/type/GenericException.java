package com.bot.middleware.exceptions.type;


import com.bot.middleware.exceptions.error.AladdinErrorType;
import com.bot.middleware.exceptions.error.ApplicationErrorType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@NoArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class GenericException extends BaseException {

    private AladdinErrorType aladdinErrorType;

    public GenericException(Exception e) {
        super(e, ApplicationErrorType.ALADDIN);
    }

    public GenericException(Exception e, AladdinErrorType aladdinErrorType) {
        super(e, ApplicationErrorType.ALADDIN);
        this.aladdinErrorType = aladdinErrorType;
    }

    public GenericException(AladdinErrorType aladdinErrorType) {
        super(ApplicationErrorType.ALADDIN);
        this.aladdinErrorType = aladdinErrorType;
    }

    public GenericException(AladdinErrorType aladdinErrorType, String value) {
        super(ApplicationErrorType.ALADDIN);
        aladdinErrorType.setValue(value);
        this.aladdinErrorType = aladdinErrorType;
    }

}