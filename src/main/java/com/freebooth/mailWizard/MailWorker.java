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
package com.freebooth.mailWizard;

import com.freebooth.mailWizard.MailConfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import javax.mail.MessagingException;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

/**
 *
 * @author johannes
 */
public class MailWorker extends SwingWorker<Integer, String> {

    List<String> mails;
    List<String> images;
    JTextArea messagesTextArea;
    MailConfig mailconfig;
    String path;

    public MailWorker(final List<String> mails, final List<String> images, final String path, final MailConfig mailconfig, final JTextArea messagesTextArea) {
        this.mails = mails;
        this.images = images;
        this.messagesTextArea = messagesTextArea;
        this.mailconfig = mailconfig;
        this.path = path;

    }

    @Override
    protected Integer doInBackground() throws Exception {
        // Create the attachment
        publish(java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.Worker.start"));

        MailConfigurator configurator = new MailConfigurator();
        System.out.println("Test");
        String sobFilePath = path + "mails.soph";
        String sobFiletmpPath = path + "mails.tmp";
        String line = "";
        String cvsSplitBy = ",";
        
        for (int i = 0; i < mails.size(); i++) {
            System.out.println("Test");

            try {
                configurator.conifgureMail(mailconfig, path, images.get(i), mails.get(i), true);
                configurator.sendMessage();
                int x = i+1;
                publish("(" + x + "/" + mails.size() + ")" + java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.Worker.msg") + " " + mails.get(i));
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(sobFiletmpPath, true)));
                if (new File(sobFilePath).exists()) {
                    BufferedReader br = new BufferedReader(new FileReader(sobFilePath));
                    while ((line = br.readLine()) != null) {

                        String[] columns = line.split(cvsSplitBy);
                        if (columns[0].equals(images.get(i)) && columns[1].equals(mails.get(i))) {
                            out.println(images.get(i) + "," + mails.get(i) + "," + columns[2] + "," + "1");
                        } else {
                            out.println(line);
                        }

                    }
                    out.close();
                    File oldFile = new File(sobFilePath);

                    // File (or directory) with new name
                    File newFile = new File(sobFiletmpPath);
                    oldFile.delete();
                    newFile.renameTo(oldFile);
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
                String msg = ex.getMessage();
                msg += "\n";
                publish(msg);
            }
        }
        publish(java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.Worker.done"));
        return 0;

    }

    @Override
    protected void process(List<String> chunks) {
        for (final String string : chunks) {
            messagesTextArea.append(string);
            messagesTextArea.append("\n");
        }
    }

}
