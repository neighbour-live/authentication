package com.bot.middleware.persistence.request;

import com.bot.middleware.persistence.domain.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddReportUserRequest {

    @NotBlank
    private String subject;

    @NotBlank
    private String issue;

    @NotBlank
    private String reporterPublicId;

    @NotBlank
    private String reportedPublicId;

    private String ipAddress;
}
