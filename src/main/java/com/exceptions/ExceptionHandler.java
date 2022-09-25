package com.exceptions;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler{

	@org.springframework.web.bind.annotation.ExceptionHandler(CurrencyNotConfiguredException.class)
	public ResponseEntity<?> currencyNotFoundExceptionHandler(CurrencyNotConfiguredException ex, WebRequest request){
		
		Map<String, Object> parameters = new LinkedHashMap<>();
		
		parameters.put("timestamp", Instant.now().toString());
		parameters.put("error", ex.getMessage());
		
		return new ResponseEntity<>(parameters,HttpStatus.NOT_FOUND);
	}
	
}
