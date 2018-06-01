package com.harman.rtnm.common.constant;

public enum FilterValueEnum {

	OR_CONDITION("OR"),AND_CONDITION("AND"), FILTER_TYPE_JAVASCRIPT("javascript"),FILTER_VALUE_JAVASCRIPT("function(x) { return(x >-999999) }")
	,FILTER_TYPE_SELECTOR("selector"), DIMENSION_NODENAME("NodeName"), FILTER_TYPE_NOT("not"), FILTER_TYPE_BOUND("bound")
	;

	private final String value;

	private FilterValueEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static FilterValueEnum getEnum(String value) {
		for (FilterValueEnum v : values())
			if (v.getValue().equals(value))
				return v;
		throw new IllegalArgumentException();
	}

}