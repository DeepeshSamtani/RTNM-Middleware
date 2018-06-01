package com.harman.rtnm.common.constant;

public enum FileType {
	
	CSV("csv"), XLS("xls"),PDF("pdf");
	private final String value;

	private FileType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static FileType getEnum(String value) {
		for (FileType v : values())
			if (v.getValue().equals(value))
				return v;
		throw new IllegalArgumentException();
	}

}
