package com.managementsystem.exception;

/**
 * 
* AuthorizationException - exception thrown when an authorization failure occurs.
* 
* This exception is used in service classes to handle authorization errors,
* such as invalid permissions or access control violations based on hierarchy 
* specified in the requirements.
*
* @author 
* @version Oct 7, 2024
 */
@SuppressWarnings("serial")
public class AuthorizationException extends Exception {
	
	public AuthorizationException() {
		super();
	}
	
	public AuthorizationException(String msg) {
		super(msg);
	}
}
