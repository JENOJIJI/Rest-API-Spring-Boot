package com.java.studenthandler.exception;

public class StudentListEmptyException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private  String errMessage;
	
	
	
	public StudentListEmptyException(String errMessage) {
		super();
		this.errMessage = errMessage;
	}
	public String getErrMessage() {
		return errMessage;
	}
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	

}
