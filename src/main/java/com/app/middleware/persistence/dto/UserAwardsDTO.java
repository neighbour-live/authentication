package com.app.middleware.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

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
