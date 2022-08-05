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
package com.freebooth.server;

import com.freebooth.fileWatcher.SlideshowFileWatcher;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to get for init all images
 * @author johannes
 */
public class GetAllImagesServlet extends HttpServlet {
   
    private SlideshowFileWatcher fw;

    public GetAllImagesServlet(SlideshowFileWatcher fw) {
        this.fw = fw;
    }

    

    
    
    
    
    
   
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setStatus(HttpServletResponse.SC_OK);
    
        List<String> images = fw.getAllImages();
        
        boolean first = true;
        for(String image: images){
            if(!first){
                response.getWriter().print(",");
            }
            response.getWriter().print(image);
            first = false;
        }
    
}
    
}
