package com.harman.rtnm.vo;

import java.io.Serializable;

public class ExportReportVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1032926497491291997L;
	
	private String downloadURL;

	public String getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	@Override
	public String toString() {
		return "ExportReportVO [downloadURL=" + downloadURL + "]";
	}
	

}
