package com.app.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddAward {

    @NotBlank
    private String userPublicId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String awardIconUrl;

    @NotBlank
    private String awardType;

    private String ipAddress;
}
