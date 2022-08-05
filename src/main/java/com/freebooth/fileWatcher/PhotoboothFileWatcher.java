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

import java.io.File;
import com.freebooth.photobooth.Photobooth;

/**
 *
 * @author johannes
 */
public class PhotoboothFileWatcher  extends FileWatcher {
    
    Photobooth photobooth;

    public PhotoboothFileWatcher(String pathToWatch, Photobooth photobooth, boolean skipDecoration) {
        super(pathToWatch);
        this.photobooth = photobooth;
        if(!skipDecoration){
            this.decorator = new WatermarkDecorator(new ThumbDecorator(false));
        } else {
            this.decorator = null;
        }
    }
    
    
    @Override
    public void processReadImage(File image) {
        photobooth.addImage(image.getName());
    }

    @Override
    public void processDeletion(File image) {
        photobooth.deleteImage(image.getName());
    }

    @Override
    protected void decorateStoredImages() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
