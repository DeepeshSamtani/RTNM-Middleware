package com.harman.rtnm.model.response;


public class DatasourceJobPojo {
	private Object tuningConfig;

	private Object ioConfig;

	private String context;

	private DataSchema dataSchema;

	private String type;
	
	public String getContext() {
		return context;
	}

	public void setContext(Object object) {
		this.context = (String) object;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getTuningConfig() {
		return tuningConfig;
	}

	public void setTuningConfig(Object tuningConfig) {
		this.tuningConfig = tuningConfig;
	}

	public Object getIoConfig() {
		return ioConfig;
	}

	public void setIoConfig(Object ioConfig) {
		this.ioConfig = ioConfig;
	}

	public DataSchema getDataSchema() {
		return dataSchema;
	}

	public void setDataSchema(DataSchema dataSchema) {
		this.dataSchema = dataSchema;
	}

	public Object getType() {
		return type;
	}

	public void setType(Object object) {
		this.type =  (String) object;
	}

	@Override
	public String toString() {
		return "ClassPojo [tuningConfig = " + tuningConfig + ", ioConfig = " + ioConfig + ", context = " + context
				+ ", dataSchema = " + dataSchema + ", type = " + type + "]";
	}
}
