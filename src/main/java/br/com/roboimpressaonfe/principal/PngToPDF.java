package br.com.roboimpressaonfe.principal;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.PngImage;

public class PngToPDF {

	public byte[] transformaImagensEmPDF(List<File> listaImagens, int mes, int ano, String caminhoPasta) {
		byte[] pdfFinal = null;
		try {
			Document convertPngToPdf = new Document();
			PdfWriter.getInstance(convertPngToPdf,
					new FileOutputStream(caminhoPasta + File.separator + "NOTAS-" + mes + "-" + ano + ".pdf"));
			convertPngToPdf.setMargins(50, 100, 65, 0);
			convertPngToPdf.open();
			for (File file : listaImagens) {
				Image convertBmp = PngImage.getImage(file.getAbsolutePath());
				convertBmp.scaleToFit(PageSize.A4.getWidth() - 50, PageSize.A4.getHeight() - 50);
				convertPngToPdf.add(convertBmp);
				if (file.exists()) {
					file.delete();
				}
			}
			convertPngToPdf.close();
		} catch (Exception i1) {
			i1.printStackTrace();
		}
		return pdfFinal;
	}
}