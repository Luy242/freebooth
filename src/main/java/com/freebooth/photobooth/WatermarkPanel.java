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
package com.freebooth.photobooth;

import com.freebooth.utilities.PathCreator;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.prefs.Preferences;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import say.swing.JFontChooser;

/**
 *
 * @author johannes
 */
public class WatermarkPanel extends javax.swing.JPanel {

    Preferences prefs;

    /**
     * Creates new form WatermarkPanel
     */
    public WatermarkPanel() {
        initComponents();
        this.prefs = Preferences.userNodeForPackage(WatermarkPanel.class);
        
        //listener for the watermark overlay checkbox, this will also be called during init
        jCheckBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (jCheckBox1.isSelected()) {
                    prefs.put("watermark", "true");
                    String position = prefs.get("watermark_position", "center");
                    enableOneButton(position);
                    jComboBox1.setEnabled(true);

                    positionNorthWestButton.setEnabled(true);
                    positionNorthButton.setEnabled(true);
                    positionNorthEastButton.setEnabled(true);
                    positionWestButton.setEnabled(true);
                    positionSouthWestButton.setEnabled(true);
                    positionCenterButton.setEnabled(true);
                    positionSouthButton.setEnabled(true);
                    positionEastButton.setEnabled(true);
                    positionSouthEastButton.setEnabled(true);

                    
                    jSpinner3.setEnabled(true);
                    jSpinner4.setEnabled(true);

                    if (jComboBox1.getSelectedItem().toString().equals("Text")) {
                        jTextField2.setEnabled(true);
                        jButton2.setEnabled(true);
                        jButton1.setEnabled(false);
                        jButton3.setEnabled(true);
                        jSpinner1.setEnabled(false);
                        jSpinner2.setEnabled(true);

                    } else {
                        jButton1.setEnabled(true);
                        jTextField2.setEnabled(false);
                        jButton2.setEnabled(false);
                        jButton3.setEnabled(false);
                        jSpinner1.setEnabled(true);

                    }

                } else {
                    disableAll();
                    prefs.put("watermark", "false");

                }

            }

        });

        //event listener for text changes 
        jTextField2.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                prefs.put("watermark_text", jTextField2.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                prefs.put("watermark_text", jTextField2.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                prefs.put("watermark_text", jTextField2.getText());
            }
        });
     
        

      
        //opacity change listener
        jSpinner2.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if(Integer.parseInt(jSpinner2.getValue().toString())<1){
                    jSpinner2.setValue(new Integer(1));
                }
                if(Integer.parseInt(jSpinner2.getValue().toString())>100){
                    jSpinner2.setValue(new Integer(100));
                }
                prefs.put("watermark_opacity", jSpinner2.getValue().toString());
            }
        });
        
        // set watermark size on change
        jSpinner1.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if(Integer.parseInt(jSpinner1.getValue().toString())<1){
                    jSpinner1.setValue(new Integer(1));
                }
                if(Integer.parseInt(jSpinner1.getValue().toString())>100){
                    jSpinner1.setValue(new Integer(100));
                }
                prefs.put("watermark_percentage", jSpinner1.getValue().toString());
            }
        });
        
        //margin x change listener
        jSpinner3.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if(Integer.parseInt(jSpinner1.getValue().toString())<0){
                    jSpinner3.setValue(new Integer(0));
                }
                if(Integer.parseInt(jSpinner3.getValue().toString())>100){
                    jSpinner3.setValue(new Integer(100));
                }
                prefs.put("watermark_margin_x", jSpinner3.getValue().toString());
            }
        });
        
        //margin x change listener
        jSpinner4.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if(Integer.parseInt(jSpinner4.getValue().toString())<0){
                    jSpinner4.setValue(new Integer(1));
                }
                if(Integer.parseInt(jSpinner4.getValue().toString())>100){
                    jSpinner4.setValue(new Integer(100));
                }
                prefs.put("watermark_margin_y", jSpinner4.getValue().toString());
            }
        });

        //type change listener, enabes and disables the components 
        jComboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (jComboBox1.getSelectedItem().toString().equals("Text")) {
                    jButton1.setEnabled(false);
                    jTextField2.setEnabled(true);
                    jButton2.setEnabled(true);
                    jButton3.setEnabled(true);
                    jSpinner2.setEnabled(true);
                    prefs.put("watermark_type", "Text");
                    jSpinner1.setEnabled(false);

                } else {
                    jButton1.setEnabled(true);
                    jTextField2.setEnabled(false);
                    jButton2.setEnabled(false);
                    jButton3.setEnabled(false);
                    jSpinner2.setEnabled(false);
                    jSpinner1.setEnabled(true);
                    prefs.put("watermark_type", "Image");
                }

            }

        });
        
        

        //init for saved values - always set
        boolean watermarkBool = Boolean.parseBoolean(prefs.get("watermark", "false"));
        enableOneButton(prefs.get("watermark_position", "center"));
        jTextField2.setText(prefs.get("watermark_text", "FreeBooth"));
        jComboBox1.setSelectedItem(prefs.get("watermark_type", "Text"));
       
        jSpinner2.setValue(Integer.parseInt(prefs.get("watermark_opacity", "0")));
        PathCreator pc = new PathCreator();
        jSpinner1.setValue(Integer.parseInt(prefs.get("watermark_percentage", "100")));
        jSpinner3.setValue(prefs.getInt("watermark_margin_x", 0));
        jSpinner4.setValue(prefs.getInt("watermark_margin_y", 0));
        
        
        jLabel5.setText(prefs.get("watermark_path", pc.getWebPath() + "watermark.png"));
        if (watermarkBool) {
            jCheckBox1.setSelected(true); //triggers the change listener
        } else {
            disableAll();
        }
        jLabel8.setFont(new Font(prefs.get("watermark_font_name", "Arial"), Integer.parseInt(prefs.get("watermark_font_style", Integer.toString(Font.PLAIN))) , 12));
        jLabel8.setText(prefs.get("watermark_font_name", "Arial") + " " + prefs.get("watermark_font_size", "12"));
        jLabel8.setForeground(Color.decode(prefs.get("watermark_text_color", "#000000")));
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        positionNorthWestButton = new javax.swing.JToggleButton();
        positionNorthButton = new javax.swing.JToggleButton();
        positionNorthEastButton = new javax.swing.JToggleButton();
        positionWestButton = new javax.swing.JToggleButton();
        positionSouthWestButton = new javax.swing.JToggleButton();
        positionCenterButton = new javax.swing.JToggleButton();
        positionSouthButton = new javax.swing.JToggleButton();
        positionEastButton = new javax.swing.JToggleButton();
        positionSouthEastButton = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jSpinner4 = new javax.swing.JSpinner();

        setLayout(new java.awt.GridBagLayout());

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("properties/Bundle"); // NOI18N
        jCheckBox1.setText(bundle.getString("WatermarkPanel.jCheckBox1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jCheckBox1, gridBagConstraints);

        jLabel1.setText(bundle.getString("WatermarkPanel.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jLabel1, gridBagConstraints);

        positionNorthWestButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrows/arrow_north_west.png"))); // NOI18N
        positionNorthWestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positionNorthWestButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        add(positionNorthWestButton, gridBagConstraints);

        positionNorthButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrows/arrow_north.png"))); // NOI18N
        positionNorthButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positionNorthButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        add(positionNorthButton, gridBagConstraints);

        positionNorthEastButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrows/arrow_north_east.png"))); // NOI18N
        positionNorthEastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positionNorthEastButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        add(positionNorthEastButton, gridBagConstraints);

        positionWestButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrows/arrow_west.png"))); // NOI18N
        positionWestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positionWestButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        add(positionWestButton, gridBagConstraints);

        positionSouthWestButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrows/arrow_south_west.png"))); // NOI18N
        positionSouthWestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positionSouthWestButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        add(positionSouthWestButton, gridBagConstraints);

        positionCenterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrows/center.png"))); // NOI18N
        positionCenterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positionCenterButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        add(positionCenterButton, gridBagConstraints);

        positionSouthButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrows/arrow_south.png"))); // NOI18N
        positionSouthButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positionSouthButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        add(positionSouthButton, gridBagConstraints);

        positionEastButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrows/arrow_east.png"))); // NOI18N
        positionEastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positionEastButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        add(positionEastButton, gridBagConstraints);

        positionSouthEastButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrows/arrow_south_east.png"))); // NOI18N
        positionSouthEastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positionSouthEastButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        add(positionSouthEastButton, gridBagConstraints);

        jLabel2.setText(bundle.getString("WatermarkPanel.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jLabel2, gridBagConstraints);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Text", "Image" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jComboBox1, gridBagConstraints);

        jLabel3.setText(bundle.getString("WatermarkPanel.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jLabel3, gridBagConstraints);

        jLabel4.setText(bundle.getString("WatermarkPanel.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jLabel4, gridBagConstraints);

        jButton1.setText(bundle.getString("WatermarkPanel.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jButton1, gridBagConstraints);

        jLabel5.setText(bundle.getString("WatermarkPanel.jLabel5.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jTextField2, gridBagConstraints);

        jLabel7.setText(bundle.getString("WatermarkPanel.jLabel7.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jLabel7, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jSpinner2, gridBagConstraints);

        jButton2.setText(bundle.getString("WatermarkPanel.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jButton2, gridBagConstraints);

        jLabel8.setText(bundle.getString("WatermarkPanel.jLabel8.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jLabel8, gridBagConstraints);

        jButton3.setText(bundle.getString("WatermarkPanel.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jButton3, gridBagConstraints);

        jLabel6.setText(bundle.getString("WatermarkPanel.jLabel6.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        add(jLabel6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        add(jSpinner1, gridBagConstraints);

        jLabel9.setText(bundle.getString("WatermarkPanel.jLabel9.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jLabel9, gridBagConstraints);

        jLabel10.setText(bundle.getString("WatermarkPanel.jLabel10.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jLabel10, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jSpinner3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jSpinner4, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void positionNorthButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positionNorthButtonActionPerformed
        savePosition("north");
    }//GEN-LAST:event_positionNorthButtonActionPerformed

    private void positionNorthWestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positionNorthWestButtonActionPerformed
        savePosition("north_west");
    }//GEN-LAST:event_positionNorthWestButtonActionPerformed

    private void positionNorthEastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positionNorthEastButtonActionPerformed
        savePosition("north_east");
    }//GEN-LAST:event_positionNorthEastButtonActionPerformed

    private void positionWestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positionWestButtonActionPerformed
        savePosition("west");
    }//GEN-LAST:event_positionWestButtonActionPerformed

    private void positionCenterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positionCenterButtonActionPerformed
        savePosition("center");
    }//GEN-LAST:event_positionCenterButtonActionPerformed

    private void positionEastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positionEastButtonActionPerformed
        savePosition("east");
    }//GEN-LAST:event_positionEastButtonActionPerformed

    private void positionSouthWestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positionSouthWestButtonActionPerformed
        savePosition("south_west");
    }//GEN-LAST:event_positionSouthWestButtonActionPerformed

    private void positionSouthButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positionSouthButtonActionPerformed
        savePosition("south");
    }//GEN-LAST:event_positionSouthButtonActionPerformed

    private void positionSouthEastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positionSouthEastButtonActionPerformed
        savePosition("south_east");
    }//GEN-LAST:event_positionSouthEastButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        JFileChooser fc = new JFileChooser(prefs.get("watermark_path", ""));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.showOpenDialog(this);
        if (fc.getSelectedFile() != null) {
            String path = fc.getSelectedFile().getAbsolutePath();

            prefs.put("watermark_path", path);
            jLabel5.setText(path);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JFontChooser fontChooser = new JFontChooser();
        fontChooser.setSelectedFont(new Font(prefs.get("watermark_font_name", "Arial"), Integer.parseInt(prefs.get("watermark_font_style", Integer.toString(Font.PLAIN))) , Integer.parseInt(prefs.get("watermark_font_size", "12"))));
        
        
        int result = fontChooser.showDialog(this);
        if (result == JFontChooser.OK_OPTION)
        {
             Font font = fontChooser.getSelectedFont(); 
             prefs.put("watermark_font_family", font.getFamily());
             prefs.put("watermark_font_name", font.getName());
             prefs.put("watermark_font_style", Integer.toString(font.getStyle()));
             prefs.put("watermark_font_size",Integer.toString(font.getSize()));
             System.out.println(font);

             jLabel8.setText(font.getFamily()+" " +font.getStyle() + " " + font.getSize()); 
             jLabel8.setFont(new Font(font.getName(), font.getStyle(), 12));
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Color newColor = JColorChooser.showDialog(
                this, java.util.ResourceBundle.getBundle("properties/Bundle").getString("WatermarkPanel.jButton3.text"),
                jLabel8.getForeground());
        if (newColor != null) {
            String hexColor = Integer.toHexString(newColor.getRGB() & 0xffffff);
            hexColor = "#" + hexColor;
           
            prefs.put("watermark_text_color", hexColor);
            jLabel8.setForeground(Color.decode(hexColor));
            

        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToggleButton positionCenterButton;
    private javax.swing.JToggleButton positionEastButton;
    private javax.swing.JToggleButton positionNorthButton;
    private javax.swing.JToggleButton positionNorthEastButton;
    private javax.swing.JToggleButton positionNorthWestButton;
    private javax.swing.JToggleButton positionSouthButton;
    private javax.swing.JToggleButton positionSouthEastButton;
    private javax.swing.JToggleButton positionSouthWestButton;
    private javax.swing.JToggleButton positionWestButton;
    // End of variables declaration//GEN-END:variables

    //method to save the selected postion
    private void savePosition(String position) {
        enableOneButton(position);
        prefs.put("watermark_position", position);
    }

    //diabels all components of the UI
    private void disableAll() {
        jComboBox1.setEnabled(false);
        positionNorthWestButton.setEnabled(false);
        positionNorthButton.setEnabled(false);
        positionNorthEastButton.setEnabled(false);
        positionWestButton.setEnabled(false);
        positionSouthWestButton.setEnabled(false);
        positionCenterButton.setEnabled(false);
        positionSouthButton.setEnabled(false);
        positionEastButton.setEnabled(false);
        positionSouthEastButton.setEnabled(false);
        jButton1.setEnabled(false);
        jTextField2.setEnabled(false);
        jButton2.setEnabled(false);
        jSpinner2.setEnabled(false);
        jButton3.setEnabled(false);
        jSpinner1.setEnabled(false);
        jSpinner3.setEnabled(false);
        jSpinner4.setEnabled(false);
    }

    //enables the one button of the given position
    private void enableOneButton(String position) {
        positionNorthWestButton.setSelected(false);
        positionNorthButton.setSelected(false);
        positionNorthEastButton.setSelected(false);
        positionWestButton.setSelected(false);
        positionSouthWestButton.setSelected(false);
        positionCenterButton.setSelected(false);
        positionSouthButton.setSelected(false);
        positionEastButton.setSelected(false);
        positionSouthEastButton.setSelected(false);

        switch (position) {
            case "center":
                positionCenterButton.setSelected(true);
                break;
            case "north_west":
                positionNorthWestButton.setSelected(true);
                break;
            case "north":
                positionNorthButton.setSelected(true);
                break;
            case "north_east":
                positionNorthEastButton.setSelected(true);
                break;
            case "west":
                positionWestButton.setSelected(true);
                break;
            case "east":
                positionEastButton.setSelected(true);
                break;
            case "south_west":
                positionSouthWestButton.setSelected(true);
                break;
            case "south":
                positionSouthButton.setSelected(true);
                break;
            case "south_east":
                positionSouthEastButton.setSelected(true);
                break;
            default:
                break;

        }

    }
}
