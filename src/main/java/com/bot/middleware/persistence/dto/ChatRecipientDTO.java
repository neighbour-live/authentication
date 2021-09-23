package com.bot.middleware.persistence.dto;


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
public class ChatRecipientDTO {
    private String publicId;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String phoneNumber;
}
