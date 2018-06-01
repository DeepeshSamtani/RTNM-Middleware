package com.harman.rtnm.common.exception;

public class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9071629605704012879L;
	private int code;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BusinessException() {

	}

	public BusinessException(String message) {
		super(message);
		this.message=message;
	}

	public BusinessException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public BusinessException(int code) {
		super();
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "BusinessException [code=" + code + ", message=" + message + "]";
	}
}