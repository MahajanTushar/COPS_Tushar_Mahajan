package com.exceptions;

public class CurrencyNotConfiguredException extends RuntimeException{

	private static final long serialVersionUID = -718830939730595269L;

	private String message;
	
	@Override
	public String getMessage() {
		return message;
	}
	
	public CurrencyNotConfiguredException(String message) {
		this.message = message;
	}
	
}
