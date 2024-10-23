package com.managementsystem.exception;

/**
 * 
 * AuthenticationException - Exception thrown when an authentication failure occurs.
 * 
 * This exception is used in service classes to handle authentication errors,
 * such as invalid credential.
 *
 * @author 
 * @version Oct 7, 2024
 */
@SuppressWarnings("serial")
public class AuthenticationException extends Exception {
	
	public AuthenticationException() {
		super();
	}
	
	public AuthenticationException(String msg) {
		super(msg);
	}
}
