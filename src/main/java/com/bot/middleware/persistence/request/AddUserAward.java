package com.bot.middleware.persistence.request;

import com.bot.middleware.persistence.domain.Award;
import com.bot.middleware.persistence.domain.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddUserAward {

    @NotNull
    private boolean isActive;

    @NotNull
    private boolean isUnlocked;

    @NotNull
    private int progress;

    @NotBlank
    private String awardPublicId;

    @NotBlank
    private String userPublicId;

    private String ipAddress;
}
