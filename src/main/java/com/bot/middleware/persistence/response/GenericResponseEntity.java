package com.bot.middleware.persistence.response;

import com.bot.middleware.persistence.dto.StatusMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class GenericResponseEntity {


    public static <T> org.springframework.http.ResponseEntity create(String message, Integer status, T data, Long totalSize, Integer pageSize, Integer totalPages,
                                                                     MultiValueMap<String, String> headers, HttpStatus httpStatus) {
        return new org.springframework.http.ResponseEntity(new PageableResponseEntity<T>(status, message, data, totalSize, pageSize,totalPages), headers,
                httpStatus);
    }

    public static <T> org.springframework.http.ResponseEntity create(PageableResponseEntity<T> pageableResponse, HttpStatus httpStatus) {
        return new org.springframework.http.ResponseEntity(pageableResponse, httpStatus);
    }

    public static <T> org.springframework.http.ResponseEntity create(String message, Integer status, T data,
                                                                     MultiValueMap<String, String> headers, HttpStatus httpStatus) {
        return new org.springframework.http.ResponseEntity(new ResponseEntity<>(status, message, data), headers,
                httpStatus);
    }

    public static <T> org.springframework.http.ResponseEntity create(String message, Integer status, T data, HttpStatus httpStatus) {
        return create(null, status, data, new LinkedMultiValueMap<String, String>(), httpStatus);

    }

    public static <T> org.springframework.http.ResponseEntity create(ResponseEntity<T> responseEntity, HttpStatus httpStatus) {
        return create(responseEntity.getMessage(), responseEntity.getStatus(), responseEntity.getData(), new LinkedMultiValueMap<String, String>(), httpStatus);
    }

    public static <T> org.springframework.http.ResponseEntity create(Integer status, T data,
                                                                     MultiValueMap<String, String> headers, HttpStatus httpStatus) {
        return create(null, status, data, headers, httpStatus);

    }

    public static <T> org.springframework.http.ResponseEntity create(Integer status, T data, HttpStatus httpStatus) {
        return create(null, status, data, new LinkedMultiValueMap<String, String>(), httpStatus);
    }

    public static <T> org.springframework.http.ResponseEntity create(Integer status, String message,
                                                                     MultiValueMap<String, String> headers, HttpStatus httpStatus) {
        return create(message, status, null, headers, httpStatus);
    }

    public static <T> org.springframework.http.ResponseEntity create(Integer status, String message,
                                                                     HttpStatus httpStatus) {
        return create(message, status, null, new LinkedMultiValueMap<String, String>(), httpStatus);
    }

    public static <T> org.springframework.http.ResponseEntity create(StatusMessageDTO statusMessageDTO, T data,
                                                                     HttpStatus httpStatus) {
        return create(statusMessageDTO.getMessage(), statusMessageDTO.getStatus(), data, new LinkedMultiValueMap<String, String>(), httpStatus);
    }

    public static <T> org.springframework.http.ResponseEntity create(StatusMessageDTO statusMessageDTO,
                                                                     HttpStatus httpStatus) {
        return create(statusMessageDTO.getMessage(), statusMessageDTO.getStatus(), null, new LinkedMultiValueMap<String, String>(), httpStatus);
    }

    public static <T> org.springframework.http.ResponseEntity create(StatusMessageDTO statusMessageDTO,
                                                                     MultiValueMap<String, String> headers, HttpStatus httpStatus) {
        return create(statusMessageDTO.getMessage(), statusMessageDTO.getStatus(), null, headers, httpStatus);
    }

}
