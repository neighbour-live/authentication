package com.app.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRequest {

    @Email(message = "Bad email format.")
    private String email;

    private String username;
    private String phone;

    @NotBlank
    private String password;

    private boolean phoneLogin;
    private boolean emailLogin;
    private boolean userNameLogin;
    private String firebaseKey;
}
