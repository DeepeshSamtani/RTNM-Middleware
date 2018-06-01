package com.harman.rtnm.model.response;

public class KafkaJsonPojo {
	private String type;
		
	private String name;
	
	private String fieldName;
	
	private String expression;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}



	@Override
	public String toString() {
		return "KafkaJsonPojo [type=" + type + ", name=" + name + ", fieldName="
				+ fieldName + ", expression=" + expression + "]";
	}

	
	
}
