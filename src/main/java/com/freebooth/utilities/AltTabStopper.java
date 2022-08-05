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

import java.awt.Robot;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

/**
 *
 * @author johannes
 */
public class AltTabStopper implements Runnable
{
     private boolean working = true;
     private JFrame frame;

     public AltTabStopper(JFrame frame)
     {
          this.frame = frame;
     }

     public void stop()
     {
          working = false;
     }

     public static AltTabStopper create(JFrame frame)
     {
         AltTabStopper stopper = new AltTabStopper(frame);
         new Thread(stopper, "Alt-Tab Stopper").start();
         return stopper;
     }

     public void run()
     {
         try
         {
             Robot robot = new Robot();
             while (working)
             {
                  robot.keyRelease(KeyEvent.VK_ALT);
                  robot.keyRelease(KeyEvent.VK_TAB);
                  frame.requestFocus();
                  try {
                      Thread.sleep(10); 
                  } catch(Exception  e) {}
             }
         } catch (Exception e) { e.printStackTrace(); System.exit(-1); }
     }
}