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

import com.sun.mail.util.MailSSLSocketFactory;
import java.io.File;
import java.security.GeneralSecurityException;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;

import jakarta.mail.*;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeMessage;

/**
 *
 * @author johannes
 */
public class MailConfigurator {

    private Properties prop;
    private Session session;
    private MimeMessage message;

    public void conifgureMail(MailConfig config, String filePath, String fileName, String to,boolean withAttachment) throws EmailException, MessagingException {
        // Create the attachment
        //System.setProperty("javax.net.ssl.trustStore","clientTrustStore.key");
        EmailAttachment attachment = new EmailAttachment();
        if(withAttachment){
            
        }
        // Create the email message
        boolean tls = false;
        boolean starttls = false;
        boolean ssl = false;

        prop = new Properties();
        if (config.getProvider().equals("GMail")) {
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

            //tls = true;
            starttls = true;
            
        } else if (config.getProvider().equals("web.de")) {
            prop.put("mail.smtp.host", "smtp.web.de");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.ssl.trust", "smtp.web.de");
            starttls = true;
        } else if (config.getProvider().equals("GMX")) {
            prop.put("mail.smtp.host", "mail.gmx.net");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.ssl.trust", "mail.gmx.net");
            
            starttls = true;
            //ssl = true;

        } else if (config.getProvider().equals("Outlook.com")) {
            prop.put("mail.smtp.host", "smtp-mail.outlook.com");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.ssl.trust", "smtp-mail.outlook.com");
            
            starttls = true;

        } else if (config.getProvider().equals("Yahoo!")) {
            prop.put("mail.smtp.host", "smtp.mail.yahoo.com");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.ssl.trust", "smtp.mail.yahoo.com");
//            email.setSmtpPort(587);
//            email.setAuthenticator(new DefaultAuthenticator(config.getMail(), config.getPassword()));
//            email.setHostName("smtp.mail.yahoo.com");
            starttls = true;

        } else {
            prop.put("mail.smtp.host", config.getHost());
            prop.put("mail.smtp.port", config.getPort());
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.ssl.protocols","TLSv1.2");

            if (config.getEncryption().equals("SSL")) {
                ssl = true;
            } else if (config.getEncryption().equals("TLS")) {
                tls = true;
            } else {
                prop.put("mail.smtp.ssl.trust", "*");

                starttls = true;
            }

        }
        
        session = Session.getInstance(prop,
        new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        });
        
        
        if (starttls) {
            prop.put("mail.smtp.starttls.enable", "true"); //TLS

        }
        if (ssl) {
            prop.put("mail.smtp.ssl.enable", "true");
            MailSSLSocketFactory sf;
            try {
                sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
                prop.put("mail.smtp.ssl.checkserveridentity", "true");
                prop.put("mail.smtp.ssl.socketFactory", sf);
            } catch (GeneralSecurityException ex) {
                Logger.getLogger(MailConfigurator.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

        // Create the message part
         BodyPart messageBodyPart = new MimeBodyPart();

         // Now set the actual message
         messageBodyPart.setText(config.getMessage() + "\n -- \n"+java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.Worker.signatory"));

         // Create a multipar message
         Multipart multipart = new MimeMultipart();

         // Set text message part
         multipart.addBodyPart(messageBodyPart);
         
        // add the attachment
        if(withAttachment){
            messageBodyPart = new MimeBodyPart();
            String path = filePath;
            if(config.sendWatermarked()) {
                path = filePath + "watermarked" + File.separator;
            }
            String fileNameToAttach;
            if(new File(path + fileName).exists()){
                fileNameToAttach = path + fileName;        
            } else {
                fileNameToAttach = filePath + fileName;        

            }
            DataSource source = new FileDataSource(fileNameToAttach);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);
        }
       
        message = new MimeMessage(session);

        // Send the complete message parts
        message.setContent(multipart);
        message.setFrom(new InternetAddress(config.getMail()));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(to)
        );
        message.setSubject(config.getSubject());


              
    }
    
    public void sendMessage() throws MessagingException {
        Transport.send(message);
    }
}
