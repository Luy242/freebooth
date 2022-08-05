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

/**
 *
 * @author johannes
 */
public class MailConfig {
    
    private String provider;
    private String username;
    private String password;
    private String mail;
    private String message;
    private String subject;

    private String encryption;
    private String host;
    private int port;
    private boolean sendWatermarked;
    
    public MailConfig(String provider, String username, String password, String mail, String message, String subject, String encryption, String host, int port, boolean sendWatermarked) {
        this.provider = provider;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.message = message;
        this.subject = subject;
        this.encryption = encryption;
        this.host = host;
        this.port = port;
        this.sendWatermarked = sendWatermarked;
    }

    public String getProvider() {
        return provider;
    }
    
    public boolean sendWatermarked() {
        return sendWatermarked;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMail() {
        return mail;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }
    
    public String getEncryption(){
        return this.encryption;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
    
    
    
            
    
}
