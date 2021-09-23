package com.bot.middleware.exceptions.type;

import com.bot.middleware.exceptions.error.ApplicationErrorType;
import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends BaseException {

    private ResourceNotFoundErrorType resourceNotFoundErrorType;

    public ResourceNotFoundException(ResourceNotFoundErrorType resourceNotFoundErrorType) {
        super(ApplicationErrorType.RESOURCE_NOT_FOUND);
        this.resourceNotFoundErrorType = resourceNotFoundErrorType;
    }

    public ResourceNotFoundException(ResourceNotFoundErrorType resourceNotFoundErrorType, String id) {
        super(ApplicationErrorType.RESOURCE_NOT_FOUND);
        resourceNotFoundErrorType.setValue(id);
        this.resourceNotFoundErrorType = resourceNotFoundErrorType;
    }

    public ResourceNotFoundException(Exception exception, ResourceNotFoundErrorType resourceNotFoundErrorType ) {
        super(exception, ApplicationErrorType.RESOURCE_NOT_FOUND);
        this.resourceNotFoundErrorType = resourceNotFoundErrorType;
    }

}
