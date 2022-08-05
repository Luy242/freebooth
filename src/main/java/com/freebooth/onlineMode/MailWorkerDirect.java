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
package com.freebooth.onlineMode;

import com.freebooth.mailWizard.MailConfig;
import com.freebooth.mailWizard.MailConfigurator;
import java.awt.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.mail.MessagingException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import com.freebooth.utilities.PathCreator;

/**
 *
 * @author johannes
 */
public class MailWorkerDirect extends SwingWorker<Integer, String> {

    List<String> mails;
    List<String> images;
    MailConfig mailconfig;
    String path;
    Component frame;

    public MailWorkerDirect(final List<String> mails, final List<String> images, final String path, final MailConfig mailconfig, JFrame frame) {
        this.mails = mails;
        this.images = images;;
        this.mailconfig = mailconfig;
        this.path = path;
        this.frame = frame;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        // Create the attachment

        MailConfigurator configurator = new MailConfigurator();
        System.out.println("Test");
        for (int i = 0; i < mails.size(); i++) {
            System.out.println("Test");

            String sophPath = new PathCreator().getImagePath() + "mails.soph";

            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(sophPath, true)));

            Date actDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            String dateFormat = formatter.format(actDate);


            try {
                configurator.conifgureMail(mailconfig, path, images.get(i), mails.get(i), true);
                configurator.sendMessage();
                out.println(images.get(i) + "," + mails.get(i) + "," + dateFormat + ",1");
                System.out.println("success");
            } catch (Exception ex) {
                String msg = ex.getMessage();
                msg += "\n";
                System.out.println(msg);
                JOptionPane.showMessageDialog(frame, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
                out.println(images.get(i) + "," + mails.get(i) + "," + dateFormat + ",0");
            }
            
            out.close();
        }
        publish(java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.Worker.done"));
        return 0;

    }

}
