package com.besttravel.app.api.controllers.error_handler;

import com.besttravel.app.api.models.responses.BaseErrorResponse;
import com.besttravel.app.api.models.responses.ErrorResponse;
import com.besttravel.app.util.exceptions.IdNotFoundException;
import com.besttravel.app.util.exceptions.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundController {

	@ExceptionHandler({
		IdNotFoundException.class,
		UsernameNotFoundException.class
	})
	public BaseErrorResponse handleIdNotFound(RuntimeException exception) {
		return ErrorResponse.builder()
			.message(exception.getMessage())
			.code(HttpStatus.NOT_FOUND.value())
			.status(HttpStatus.NOT_FOUND.name())
			.build()
			;
	}
}
