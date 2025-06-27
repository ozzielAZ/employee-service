package com.invex.example.employee.exception;

import org.springframework.http.HttpStatus;


public class EmployeesApiException {

	public static final ResourceBoundleException NOTFOUND_EMPLOYEE = new ResourceBoundleException(1, HttpStatus.NOT_FOUND);
	
	public static final ResourceBoundleException LIST_EMPTY = new ResourceBoundleException(2, HttpStatus.BAD_REQUEST);
	
	public static final ResourceBoundleException NAME_EMPTY = new ResourceBoundleException(2, HttpStatus.BAD_REQUEST);
	
}
