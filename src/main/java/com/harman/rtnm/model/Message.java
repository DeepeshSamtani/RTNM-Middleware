package com.harman.rtnm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MESSAGE")
public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6619745959035730368L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CODE")
	private Integer code;

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "LANGUAGE")
	private String language;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "Message [code=" + code + ", message=" + message + ", language=" + language + "]";
	}
}