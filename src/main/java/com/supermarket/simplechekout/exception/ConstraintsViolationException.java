package com.supermarket.simplechekout.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represent a constraint that was violate, like a repetition of unique value, or a missing
 * value in a not null field.
 *  
 * @author phillipe
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Some constraints were reached...")
public class ConstraintsViolationException extends RuntimeException {

	private static final long serialVersionUID = -3387516993224229948L;

	public ConstraintsViolationException(String message) {
		super(message);
	}

}
