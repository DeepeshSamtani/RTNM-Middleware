package com.harman.rtnm.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "ELEMENT")
@Entity
public class Element implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8678212952454822194L;

	@Id
	@Column(name = "ELEMENT_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long elementID;

	@Column(name = "ELEMENT_NAME")
	private String elementUserLabel;

	@Column(name = "VENDOR")
	private String vendor;
	
	@Column(name = "UNIVERSE")
	private String universe;
	
	@Column(name = "NETWORK_NAME")
	private String networkName;
	
	@Column(name = "NE_VERSION")
	private String neVersion;
	
	@Column(name = "DEVICE_TYPE")
	private String deviceType;

	public Long getElementID() {
		return elementID;
	}

	public void setElementID(Long elementID) {
		this.elementID = elementID;
	}

	public String getElementUserLabel() {
		return elementUserLabel;
	}

	public void setElementUserLabel(String elementUserLabel) {
		this.elementUserLabel = elementUserLabel;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getUniverse() {
		return universe;
	}

	public void setUniverse(String universe) {
		this.universe = universe;
	}

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	public String getNeVersion() {
		return neVersion;
	}

	public void setNeVersion(String neVersion) {
		this.neVersion = neVersion;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	/*@JsonIgnore
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, mappedBy = "element")
	private List<SubElement> subElementList;*/

	

}
