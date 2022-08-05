/*
 * Copyright (C) 2021 Johannes
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

import java.io.File;
import java.util.prefs.Preferences;
import com.freebooth.photobooth.PhotoboothFrame;
import com.freebooth.photobooth.WatermarkPanel;

/**
 *
 * @author Johannes
 */
public class FileExistenceChecker {
        /**
     * checks whether the watermark already exists
     * @param filename
     * @return true when the file exists or the settings is turned off because in this case no creation is needed either
     */
    public static boolean watermarkExists(String filename) {
        Preferences prefsWatermark = Preferences.userNodeForPackage(WatermarkPanel.class);
        boolean setting = prefsWatermark.getBoolean("watermark", true);
        PathCreator pathCreator = new PathCreator();
        return !setting || new File(pathCreator.getWatermarkPath() + filename).exists();
    }
    
    /**
     * checks whether the thumbnail already exists
     * @param filename
     * @return true when the file exists or the settings is turned off because in this case no creation is needed either
     */
    public static boolean thumbExists(String filename) {
        Preferences prefs = Preferences.userNodeForPackage(PhotoboothFrame.class);
        boolean setting = prefs.getBoolean("thumbnails", true);
        PathCreator pathCreator = new PathCreator();
        return !setting || new File(pathCreator.getThumbPath()+ filename).exists();
    }
    
    public static boolean webThumbExists(String filename) {
        PathCreator pathCreator = new PathCreator();
        return new File(pathCreator.getThumbWebPath()+ filename).exists();
    }
}
