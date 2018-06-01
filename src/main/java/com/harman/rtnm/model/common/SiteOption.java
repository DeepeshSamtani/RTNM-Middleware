package com.harman.rtnm.model.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SITE_OPTION")
public class SiteOption implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8071196399038895126L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "DIMENSION", nullable = false)
	private String dimension;

	@Column(name = "VALUE", nullable = false)
	private String value;

	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getDimension() {
		return dimension;
	}


	public void setDimension(String dimension) {
		this.dimension = dimension;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public String toString() {
		return "SiteOption [id=" + id + ", dimension=" + dimension + ", value=" + value + "]";
	}

}
