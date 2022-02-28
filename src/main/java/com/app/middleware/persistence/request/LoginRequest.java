package com.app.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRequest {

    @Email(message = "Bad email format.")
    private String email;
    private String userName;
    private String phoneNumber;

    @NotBlank
    private String password;

    private boolean phoneLogin;
    private boolean emailLogin;
    private boolean userNameLogin;
    private String firebaseKey;
}
