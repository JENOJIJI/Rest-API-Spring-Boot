package com.java.studenthandler.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.java.studenthandler.exception.IDNotFoundException;
import com.java.studenthandler.exception.MandatoryFieldMissingException;
import com.java.studenthandler.exception.StudentListEmptyException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(IDNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public String runtimeExceptionHandler(IDNotFoundException ex) {
		return ex.getErrMessage();
	}
	
	@ExceptionHandler(StudentListEmptyException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public String runtimeExceptionHandler(StudentListEmptyException ex) {
		return ex.getErrMessage();
	}
	
	@ExceptionHandler(MandatoryFieldMissingException.class)
	public ResponseEntity<String> mandatoryFieldMissingExceptionHandler(MandatoryFieldMissingException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getErrMessage());
	}

}
