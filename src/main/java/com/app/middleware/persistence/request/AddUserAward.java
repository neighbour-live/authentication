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
public class AddUserAward {
    @NotBlank
    private String awardPublicId;
    @NotBlank
    private String userPublicId;
    private Integer progress;
    private boolean isActive;
    private boolean isUnlocked;
}
