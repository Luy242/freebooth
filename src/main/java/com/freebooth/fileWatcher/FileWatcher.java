/* 
 * Copyright (C) 2016 Johannes Wilke
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

import com.freebooth.utilities.PathCreator;
import com.freebooth.fileWatcher.ImageDecorator;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.regex.Pattern;

/**
 *
 * @author johannes
 */
public abstract class FileWatcher implements Runnable {
    
    String lastImage;
    boolean run;
    ImageDecorator decorator = null;
    String pathToWatch;

    public FileWatcher(String pathToWatch) {
        run = true;
        lastImage = new String();
        this.pathToWatch = pathToWatch;
    }

    public void stopFileWatcher() {
        this.run = false;
    }
    
    public abstract void processReadImage(File image);
    public abstract void processDeletion(File image);
    

    @Override
    public void run() {
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();

            Path dir = Paths.get(pathToWatch);
            dir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

            System.out.println("Watch Service registered for dir: " + dir.toString());

            while (run) {

                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException ex) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    if (fileName.toString().contains(".")) {
                        System.out.println("new file tralala: " + kind + "-" + fileName);
                        String fileEnding = fileName.toString().substring(fileName.toString().lastIndexOf("."));
                        String overlay = fileName.toString().split(Pattern.quote("."))[0];

                        if ((fileEnding.equals(".jpg") || fileEnding.equals(".JPG")) && !lastImage.equals(fileName.toString()) && !overlay.equals("overlay")) {
                            if (kind == ENTRY_DELETE) {                       
                                processDeletion(new File(new PathCreator().getImagePath() + fileName));

                            } else {
                                File newImage = new File(new PathCreator().getImagePath() + fileName);
                                if(decorator != null) {
                                    newImage = decorator.processImage(newImage, null);
                                }

                                lastImage = fileName.toString();
                                processReadImage(newImage);
                              
                            }
                        }
                    }

                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    protected abstract void decorateStoredImages();

}
