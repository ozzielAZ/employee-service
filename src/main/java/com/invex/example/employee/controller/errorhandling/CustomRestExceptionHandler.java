package com.invex.example.employee.controller.errorhandling;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.invex.example.employee.exception.ResourceBoundleException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CustomRestExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ ResourceBoundleException.class })
	public ResponseEntity<Object> handleBusinessException(final ResourceBoundleException e, final WebRequest request) {
		final Map<String, Object> body = new HashMap<String, Object>();
		body.put("status", e.getErrorCode());
		body.put("error", "");
		body.put("message", e.getDescription());
		body.put("path", request.getDescription(false).split("uri=")[1]);
		log.error("Exception: " + e.getDescription());
		return ResponseEntity.status(e.getHttpStatusCode()).body(body);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();            
            if (fieldName.contains(".")) {
                fieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
            }
            errors.put(fieldName, violation.getMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
