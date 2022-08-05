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
import java.util.ArrayList;
import java.util.List;
import com.freebooth.utilities.BILoader;
import com.freebooth.utilities.FileExistenceChecker;
import com.freebooth.utilities.PathCreator;

/**
 *
 * @author johannes
 */
public class SlideshowFileWatcher extends FileWatcher {
    
    private List<String> images;

    public SlideshowFileWatcher() {
        super(new PathCreator().getImagePath());
        images = new ArrayList<>();
        this.decorator = new WatermarkDecorator(new ThumbDecorator(true));
        this.decorateStoredImages();
    }

    @Override
    public void processReadImage(File image) {
        images.add(image.getName());
    }

    @Override
    public void processDeletion(File image) {
       
    }
    
    public String getLastImage() {
        if (!images.isEmpty()) {
            return this.images.get(images.size() - 1);
        }
        return new String("");
    }

    public List<String> getAllImages() {
        return this.images;
    }

    @Override
    protected void decorateStoredImages() {
        File folder = new File(new PathCreator().getImagePath());
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (!file.isDirectory()) {
                String fileName = file.getName();
                String fileEnding = fileName.substring(fileName.lastIndexOf("."));
                boolean allFilesExist = FileExistenceChecker.webThumbExists(fileName) && FileExistenceChecker.watermarkExists(fileName);
                File decoratedFile;
                if (file.isFile() && (fileEnding.equals(".jpg") || fileEnding.equals(".JPG"))) {
                    if (allFilesExist) {
                        decoratedFile = decorator.processImage(file, null);
                    } else {
                        BufferedImage image = BILoader.loadImage(new PathCreator().getImagePath() + File.separator + file.getName());
                        decoratedFile = decorator.processImage(file, image);
                    }
                    images.add(decoratedFile.getName());
                }
            }
        }
    }
    
}
