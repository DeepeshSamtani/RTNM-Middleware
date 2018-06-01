package com.harman.rtnm.model.response;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class ExportDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String filename;
	private List<String> fileHeaderList; 
	private UserReport userReport;
	private byte result[];
	private String reportType;
	private List<String[]> fileContent;
	private String filePath;
	private String filePathURL;	
	private boolean reportFlag;
	private String reportCreationTime;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public List<String> getFileHeaderList() {
		return fileHeaderList;
	}
	public void setFileHeaderList(List<String> fileHeaderList) {
		this.fileHeaderList = fileHeaderList;
	}
	public UserReport getUserReport() {
		return userReport;
	}
	public void setUserReport(UserReport userReport) {
		this.userReport = userReport;
	}
	public byte[] getResult() {
		return result;
	}
	public void setResult(byte[] result) {
		this.result = result;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public List<String[]> getFileContent() {
		return fileContent;
	}
	public void setFileContent(List<String[]> fileContent) {
		this.fileContent = fileContent;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFilePathURL() {
		return filePathURL;
	}
	public void setFilePathURL(String filePathURL) {
		this.filePathURL = filePathURL;
	}	
	public boolean getReportFlag() {
		return reportFlag;
	}
	public void setReportFlag(boolean reportFlag) {
		this.reportFlag = reportFlag;
	}	
	public String getReportcreationTime() {
		return reportCreationTime;
	}
	public void setReportcreationTime(String reportCreationTime) {
		this.reportCreationTime = reportCreationTime;
	}
	@Override
	public String toString() {
		return "ExportDetails [filename=" + filename + ", fileHeaderList=" + fileHeaderList + ", userReport="
				+ userReport + ", result=" + Arrays.toString(result) + ", reportType=" + reportType + ", fileContent="
				+ fileContent + ", filePath=" + filePath + ", filePathURL=" + filePathURL + ", reportFlag=" + reportFlag
				+ ", reportcreationTime=" + reportCreationTime + "]";
	}
}
