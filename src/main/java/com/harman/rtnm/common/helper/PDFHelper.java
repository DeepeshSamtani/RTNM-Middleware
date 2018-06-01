package com.harman.rtnm.common.helper;

import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;


public class PDFHelper {
	/**
	 * 
	 * @param table
	 * @param header
	 * @param font
	 */
	public static void pdfHeaderWriter(PdfPTable table, String[] header, Font font) {
		if (null != header && header.length > 0) {
			//insertCell(table, header, Element.ALIGN_LEFT, 1, font);
			//Font font = new Font(bf, size, style, color)
			font = new Font(FontFamily.TIMES_ROMAN, 4, 1, BaseColor.WHITE);
			for (String pdfData : header) {
				PdfPCell cell = new PdfPCell(new Phrase(pdfData.trim(), font));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setColspan(1);
				cell.setBackgroundColor(new BaseColor(95, 90, 165)); //#5f5aa5 hexcode
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				
	            //cell.setBorder(Rectangle.NO_BORDER);
				if (pdfData.trim().equalsIgnoreCase("")) {
					cell.setMinimumHeight(10f);					
				}
				table.addCell(cell);
			}
		}
	}

	/**
	 * 
	 * @param table
	 * @param text
	 * @param align
	 * @param colspan
	 * @param font
	 */
	private static void insertCell(PdfPTable table, String[] pdfContent, int align, int colspan, Font font) {

		for (String pdfData : pdfContent) {
			PdfPCell cell = new PdfPCell(new Phrase(pdfData.trim(), font));
			cell.setHorizontalAlignment(align);
			cell.setColspan(colspan);
			if (pdfData.trim().equalsIgnoreCase("")) {
				cell.setMinimumHeight(10f);
			}
			table.addCell(cell);
		}
	}

	/**
	 * 
	 * @param table
	 * @param reportdatalist
	 * @param font
	 * @param document
	 * @param paragraph
	 * @throws Exception
	 */
	public static void pdfContentWriter(PdfPTable table, List<String[]> reportdatalist, Font font, Document document,
			Paragraph paragraph) throws Exception {
		if (null != reportdatalist && !reportdatalist.isEmpty()) {
			for (String[] pdfData : reportdatalist) {
				insertCell(table, pdfData, Element.ALIGN_LEFT, 1, font);
			}
			paragraph.add(table);
			document.add(paragraph);
			document.close();
		}
	}

}
