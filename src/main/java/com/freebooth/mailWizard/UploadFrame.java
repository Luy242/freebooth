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

import com.freebooth.utilities.PathCreator;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * this is the form of the mailwizard
 *
 * @author johannes
 */
public class UploadFrame extends javax.swing.JFrame {

    List<String> images;
    List<String> mails;
    List<String> imagesSent;
    List<String> mailsSent;
    String path;
    List<String> logEntries;

    /**
     * Creates new form UploadFrame
     */
    public UploadFrame() {
        images = new ArrayList<>();
        imagesSent = new ArrayList<>();
        mailsSent = new ArrayList<>();
        mails = new ArrayList<>();
        initComponents();
        jLabel2.setText("0 " + java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.jLabel2.text"));
        editProvider.addItem("-");
        editProvider.addItem("GMail");
        editProvider.addItem("GMX");
        editProvider.addItem("Outlook.com");
        //editProvider.addItem("Mail.com");
        editProvider.addItem("web.de");

        editProvider.addItem("Yahoo!");

        editProvider.addItem("other");

        editProvider.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (editProvider.getSelectedItem().toString().equals("other")) {
                    editHost.setEnabled(true);
                    editEncrypt.setEnabled(true);
                    editPort.setEnabled(true);
                } else {
                    editHost.setEnabled(false);
                    editEncrypt.setEnabled(false);
                    editPort.setEnabled(false);
                }
            }
        });

        logEntries = new ArrayList<>();

        jCheckBox1.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (jXDatePicker1 != null && jXDatePicker1.getDate() != null) {
                    Date date;

                    date = jXDatePicker1.getDate();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                    String dateFormat = formatter.format(date);

                    BufferedReader br = null;
                    String line = "";
                    String cvsSplitBy = ",";
                    String dateFormatAdded = "";
                    if (jCheckBox1.isSelected()) {

                        LocalDate parsedDate = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date));
                        LocalDate addedDate = parsedDate.plusDays(1);
                        dateFormatAdded = formatter.format(java.sql.Date.valueOf(addedDate));

                    }

                    try {
                        images.clear();
                        mails.clear();
                        String sobFilePath = path + "mails.soph";
                        br = new BufferedReader(new FileReader(sobFilePath));
                        while ((line = br.readLine()) != null) {

                            // use comma as separator
                            String[] columns = line.split(cvsSplitBy);
                            if (columns[2].equals(dateFormat)) {
                                if (columns[3].equals("0")) {
                                    images.add(columns[0]);
                                    mails.add(columns[1]);
                                } else {
                                    imagesSent.add(columns[0]);
                                    mailsSent.add(columns[1]);
                                }

                            }
                            if (jCheckBox1.isSelected()) {
                                if (columns[2].equals(dateFormatAdded)) {
                                    if (columns[3].equals("0")) {
                                        images.add(columns[0]);
                                        mails.add(columns[1]);
                                    } else {
                                        imagesSent.add(columns[0]);
                                        mailsSent.add(columns[1]);
                                    }
                                }
                            }

                        }

                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    jLabel2.setText(images.size()+mailsSent.size() + " " + java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.jLabel2.text") + " (" + mailsSent.size() + " " + java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.jLabel2.sent") + ")");
                    if (images.size() != 0) {
                        jButton1.setEnabled(true);
                        editProvider.setEnabled(true);
                        editPassword.setEnabled(true);
                        editSubject.setEnabled(true);
                        editTestmail.setEnabled(true);
                        editText.setEnabled(true);
                        editUsername.setEnabled(true);
                        buttonSend.setEnabled(true);
                        sendAllButton.setEnabled(true);
                        buttonTestmail.setEnabled(true);
                        editMailadress.setEnabled(true);
                    } else {
                        jButton1.setEnabled(false);
                        editProvider.setEnabled(false);
                        editPassword.setEnabled(false);
                        editSubject.setEnabled(false);
                        editTestmail.setEnabled(false);
                        editText.setEnabled(false);
                        editUsername.setEnabled(false);
                        buttonSend.setEnabled(false);
                        sendAllButton.setEnabled(false);
                        buttonTestmail.setEnabled(false);
                        editMailadress.setEnabled(false);
                    }

                }
            }

        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        dateComponentFormatter1 = new org.jdatepicker.impl.DateComponentFormatter();
        jDatePickerUtil1 = new org.jdatepicker.util.JDatePickerUtil();
        jLabel1 = new javax.swing.JLabel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        editProvider = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        editUsername = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        buttonTestmail = new javax.swing.JButton();
        buttonSend = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        editTestmail = new javax.swing.JTextField();
        editSubject = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        editText = new javax.swing.JTextArea();
        editPassword = new javax.swing.JPasswordField();
        jLabel9 = new javax.swing.JLabel();
        editMailadress = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        editHost = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        editPort = new javax.swing.JTextField();
        editEncrypt = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        pathLabel = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        sendAllButton = new javax.swing.JButton();
        sendWatermarked = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("properties/Bundle"); // NOI18N
        setTitle(bundle.getString("UploadFrame.title")); // NOI18N
        setSize(new java.awt.Dimension(500, 800));
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0};
        layout.rowHeights = new int[] {0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0, 10, 0};
        getContentPane().setLayout(layout);

        jLabel1.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        jLabel1.setText(bundle.getString("UploadFrame.jLabel1.text")); // NOI18N
        jLabel1.setToolTipText(bundle.getString("UploadFrame.jLabel1.toolTipText")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        jXDatePicker1.setEnabled(false);
        jXDatePicker1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jXDatePicker1PropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.ipadx = 363;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        getContentPane().add(jXDatePicker1, gridBagConstraints);

        jCheckBox1.setText(bundle.getString("UploadFrame.jCheckBox1.text")); // NOI18N
        jCheckBox1.setEnabled(false);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jCheckBox1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jCheckBox1PropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        getContentPane().add(jCheckBox1, gridBagConstraints);

        jLabel2.setText(bundle.getString("UploadFrame.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        getContentPane().add(jLabel2, gridBagConstraints);

        jButton1.setText(bundle.getString("UploadFrame.jButton1.text")); // NOI18N
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jButton1, gridBagConstraints);

        editProvider.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.ipadx = 363;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        getContentPane().add(editProvider, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        jLabel3.setText(bundle.getString("UploadFrame.jLabel3.text")); // NOI18N
        jLabel3.setToolTipText(bundle.getString("UploadFrame.jLabel3.toolTipText")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 0, 0);
        getContentPane().add(jLabel3, gridBagConstraints);

        editUsername.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.ipadx = 230;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(editUsername, gridBagConstraints);

        jLabel4.setText(bundle.getString("UploadFrame.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        getContentPane().add(jLabel4, gridBagConstraints);

        jLabel5.setText(bundle.getString("UploadFrame.jLabel5.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        getContentPane().add(jLabel5, gridBagConstraints);

        jLabel6.setText(bundle.getString("UploadFrame.jLabel6.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        getContentPane().add(jLabel6, gridBagConstraints);

        jLabel7.setText(bundle.getString("UploadFrame.jLabel7.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        getContentPane().add(jLabel7, gridBagConstraints);

        buttonTestmail.setText(bundle.getString("UploadFrame.buttonTestmail.text")); // NOI18N
        buttonTestmail.setEnabled(false);
        buttonTestmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTestmailActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 43;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 20, 0);
        getContentPane().add(buttonTestmail, gridBagConstraints);

        buttonSend.setText(bundle.getString("UploadFrame.buttonSend.text")); // NOI18N
        buttonSend.setEnabled(false);
        buttonSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSendActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 20, 0);
        getContentPane().add(buttonSend, gridBagConstraints);

        jLabel8.setText(bundle.getString("UploadFrame.jLabel8.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.ipadx = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 20, 0);
        getContentPane().add(jLabel8, gridBagConstraints);

        editTestmail.setToolTipText(bundle.getString("UploadFrame.editTestmail.toolTipText")); // NOI18N
        editTestmail.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 230;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 20, 0);
        getContentPane().add(editTestmail, gridBagConstraints);

        editSubject.setText(bundle.getString("UploadFrame.editSubject.text")); // NOI18N
        editSubject.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 230;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(editSubject, gridBagConstraints);

        editText.setColumns(20);
        editText.setRows(5);
        editText.setText(bundle.getString("UploadFrame.editText.text")); // NOI18N
        editText.setEnabled(false);
        jScrollPane2.setViewportView(editText);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 230;
        gridBagConstraints.ipady = 130;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(jScrollPane2, gridBagConstraints);

        editPassword.setToolTipText(bundle.getString("UploadFrame.editPassword.toolTipText")); // NOI18N
        editPassword.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 230;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(editPassword, gridBagConstraints);

        jLabel9.setText(bundle.getString("UploadFrame.jLabel9.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        getContentPane().add(jLabel9, gridBagConstraints);

        editMailadress.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 230;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(editMailadress, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 21));
        jScrollPane1.setRequestFocusEnabled(false);
        jScrollPane1.setVerifyInputWhenFocusTarget(false);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setTabSize(20);
        jScrollPane1.setViewportView(jTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 27;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 450;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 40);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jLabel10.setText(bundle.getString("UploadFrame.jLabel10.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        getContentPane().add(jLabel10, gridBagConstraints);

        jLabel11.setText(bundle.getString("UploadFrame.jLabel11.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        getContentPane().add(jLabel11, gridBagConstraints);

        editHost.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.ipadx = 150;
        getContentPane().add(editHost, gridBagConstraints);

        jLabel12.setText(bundle.getString("UploadFrame.jLabel12.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        getContentPane().add(jLabel12, gridBagConstraints);

        editPort.setEnabled(false);
        editPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPortActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(editPort, gridBagConstraints);

        editEncrypt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SSL", "TLS", "STARTTLS" }));
        editEncrypt.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 26;
        getContentPane().add(editEncrypt, gridBagConstraints);

        jButton2.setText(bundle.getString("UploadFrame.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        getContentPane().add(jButton2, gridBagConstraints);

        jLabel13.setText(bundle.getString("UploadFrame.jLabel13.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        getContentPane().add(jLabel13, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(pathLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 0;
        getContentPane().add(jLayeredPane1, gridBagConstraints);

        sendAllButton.setText(bundle.getString("UploadFrame.sendAllButton.text")); // NOI18N
        sendAllButton.setEnabled(false);
        sendAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendAllButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(sendAllButton, gridBagConstraints);

        sendWatermarked.setSelected(true);
        sendWatermarked.setText(bundle.getString("UploadFrame.sendWatermarked.text")); // NOI18N
        sendWatermarked.setActionCommand(bundle.getString("UploadFrame.sendWatermarked.actionCommand")); // NOI18N
        sendWatermarked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendWatermarkedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 20);
        getContentPane().add(sendWatermarked, gridBagConstraints);
        sendWatermarked.getAccessibleContext().setAccessibleName(bundle.getString("UploadFrame.sendWatermarked.AccessibleContext.accessibleName")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        MailsDialog md = new MailsDialog(this, false, images, mails);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jXDatePicker1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jXDatePicker1PropertyChange
        if (evt.getNewValue() != null) {
            Date date;
            if (evt.getNewValue() instanceof Date) {
                date = (Date) evt.getNewValue();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                String dateFormat = formatter.format(date);

                BufferedReader br = null;
                String line = "";
                String cvsSplitBy = ",";
                String dateFormatAdded = "";
                if (jCheckBox1.isSelected()) {
                    LocalDate parsedDate = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date));
                    LocalDate addedDate = parsedDate.plusDays(1);
                    dateFormatAdded = formatter.format(java.sql.Date.valueOf(addedDate));

                }

                try {
                    images.clear();
                    mails.clear();
                    imagesSent.clear();
                    mailsSent.clear();
                    String sobFilePath = path + "mails.soph";
                    if (new File(sobFilePath).exists()) {
                        br = new BufferedReader(new FileReader(sobFilePath));
                        while ((line = br.readLine()) != null) {

                            // use comma as separator
                            String[] columns = line.split(cvsSplitBy);
                            if (columns[2].equals(dateFormat)) {
                                if (columns[3].equals("0")) {
                                    images.add(columns[0]);
                                    mails.add(columns[1]);
                                } else {
                                    imagesSent.add(columns[0]);
                                    mailsSent.add(columns[1]);
                                }
                            }
                            if (jCheckBox1.isSelected()) {
                                if (columns[2].equals(dateFormatAdded)) {
                                    if (columns[3].equals("0")) {
                                        images.add(columns[0]);
                                        mails.add(columns[1]);
                                    } else {
                                        imagesSent.add(columns[0]);
                                        mailsSent.add(columns[1]);
                                    }
                                }
                            }

                        }
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                jLabel2.setText(images.size()+mailsSent.size() + " " + java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.jLabel2.text") + " (" + mailsSent.size() + " " + java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.jLabel2.sent") + ")");
                if (images.size() != 0) {
                    jButton1.setEnabled(true);
                    editProvider.setEnabled(true);
                    editPassword.setEnabled(true);
                    editSubject.setEnabled(true);
                    editTestmail.setEnabled(true);
                    editText.setEnabled(true);
                    editUsername.setEnabled(true);
                    buttonSend.setEnabled(true);
                    sendAllButton.setEnabled(true);
                    buttonTestmail.setEnabled(true);
                    editMailadress.setEnabled(true);
                } else {
                    jButton1.setEnabled(false);
                    editProvider.setEnabled(false);
                    editPassword.setEnabled(false);
                    editSubject.setEnabled(false);
                    editTestmail.setEnabled(false);
                    editText.setEnabled(false);
                    editUsername.setEnabled(false);
                    buttonSend.setEnabled(false);
                    sendAllButton.setEnabled(false);
                    buttonTestmail.setEnabled(false);
                    editMailadress.setEnabled(false);
                }
            }
        } else {
            mails.clear();
            images.clear();
            if (images.isEmpty()) {
                jButton1.setEnabled(false);
                editProvider.setEnabled(false);
                editPassword.setEnabled(false);
                editSubject.setEnabled(false);
                editTestmail.setEnabled(false);
                editText.setEnabled(false);
                editUsername.setEnabled(false);
                buttonSend.setEnabled(false);
                sendAllButton.setEnabled(false);
                buttonTestmail.setEnabled(false);
                editMailadress.setEnabled(false);
                jLabel2.setText(images.size()+mailsSent.size() + " " + java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.jLabel2.text") + " (" + mailsSent.size() + " " + java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.jLabel2.sent") + ")");
            }

        }


    }//GEN-LAST:event_jXDatePicker1PropertyChange

    private void buttonTestmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTestmailActionPerformed
        if (!editProvider.getSelectedItem().toString().equals("-")) {
            int port = 0;
            if (!editPort.getText().equals("")) {
                port = Integer.parseInt(editPort.getText());
            }
            MailConfig config = new MailConfig(editProvider.getSelectedItem().toString(), editUsername.getText(), new String(editPassword.getPassword()), editMailadress.getText(), editText.getText(), editSubject.getText(), editEncrypt.getSelectedItem().toString(), editHost.getText(), port, sendWatermarked.isSelected());
            List<String> testmail = new ArrayList<>();
            testmail.add(editTestmail.getText());
            List<String> testImage = new ArrayList<>();
            testImage.add(images.get(0));

            MailWorker worker = new MailWorker(testmail, testImage, path, config, jTextArea1);
            worker.execute();
            jTextArea1.append(java.util.ResourceBundle.getBundle("properties/Bundle").getString("MailConfigFrame.Worker.testmail"));
            jTextArea1.append("\n");
        }
    }//GEN-LAST:event_buttonTestmailActionPerformed

    private void buttonSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSendActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(null, java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.Worker.question"), "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {

            int port = 0;
            if (!editPort.getText().equals("")) {
                port = Integer.parseInt(editPort.getText());
            }
            MailConfig config = new MailConfig(editProvider.getSelectedItem().toString(), editUsername.getText(), new String(editPassword.getPassword()), editMailadress.getText(), editText.getText(), editSubject.getText(), editEncrypt.getSelectedItem().toString(), editHost.getText(), port, sendWatermarked.isSelected());
            MailWorker mailWorker = new MailWorker(mails, images, path, config, jTextArea1);
            mailWorker.execute();
        }

    }//GEN-LAST:event_buttonSendActionPerformed

    private void jCheckBox1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jCheckBox1PropertyChange


    }//GEN-LAST:event_jCheckBox1PropertyChange

    private void editPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPortActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editPortActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        JFileChooser fc = new JFileChooser(new PathCreator().getImagePath());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.showOpenDialog(this);
        if (fc.getSelectedFile() != null) {
            String newPath = fc.getSelectedFile().getPath();

            if (newPath.substring(newPath.length() - 2).equals("/.")) {
                newPath = newPath.substring(0, newPath.length() - 2);
            }
            if (newPath.substring(newPath.length() - 3).equals("/..")) {

                newPath = newPath.substring(0, newPath.length() - 3);
            }
            newPath = newPath + File.separator;
            pathLabel.setText(newPath);
            path = newPath;
            String sophFilePath = path + "mails.soph";
            File mailsFile = new File(sophFilePath);
            images.clear();
            mails.clear();
            System.out.println(mailsFile.getAbsolutePath());
            if (mailsFile.exists()) {
                BufferedReader br = null;
                try {

                    String line = "";
                    String cvsSplitBy = ",";
                    br = new BufferedReader(new FileReader(sophFilePath));
                    while ((line = br.readLine()) != null) {

                        // use comma as separator
                        String[] columns = line.split(cvsSplitBy);

                        if (columns[3].equals("0")) {
                            images.add(columns[0]);
                            mails.add(columns[1]);
                        } else {
                            imagesSent.add(columns[0]);
                            mailsSent.add(columns[1]);
                        }

                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (images.isEmpty()) {
                jButton1.setEnabled(false);
                editProvider.setEnabled(false);
                editPassword.setEnabled(false);
                editSubject.setEnabled(false);
                editTestmail.setEnabled(false);
                editText.setEnabled(false);
                editUsername.setEnabled(false);
                buttonSend.setEnabled(false);
                sendAllButton.setEnabled(false);
                buttonTestmail.setEnabled(false);
                editMailadress.setEnabled(false);
                jXDatePicker1.setEnabled(false);
                jCheckBox1.setEnabled(false);
            } else {
                jButton1.setEnabled(true);
                editProvider.setEnabled(true);
                editPassword.setEnabled(true);
                editSubject.setEnabled(true);
                editTestmail.setEnabled(true);
                editText.setEnabled(true);
                editUsername.setEnabled(true);
                buttonSend.setEnabled(true);
                sendAllButton.setEnabled(true);
                buttonTestmail.setEnabled(true);
                editMailadress.setEnabled(true);
                jXDatePicker1.setEnabled(true);
                jCheckBox1.setEnabled(false);

            }
            jLabel2.setText(images.size()+mailsSent.size() + " " + java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.jLabel2.text") + " (" + mailsSent.size() + " " + java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.jLabel2.sent") + ")");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void sendAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendAllButtonActionPerformed
        List<String> allImages = new ArrayList<>();
        List<String> allMails = new ArrayList<>();
        
        allImages.addAll(images);
        allImages.addAll(imagesSent);
        
        allMails.addAll(mails);
        allMails.addAll(mailsSent);
        
        int dialogResult = JOptionPane.showConfirmDialog(null, java.util.ResourceBundle.getBundle("properties/Bundle").getString("UploadFrame.Worker.questionAll"), "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {

            int port = 0;
            if (!editPort.getText().equals("")) {
                port = Integer.parseInt(editPort.getText());
            }
            MailConfig config = new MailConfig(editProvider.getSelectedItem().toString(), editUsername.getText(), new String(editPassword.getPassword()), editMailadress.getText(), editText.getText(), editSubject.getText(), editEncrypt.getSelectedItem().toString(), editHost.getText(), port, sendWatermarked.isSelected());
            MailWorker mailWorker = new MailWorker(allMails, allImages, path, config, jTextArea1);
            mailWorker.execute();
        }

    }//GEN-LAST:event_sendAllButtonActionPerformed

    private void sendWatermarkedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendWatermarkedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sendWatermarkedActionPerformed

    Throwable getCause(Throwable e) {
        Throwable cause = null;
        Throwable result = e;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonSend;
    private javax.swing.JButton buttonTestmail;
    private org.jdatepicker.impl.DateComponentFormatter dateComponentFormatter1;
    private javax.swing.JComboBox editEncrypt;
    private javax.swing.JTextField editHost;
    private javax.swing.JTextField editMailadress;
    private javax.swing.JPasswordField editPassword;
    private javax.swing.JTextField editPort;
    private javax.swing.JComboBox editProvider;
    private javax.swing.JTextField editSubject;
    private javax.swing.JTextField editTestmail;
    private javax.swing.JTextArea editText;
    private javax.swing.JTextField editUsername;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private org.jdatepicker.util.JDatePickerUtil jDatePickerUtil1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JButton sendAllButton;
    private javax.swing.JCheckBox sendWatermarked;
    // End of variables declaration//GEN-END:variables
}
