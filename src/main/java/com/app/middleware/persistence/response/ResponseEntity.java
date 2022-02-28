package com.app.middleware.persistence.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseEntity<T> {

    protected Integer status;
	protected String message;
	protected T data;

}
