package br.com.roboimpressaonfe.principal;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class ImgPrinter implements Printable {

	Image img;

	public ImgPrinter(Image img) {
		this.img = img;
	}

	public int print(Graphics pg, PageFormat pf, int pageNum) {
		if (pageNum != 0) {
			return Printable.NO_SUCH_PAGE;
		}
		Graphics2D g2 = (Graphics2D) pg;
		g2.translate(pf.getImageableX(), pf.getImageableY());
		g2.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
		return Printable.PAGE_EXISTS;
	}

	public void printPage() throws PrinterException {
		PrinterJob job = PrinterJob.getPrinterJob();
		boolean ok = job.printDialog();
		if (ok) {
			job.setJobName("TEST JOB");
			job.setPrintable(this);
			job.print();
		}
	}
}