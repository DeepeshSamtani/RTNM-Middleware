package com.harman.rtnm.vo;

import java.io.Serializable;

public class MessageVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2478497149223163467L;
	private String message;
	private  int messagecode;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getMessagecode() {
		return messagecode;
	}
	public void setMessagecode(int messagecode) {
		this.messagecode = messagecode;
	}
	@Override
	public String toString() {
		return "MessageVO [message=" + message + ", messagecode=" + messagecode + "]";
	}
	
	

}
