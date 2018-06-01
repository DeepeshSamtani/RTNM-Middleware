package com.harman.rtnm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.harman.rtnm.model.response.ExportDetails;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.harman.rtnm.common.helper.DateHelper;
import com.harman.rtnm.common.helper.PDFHelper;



public class ReportPDFWriter {

	public static final String CSS = " tr { text-align: left;  } th { background-color: #5f5aa5; font-size:8pt; padding:5px; color:white } td {padding: 5px; font-size:6pt }";

	public static final String EXTENSION = ".pdf";
	public String PRESCRIPTION_URL = "template.xsl";

	private static ReportPDFWriter instance;

	private ReportPDFWriter() {

	}

	public static ReportPDFWriter getInstance() {
		if (null == instance) {
			synchronized (ReportPDFWriter.class) {
				if (null == instance) {
					instance = new ReportPDFWriter();
				}
			}
		}
		return instance;
	}

	/**
	 * 
	 * @param filename
	 * @param reportlist
	 * @throws Exception
	 */
	public ByteArrayOutputStream createPDFContent(ExportDetails exportDetails, Document document) throws Exception {

		String filePath = exportDetails.getFilePath();
		List<String> fieldList = exportDetails.getFileHeaderList();
		List<String[]> pdfContent = exportDetails.getFileContent();

		ByteArrayOutputStream pdfoutStream = new ByteArrayOutputStream();
		OutputStream file = new FileOutputStream(new File(filePath));

		PdfWriter.getInstance(document, file);

		document.open();
		Font bf12 = new Font(FontFamily.TIMES_ROMAN, 4);
		document.setPageSize(PageSize.A4);

		String logo_path = null;
		//logo_path = "C:/Users/611205238/work/codebase/dyns_frontend/src/assets/images/bt-logo.png";
		if (null != logo_path && !logo_path.isEmpty()) {
			Image img = Image.getInstance(logo_path);
			img.scaleToFit(50f, 50f);
			img.setAlignment(img.ALIGN_RIGHT);
			// img.setAbsolutePosition(50, 50);
			document.add(img);
		}

		Font f = new Font(FontFamily.TIMES_ROMAN, 7);
		Paragraph paragraph = new Paragraph("Report Details : ", f);
		paragraph.add(Chunk.NEWLINE);
		List<String> reportDetail = new ArrayList<>();
		reportDetail.add(null != exportDetails.getFilename() ? "Report Name : " + exportDetails.getFilename() : null);
		reportDetail.add(null != exportDetails.getUserReport().getUserName()
				? "User Name : " + exportDetails.getUserReport().getUserName() : null);
		reportDetail
				.add(null != DateHelper.getDateTimeFormatFromMillisec(exportDetails.getReportcreationTime())
						? "Report Creation Date : "
								+ DateHelper.getDateTimeFormatFromMillisec(exportDetails.getReportcreationTime())
						: null);
		for (String reportData : reportDetail) {
			paragraph.add(new Chunk(reportData));
			paragraph.add(Chunk.NEWLINE);
		}

		// paragraph.getKeepTogether();
		// paragraph.setIndentationLeft(10);
		// paragraph.setAlignment(paragraph.ALIGN_MIDDLE);
		paragraph.add(Chunk.NEWLINE);
		paragraph.setAlignment(paragraph.ALIGN_LEFT);
		document.add(paragraph);
		Paragraph para2 = new Paragraph();

		StringBuilder buildheaderstring = new StringBuilder();
		fieldList.forEach((headerName) -> {
			buildheaderstring.append(headerName).append(",");
		});

		String[] headerArray = buildheaderstring.deleteCharAt(buildheaderstring.length() - 1).toString().split(",");
		PdfPTable table = new PdfPTable(headerArray.length);
		PDFHelper.pdfHeaderWriter(table, headerArray, bf12);
		PDFHelper.pdfContentWriter(table, pdfContent, bf12, document, para2);
		return pdfoutStream;
	}

	public void createFinalPDF(ExportDetails exportDetails, Document document) throws Exception {

		List<String> fieldList = exportDetails.getFileHeaderList();
		List<String[]> pdfContent = exportDetails.getFileContent();

		List<String> reportDetails = new ArrayList<>();
		reportDetails.add(null != exportDetails.getFilename() ? "Report Name : " + exportDetails.getFilename() : null);
		reportDetails.add(null != exportDetails.getUserReport().getUserName()
				? "User Name : " + exportDetails.getUserReport().getUserName() : null);
		reportDetails
				.add(null != DateHelper.getDateTimeFormatFromMillisec(exportDetails.getReportcreationTime())
						? "Report Creation Date : "
								+ DateHelper.getDateTimeFormatFromMillisec(exportDetails.getReportcreationTime())
						: null);
		String BtLogoPath = "";

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(exportDetails.getFilePath()));
		document.open();

		StringBuilder sb = new StringBuilder();
		sb.append("<div style=\"width:100%\">");
		sb.append("<div style=\"position:relative; left:615px;!important;\">");
		sb.append("<img width=\"82\" height=\"38\" src=\"");
		sb.append(BtLogoPath);
		sb.append("\"></img>");
		sb.append("</div>");
		sb.append("<div>");
		for (String reportDetail : reportDetails) {
			if (null != reportDetail) {
				sb.append("<label style=\"font-size:12px; font-family:Arial;\">" + reportDetail);
				sb.append("</label><br/>");
			}
		}
		sb.append("</div>");
		sb.append("<br/>");
		sb.append("<div>");
		sb.append("<div>");
		sb.append(
				"<table cellspadding=\"0\" cellspacing=\"0\" style=\"max-height: 220px; width:100%; border-color:#ccc; border:1px\" >");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th>");

		for (int i = 0; i < fieldList.size(); i++) {
			sb.append("<label>" + fieldList.get(i) + "</label>");
			sb.append("</th><th>");
		}
		sb.append("</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");

		// need to change
		for (int i = 0; i < pdfContent.size(); i++) {
			String[] result = pdfContent.get(i);
			if (i % 2 == 0) {
				sb.append("<tr style=\"background:#eeeeee;\">");
				for (int j = 0; j < result.length; j++) {
					sb.append("<td>");
					sb.append(result[j]).append("</td>");
				}
				sb.append("</tr>");
			} else if (i % 2 != 0) {
				sb.append("<tr>");
				for (int j = 0; j < result.length; j++) {
					sb.append("<td>");
					sb.append(result[j]).append("</td>");
				}
				sb.append("</tr>");
			}
		}
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");

		CSSResolver cssResolver = new StyleAttrCSSResolver();
		CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream(CSS.getBytes()));
		cssResolver.addCss(cssFile);
		// HTML
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		// Pipelines
		PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
		HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
		CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
		// XML Worker
		XMLWorker worker = new XMLWorker(css, true);
		XMLParser p = new XMLParser(worker);
		p.parse(new ByteArrayInputStream(sb.toString().getBytes()));

		document.close();
	}
}
