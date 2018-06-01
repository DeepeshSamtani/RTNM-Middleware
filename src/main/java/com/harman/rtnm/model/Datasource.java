package com.harman.rtnm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "DATASOURCE")
@Entity
public class Datasource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6348765883277823008L;

	@Id
	@Column(name = "Datasource_ID")
	private int datasourceID;
	
	@Column(name = "DATASOURCE")
	private String datasource;
	
	@Column(name = "GRANULARITY")
	private String granularity;

	@Column(name = "DEVICE_TYPE")
	private String deviceType;

	public int getDatasourceID() {
		return datasourceID;
	}

	public void setDatasourceID(int datasourceID) {
		this.datasourceID = datasourceID;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getGranularity() {
		return granularity;
	}

	public void setGranularity(String granularity) {
		this.granularity = granularity;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	@Override
	public String toString() {
		return "Datasource [datasourceID=" + datasourceID + ", datasource=" + datasource + ", granularity="
				+ granularity + ", deviceType=" + deviceType + "]";
	}

	
}
