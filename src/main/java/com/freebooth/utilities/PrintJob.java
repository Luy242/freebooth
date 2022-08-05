/*
 * Copyright (C) 2017 johannes
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package utilities;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author johannes
 */
public class PrintJob implements Printable {

    private static PrintJob instance;

    private PrinterJob job;
    
    private PageFormat pageFormat;

    private BufferedImage image;

    private String imagePath;

    public PrinterJob getJob() {
        return job;
    }

    public void setJob(PrinterJob job) {
        this.job = job;
    }

    public PageFormat getPageFormat() {
        return pageFormat;
    }

    public void setPageFormat(PageFormat pageFormat) {
        this.pageFormat = pageFormat;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void printImage(String imagePath) {
        this.imagePath = imagePath;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            job.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }

    private PrintJob() {
        job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
    }

    public Printable getOuter() {
        return this;
    }

    public void setUpPrinter() {
        job.printDialog();
    }

    public static PrintJob getInstance() {
        if (PrintJob.instance == null) {
            PrintJob.instance = new PrintJob();
        }
        return PrintJob.instance;
    }
    
    

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g = (Graphics2D) graphics;

        //g.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        if (image != null) {

            graphics.drawImage(image, 0, 0, (int) pageFormat.getWidth(), (int) pageFormat.getHeight(), 0, 0, image.getWidth(), image.getHeight(), null);
        }
        return PAGE_EXISTS;
    }

}
