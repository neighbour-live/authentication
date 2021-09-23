package com.bot.middleware.persistence.dto;

import com.bot.middleware.persistence.domain.Award;
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
public class UserAwardsDTO {
    private String publicId;
    private Boolean isActive;
    private Boolean isUnlocked;
    private Integer progress;
    private AwardDTO award;
    private String userPublicId;
}
