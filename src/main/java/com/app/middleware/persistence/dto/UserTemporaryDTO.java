package com.app.middleware.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTemporaryDTO {

    private String publicId;
    private String phoneNumber;
    private boolean phoneVerified;
    private String phoneToken;

    private String email;
    private String emailToken;
    private boolean emailVerified;
    private String userName;

}
