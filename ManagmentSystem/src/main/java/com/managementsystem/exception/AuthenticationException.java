package com.managementsystem.exception;

@SuppressWarnings("serial")
public class AuthenticationException extends Exception {
	
	public AuthenticationException() {
		super();
	}
	
	public AuthenticationException(String msg) {
		super(msg);
	}
}
