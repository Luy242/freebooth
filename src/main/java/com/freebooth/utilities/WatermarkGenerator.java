/*
 * Copyright (C) 2016 johannes
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
package com.freebooth.utilities;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.commons.lang3.tuple.Pair;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

/**
 *
 * @author johannes
 */
public class WatermarkGenerator {

    /**
     * Generate a watermarked image with an other image
     *
     * @param originalImage
     * @param watermarkImage
     * @param position
     * @param watermarkSizeMaxPercentage
     * @return image with watermark
     * @throws IOException
     */
    public static BufferedImage watermark(BufferedImage originalImage,
            BufferedImage watermarkImage, String position,
            double watermarkSizeMaxPercentage, int marginX, int marginY) throws IOException {

        int imageWidth = originalImage.getWidth();
        int imageHeight = originalImage.getHeight();

        int watermarkWidth = getWatermarkWidth(originalImage, watermarkImage,
                watermarkSizeMaxPercentage);
        int watermarkHeight = getWatermarkHeight(originalImage, watermarkImage,
                watermarkSizeMaxPercentage);

        // We create a new image because we want to keep the originalImage
        // object intact and not modify it.
        BufferedImage bufferedImage = new BufferedImage(imageWidth,
                imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int x = 0;
        int y = 0;
        
        marginX = (int)((marginX*originalImage.getWidth()) / 100);
        marginY = (int)((marginY*originalImage.getHeight()) / 100);
        if (position != null) {
            switch (position) {
                case "north_west":
                    x = 0 + marginX;
                    y = 0 + marginY;
                    break;
                case "north":
                    x = (imageWidth / 2) - (watermarkWidth / 2);
                    y = 0 + marginY;
                    break;
                case "north_east":
                    x = imageWidth - watermarkWidth  - marginX;
                    y = 0 + marginY;
                    break;

                case "west":
                    x = 0 + marginX;
                    y = (imageHeight / 2) - (watermarkHeight / 2);
                    break;
                case "center":
                    x = (imageWidth / 2) - (watermarkWidth / 2);
                    y = (imageHeight / 2) - (watermarkHeight / 2) ;
                    break;
                case "east":
                    x = imageWidth - watermarkWidth  - marginX;
                    y = (imageHeight / 2) - (watermarkHeight / 2);
                    break;

                case "south_west":
                    x = 0 + marginX;
                    y = imageHeight - watermarkHeight - marginY;
                    break;
                case "south":
                    x = (imageWidth / 2) - (watermarkWidth / 2);
                    y = imageHeight - watermarkHeight - marginY;
                    break;
                case "south_east":
                    x = imageWidth - watermarkWidth  - marginX;
                    y = imageHeight - watermarkHeight - marginY;
                    break;

                default:
                    break;
            }
        }

        g2d.drawImage(Scalr.resize(watermarkImage, Method.ULTRA_QUALITY,
                watermarkWidth, watermarkHeight), x, y, null);

        return bufferedImage;

    }

    /**this is a helper method to calculate Dimensions of an image
     *
     * @param originalImage
     * @param watermarkImage
     * @param maxPercentage
     * @return dimensions
     */
    private static Pair<Double, Double> calculateWatermarkDimensions(
            BufferedImage originalImage, BufferedImage watermarkImage,
            double maxPercentage) {

        double imageWidth = originalImage.getWidth();
        double imageHeight = originalImage.getHeight();

        double maxWatermarkWidth = imageWidth / 100.0 * maxPercentage;
        double maxWatermarkHeight = imageHeight / 100.0 * maxPercentage;

        double watermarkWidth = watermarkImage.getWidth();
        double watermarkHeight = watermarkImage.getHeight();

        if (watermarkWidth > maxWatermarkWidth) {
            double aspectRatio = watermarkWidth / watermarkHeight;
            watermarkWidth = maxWatermarkWidth;
            watermarkHeight = watermarkWidth / aspectRatio;
        }

        if (watermarkHeight > maxWatermarkHeight) {
            double aspectRatio = watermarkWidth / watermarkHeight;
            watermarkHeight = maxWatermarkHeight;
            watermarkWidth = watermarkHeight / aspectRatio;
        }

        return Pair.of(watermarkWidth, watermarkHeight);
    }

    /**
     * helper method, calculate watermark width
     * @param originalImage
     * @param watermarkImage
     * @param maxPercentage
     * @return
     */
    private static int getWatermarkWidth(BufferedImage originalImage,
            BufferedImage watermarkImage, double maxPercentage) {

        return calculateWatermarkDimensions(originalImage, watermarkImage,
                maxPercentage).getLeft().intValue();

    }

    /**
     * helper method to calculate watermark height
     * @param originalImage
     * @param watermarkImage
     * @param maxPercentage
     * @return
     */
    private static int getWatermarkHeight(BufferedImage originalImage,
            BufferedImage watermarkImage, double maxPercentage) {

        return calculateWatermarkDimensions(originalImage, watermarkImage,
                maxPercentage).getRight().intValue();

    }

    /**
     * Embeds a textual watermark over a source image to produce a watermarked
     * one.
     *
     * @param text The text to be embedded as watermark.
     * @param sourceImageFile The source image file.
     * @param destImageFile The output image file.
     */
    static public BufferedImage addTextWatermark(String text, int opacity, Color color, Font font, String position, BufferedImage sourceImage,int marginX, int marginY) {
        
        BufferedImage returnImage = sourceImage;
        //calculate the font scaling factor, to reach that the font has on every Picture resolution the same size
        double fontScaleFactor = 1;
        if(returnImage.getHeight() > returnImage.getWidth()){
            fontScaleFactor = returnImage.getWidth()/500.0;
        } else {
            fontScaleFactor = returnImage.getHeight()/500.0;
        }
        font = new Font(font.getName(), font.getStyle(),(int) (font.getSize()*fontScaleFactor));
        Graphics2D g2d = (Graphics2D) returnImage.getGraphics();
        
        // initializes necessary graphic properties
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity / 100f);
        g2d.setComposite(alphaChannel);
        g2d.setColor(color);
        g2d.setFont(font);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(text, g2d);
        int width = fontMetrics.stringWidth(text);

        // calculates the coordinate where the String is painted
        int centerX = 0;
        int centerY = 0;
       
        marginX = (int)((marginX*returnImage.getWidth()) / 100);
        marginY = (int)((marginY*returnImage.getHeight()) / 100);

        if (position != null) {
            switch (position) {
                case "north_west":
                    centerX =  0 + marginX;
                    centerY = (int) fontMetrics.getHeight() + marginY;
                    break;
                case "north":
                    centerX = (sourceImage.getWidth() - (int) rect.getWidth()) / 2;
                    centerY = (int) fontMetrics.getHeight() + marginY;
                    break;
                case "north_east":
                    centerX = (sourceImage.getWidth() - (int) rect.getWidth())- marginX;
                    centerY = (int) fontMetrics.getHeight() + marginY;
                    break;

                case "west":
                    centerX = 0 + marginX;
                    centerY = sourceImage.getHeight() / 2;
                    break;
                case "center":
                    centerX = (sourceImage.getWidth() - (int) rect.getWidth()) / 2;
                    centerY = sourceImage.getHeight() / 2;
                    break;
                case "east":
                    centerX = (sourceImage.getWidth() - (int) rect.getWidth())- marginX;
                    centerY = sourceImage.getHeight() / 2;
                    break;

                case "south_west":
                    centerX = 0 + marginX;
                    centerY = sourceImage.getHeight() - marginY;
                    break;
                case "south":
                    centerX = (sourceImage.getWidth() - (int) rect.getWidth()) / 2;
                    centerY = sourceImage.getHeight() - marginY;
                    break;
                case "south_east":
                    centerX = (sourceImage.getWidth() - (int) rect.getWidth())- marginX;
                    centerY = sourceImage.getHeight() - marginY;
                    break;

                default:
                    break;
            }
        }

        // paints the textual watermark
        g2d.drawString(text, centerX, centerY);

        g2d.dispose();

        System.out.println("The tex watermark is added to the image.");

        return returnImage;
    }

}
