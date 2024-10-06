package com.managementsystem.exception;

@SuppressWarnings("serial")
public class AuthorizationException extends Exception {
	
	public AuthorizationException() {
		super();
	}
	
	public AuthorizationException(String msg) {
		super(msg);
	}
}
