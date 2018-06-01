package com.harman.rtnm.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harman.rtnm.service.ReportDownloadService;
import com.harman.rtnm.vo.ExportReportVO;
import com.harman.rtnm.vo.ReportDetailVO;



@RequestMapping(value = "/api/exportreport")
@RestController
public class ExportReportController {

	@Autowired
	ReportDownloadService reportDownloadService;
	
	
	@RequestMapping(value = "/downloadreport", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<byte[]>  downloadReport(@RequestBody ReportDetailVO reportDetailVO) throws Exception {
		ExportReportVO exportReportVO=new ExportReportVO();
		exportReportVO.setDownloadURL(reportDownloadService.exportReport(reportDetailVO)); 
		FileInputStream  fileStream = new FileInputStream(new File(exportReportVO.getDownloadURL()));
		byte[] contents = IOUtils.toByteArray(fileStream);
		String filename = null ;
		HttpHeaders headers = new HttpHeaders();
		
		if (exportReportVO.getDownloadURL().contains(".csv")) {
		
			headers.setContentType(MediaType.parseMediaType("application/csv"));
			filename = exportReportVO.getDownloadURL().substring(exportReportVO.getDownloadURL().lastIndexOf("/")+1, exportReportVO.getDownloadURL().length());
		} else if (exportReportVO.getDownloadURL().contains(".pdf"))  {
		
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			filename = exportReportVO.getDownloadURL().substring(exportReportVO.getDownloadURL().lastIndexOf("/")+1, exportReportVO.getDownloadURL().length());
		} else {
	
			headers.setContentType(MediaType.parseMediaType("application/xls"));
			filename = exportReportVO.getDownloadURL().substring(exportReportVO.getDownloadURL().lastIndexOf("/")+1, exportReportVO.getDownloadURL().length());
		}
	
		 headers.setContentDispositionFormData(filename, filename);
	 	 headers.set("Export-ReportName", filename);
	 		 
		
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	
		return response;
	};
	
}
