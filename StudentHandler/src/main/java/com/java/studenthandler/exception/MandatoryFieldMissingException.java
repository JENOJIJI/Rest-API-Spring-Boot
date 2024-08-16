package com.java.studenthandler.exception;

import java.io.Serial;

public class MandatoryFieldMissingException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	private final String errMessage;
	
	
	
	public MandatoryFieldMissingException(String errMessage) {
		super();
		this.errMessage = errMessage;
	}
	public String getErrMessage() {
		return errMessage;
	}

	

}
