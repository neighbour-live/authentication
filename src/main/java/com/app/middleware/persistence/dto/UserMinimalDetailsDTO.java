package com.app.middleware.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMinimalDetailsDTO {

    private String publicId;
    private String firstName;
    private String lastName;
    private String userName;
    private String imageUrl;
}
