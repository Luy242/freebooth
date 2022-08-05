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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import net.coobird.thumbnailator.Thumbnails;
import com.freebooth.photobooth.MultiSelectPhotoboothFrame;
import com.freebooth.photobooth.StartFrame;
import com.freebooth.utilities.PathCreator;

/**
 *
 * @author johannes
 */
public class ThumbDecorator extends ImageDecorator {
    
    private boolean slideshow;

    public ThumbDecorator(boolean forServer) {
        this.slideshow = forServer;
    }

    public ThumbDecorator(boolean forServer, ImageDecorator decorator) {
        super(decorator);
        this.slideshow = forServer;
    }

    @Override
    public File processImage(File file, BufferedImage image) {
        Preferences prefs = Preferences.userNodeForPackage(StartFrame.class);
        if (prefs.getBoolean("thumbnails", true) || slideshow) {
            File thumbnail;
            if(slideshow) {
                thumbnail = new File(new PathCreator().getThumbWebPath() + file.getName());
            } else {
                thumbnail = new File(new PathCreator().getThumbPath() + file.getName());
            }
            BufferedImage outputImage = null;
            if (!thumbnail.exists()) {
                try {
                    int height = prefs.getInt("image_height", 800);
                    int width = prefs.getInt("image_width", 1400);
                    if (image != null) {
                        Thumbnails.of(image).size(width,height).toFile(thumbnail);
                    } else {
                        Thumbnails.of(file).size(width, height).toFile(thumbnail);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MultiSelectPhotoboothFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (this.decorator != null) {
                return this.decorator.processImage(thumbnail, outputImage);
            }
            return thumbnail;
        }
        return file;
    }

}
