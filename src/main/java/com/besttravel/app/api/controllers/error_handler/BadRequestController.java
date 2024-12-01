package com.besttravel.app.api.controllers.error_handler;

import com.besttravel.app.api.models.responses.BaseErrorResponse;
import com.besttravel.app.api.models.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestController {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public BaseErrorResponse handleIdNotFound(MethodArgumentNotValidException exception) {
//		var errors = new ArrayList<String>();
//		exception.getAllErrors().forEach(error->errors.add(error.getDefaultMessage()));

		return ErrorResponse.builder()
			.message(exception.getAllErrors().get(0).getDefaultMessage())
			.code(HttpStatus.BAD_REQUEST.value())
			.status(HttpStatus.BAD_REQUEST.name())
			.build()
			;
	}
}
