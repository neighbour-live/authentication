package com.bot.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddUserSkill {

    @NotBlank
    private String skillProficiency;

    @NotBlank
    private String skillPublicId;

    @NotBlank
    private String userPublicId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String ipAddress;
}
