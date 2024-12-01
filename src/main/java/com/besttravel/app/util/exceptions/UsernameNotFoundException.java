package com.besttravel.app.util.exceptions;

public class UsernameNotFoundException extends RuntimeException{
	private static final String ERROR_MESSAGE = "Username not found in %s";

	public UsernameNotFoundException(String tableName) {
		super(String.format(ERROR_MESSAGE, tableName));
	}
}
