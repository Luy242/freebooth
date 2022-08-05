/*
 * Copyright (C) 2016 Johannes
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
import org.apache.commons.lang3.SystemUtils;
import com.freebooth.photobooth.StartFrame;
import com.freebooth.photobooth.WatermarkPanel;

/**
 * this is a helper class to get the correct path to any location
 * @author Johannes
 */
public class PathCreator {

    public String getImagePath() {
        Preferences prefs = Preferences.userNodeForPackage(StartFrame.class);
        String path = prefs.get("hot_folder_path", null);
        if (path == null) {
            File f = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            String jarLocation = f.getParent() + File.separator + "images" + File.separator;
            jarLocation = jarLocation.replaceAll("%20", " ");

            if (!new File(jarLocation).exists()) {
                new File(jarLocation).mkdirs();
                System.out.println("path created");
            }
            path = jarLocation;
        } else {
            path = path + File.separator;
        }
        //System.out.println(path); 
        return path;
    }
    
    public String getThumbPath() {
        String path = getImagePath() + "thumbs" + File.separator;
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        return path;
    }

    public String getFullResPath() {
        Preferences prefsWatermark = Preferences.userNodeForPackage(WatermarkPanel.class);
        if(prefsWatermark.getBoolean("watermark",false)){
            return getWatermarkPath();
        } else {
            return getImagePath();
        }
    }

    public String getWatermarkPath() {
        String path = getImagePath() + File.separator + "watermarked" + File.separator;
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        return path;
    }

    public String getWebPath() {
        File f = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        String jarLocation = f.getParent() + File.separator;
        jarLocation = jarLocation.replaceAll("%20", " ");
        System.out.println(jarLocation);
        if (!new File(jarLocation).exists()) {
            new File(jarLocation).mkdirs();
        }
        return jarLocation;
    }

    public String getThumbWebPath() {
        String path = getWebPath() + "freebooth_web" + File.separator + "thumb" + File.separator;
        File f = new File(path);
        if(!f.exists()){
            f.mkdirs();
        }
        return path;
    }
}
