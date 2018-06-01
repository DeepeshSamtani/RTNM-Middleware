package com.harman.rtnm.model;

import java.util.List;


public class SeriesData {
	
	private String name;
	private String id;
	private List<List<Object>> data;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<List<Object>> getData() {
		return data;
	}
	public void setData(List<List<Object>> data) {
		this.data = data;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "SeriesData [name=" + name + ", id=" + id + ", data=" + data + "]";
	}
	
	
	
}
