package com.app.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddFaq {

    @NotBlank
    private String faqPublicId;

    @NotBlank
    private String userPublicId;

    @NotBlank
    private String question;

    @NotBlank
    private String description;

    private Boolean isActive;
}
