package com.harman.rtnm.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "SUBELEMENT")
@Entity
public class SubElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3811870610225449546L;

	@EmbeddedId
	private SubElementKey subElementKey;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "ELEMENT_ID")
	private Element element;

	public SubElementKey getSubElementKey() {
		return subElementKey;
	}

	public void setSubElementKey(SubElementKey subElementKey) {
		this.subElementKey = subElementKey;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

}
