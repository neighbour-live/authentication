package com.app.middleware.exceptions.type;


import com.app.middleware.exceptions.error.ApplicationErrorType;
import com.app.middleware.exceptions.error.ResourceAlreadyExistErrorType;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends BaseException {

    private ResourceAlreadyExistErrorType resourceAlreadyExistErrorType;

    public ResourceAlreadyExistsException(){
        super(ApplicationErrorType.RESOURCE_NOT_FOUND);
    }

    public ResourceAlreadyExistsException(ResourceAlreadyExistErrorType resourceAlreadyExistErrorType) {
        super(ApplicationErrorType.RESOURCE_ALREADY_EXISTS);
        resourceAlreadyExistErrorType.setValue(resourceAlreadyExistErrorType.getValue() == null ? "" : resourceAlreadyExistErrorType.getValue());
        this.resourceAlreadyExistErrorType = resourceAlreadyExistErrorType;
    }

    public ResourceAlreadyExistsException(ResourceAlreadyExistErrorType resourceAlreadyExistErrorType, String id) {
        super(ApplicationErrorType.RESOURCE_ALREADY_EXISTS);
        resourceAlreadyExistErrorType.setValue(id);
        this.resourceAlreadyExistErrorType = resourceAlreadyExistErrorType;
    }

    public ResourceAlreadyExistsException(Exception exception, ResourceAlreadyExistErrorType resourceAlreadyExistErrorType ) {
        super(exception, ApplicationErrorType.RESOURCE_ALREADY_EXISTS);
        this.resourceAlreadyExistErrorType = resourceAlreadyExistErrorType;
    }

}
