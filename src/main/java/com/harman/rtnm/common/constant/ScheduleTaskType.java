package com.harman.rtnm.common.constant;

public enum ScheduleTaskType {

	DAILY("daily"), WEEKLY("weekly"), MONTHLY("monthly");
	private final String value;

	private ScheduleTaskType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static ScheduleTaskType getEnum(String value) {
		for (ScheduleTaskType v : values())
			if (v.getValue().equals(value))
				return v;
		throw new IllegalArgumentException();
	}
}
