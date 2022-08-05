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
import java.util.List;
import javax.mail.MessagingException;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;


/**
 *
 * @author johannes
 */
public class MailWorkerWithoutAttachment extends SwingWorker<Integer, String>  {

    List<String> mails;

    JTextArea messagesTextArea;
    MailConfig mailconfig;

    
    public MailWorkerWithoutAttachment(final List<String> mails, final MailConfig mailconfig, final JTextArea messagesTextArea) {
        this.mails = mails;

        this.messagesTextArea = messagesTextArea;
        this.mailconfig = mailconfig;

        
    }

    @Override
    protected Integer doInBackground() throws Exception {
        // Create the attachment
        publish(java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.Worker.start"));
        
        MailConfigurator configurator = new MailConfigurator();
        System.out.println("Test");
        for(int i = 0; i < mails.size(); i++){
           System.out.println(mails.get(i));

            try {
                configurator.conifgureMail(mailconfig, null, null, mails.get(i),false);
                configurator.sendMessage();
                publish(java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.Worker.msg") + " " + mails.get(i));

            } catch (Exception ex) {
                String msg = ex.getMessage();
                msg += "\n";
                publish(msg);
            }
        }
       
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
