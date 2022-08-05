/*
 * Copyright (C) 2018 johannes
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
package com.freebooth.fileWatcher;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import com.freebooth.photobooth.PhotoboothFrame;
import com.freebooth.photobooth.WatermarkPanel;
import com.freebooth.utilities.BILoader;
import com.freebooth.utilities.PathCreator;
import com.freebooth.utilities.WatermarkGenerator;

/**
 *
 * @author johannes
 */
public class WatermarkDecorator extends ImageDecorator {

    public WatermarkDecorator() {
    }

    public WatermarkDecorator(ImageDecorator decorator) {
        super(decorator);
    }
    
    

    @Override
    public File processImage(File file, BufferedImage image) {
        Preferences prefsWatermark = Preferences.userNodeForPackage(WatermarkPanel.class);
        String overlay = file.getName().split(Pattern.quote("."))[0];
        if (Boolean.parseBoolean(prefsWatermark.get("watermark", "false")) && !overlay.equals("overlay")) {
            File watermarkedFile = new File(new PathCreator().getWatermarkPath() + File.separator + file.getName());
            BufferedImage newImage = null;
            if (!watermarkedFile.exists()) {
                if (image == null) {
                    image = BILoader.loadImage(file.getAbsolutePath());
                }

                
                String position = prefsWatermark.get("watermark_position", "center");
                int opacity = prefsWatermark.getInt("watermark_opacity", 100);
                int marginX = prefsWatermark.getInt("watermark_margin_x", 0);
                int marginY = prefsWatermark.getInt("watermark_margin_y", 0);
                try {
                    if (prefsWatermark.get("watermark_type", "Text").equals("Text")) {
                        Font font = new Font(prefsWatermark.get("watermark_font_name", "Arial"), Integer.parseInt(prefsWatermark.get("watermark_font_style", Integer.toString(Font.PLAIN))), Integer.parseInt(prefsWatermark.get("watermark_font_size", "12")));
                        String text = prefsWatermark.get("watermark_text", "FreeBooth");
                        Color color = Color.decode(prefsWatermark.get("watermark_text_color", "#000000"));
                        newImage = WatermarkGenerator.addTextWatermark(text, opacity, color, font, position, image, marginX, marginY);
                    } else {

                        BufferedImage watermark = ImageIO.read(new File(prefsWatermark.get("watermark_path", prefsWatermark.get("watermark_path", new PathCreator().getWebPath() + "watermark.png"))));
                        int percent = prefsWatermark.getInt("watermark_percentage", 100);
                        newImage = WatermarkGenerator.watermark(image, watermark, position, opacity, marginX, marginY);

                    }
                    
                    ImageIO.write(newImage, "jpg", watermarkedFile);
                   
                } catch (IOException ex) {
                    Logger.getLogger(PhotoboothFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(decorator != null) {
                return decorator.processImage(watermarkedFile, newImage);
            }
            return watermarkedFile;    
        } 
        if(decorator != null) {
            return decorator.processImage(file, image);
        }
        return file;
    }
}
