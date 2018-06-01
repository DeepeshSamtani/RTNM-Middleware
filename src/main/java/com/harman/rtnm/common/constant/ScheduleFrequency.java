package com.harman.rtnm.common.constant;

public enum ScheduleFrequency {

	HOURS_24("24 Hours"), HOURS_12("12 Hours"), HOURS_6("6 Hours") ,HOURS_4("4 Hours");
	private final String value;

	private ScheduleFrequency(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static ScheduleFrequency getEnum(String value) {
		for (ScheduleFrequency v : values())
			if (v.getValue().equals(value))
				return v;
		throw new IllegalArgumentException();
	}
}
