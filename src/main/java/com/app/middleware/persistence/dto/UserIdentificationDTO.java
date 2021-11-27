package com.app.middleware.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserIdentificationDTO {

    private String publicId;
    private boolean identificationVerified;
    private String idDocFrontUrl;
    private String idDocBackUrl;
    private String userName;

}
