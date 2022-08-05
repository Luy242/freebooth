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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * this is a helper class to read a file into a bufferedimage it is needed
 * becaus it is a bit complicated to read an image from a path given from the
 * hot folder watcher, because there is a race condition because the image is
 * maybe not ready copied
 *
 * @author johannes
 */
public class BILoader {

    public static BufferedImage loadImage(String path) {
        BufferedImage result = null;
        int breakCounter = 0;

        while (result == null) {
            if (breakCounter == 600) {
                break;
            }
            try {
                result = ImageIO.read(new File(path));
            } catch (Exception ex) {
               
            }
            if (result == null) {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BILoader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return result;
    }

}
