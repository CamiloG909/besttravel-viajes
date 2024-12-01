package com.besttravel.app.util.exceptions;

public class IdNotFoundException extends RuntimeException{
	private static final String ERROR_MESSAGE = "Id not found in %s";

	public IdNotFoundException(String tableName) {
		super(String.format(ERROR_MESSAGE, tableName));
	}
}
