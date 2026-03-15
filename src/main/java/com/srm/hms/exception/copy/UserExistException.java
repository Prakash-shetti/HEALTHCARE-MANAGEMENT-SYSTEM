package com.srm.hms.exception.copy;

public class UserExistException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7022763259052662335L;
	
	public UserExistException(String msg) {
		super(msg);
	}

}