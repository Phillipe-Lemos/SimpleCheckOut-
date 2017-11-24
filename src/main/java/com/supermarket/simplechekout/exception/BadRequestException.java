package com.supermarket.simplechekout.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {

	private static final long serialVersionUID = -6737505788322703355L;

	public BadRequestException(String message) {
		super(message);
	}


}
