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
package com.freebooth.photobooth;

import com.freebooth.fileWatcher.SlideshowFileWatcher;
import com.freebooth.mailWizard.UploadFrame;
import com.freebooth.utilities.PathCreator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.util.HashMap;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.SystemUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
//import com.freebooth.server.GetActImageServlet;
//import com.freebooth.server.GetAllImagesServlet;

/**
 *
 * @author johannes
 */
public class StartFrame extends javax.swing.JFrame {

    SlideshowFileWatcher fw;
    Thread fwt;
    Server server;
    static GraphicsDevice device = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices()[0];

    /**
     * Creates new form StartFrame
     */
    public StartFrame(boolean skipSetup) {
        Preferences prefs = Preferences.userNodeForPackage(PhotoboothFrame.class);
        this.setTitle("FreeBooth");
        initComponents();
        configTabbedPane.add(new WatermarkPanel());
        configTabbedPane.setTitleAt(2, java.util.ResourceBundle.getBundle("properties/Bundle").getString("StartFrame.watermarkPanel.TabConstraints.tabTitle"));
        this.setIconImage(
                Toolkit.getDefaultToolkit().getImage("photobooth_icon.png")
        );
        configTabbedPane.add(new ShareOptionsPanel());
        configTabbedPane.setTitleAt(3, java.util.ResourceBundle.getBundle("properties/Bundle").getString("StartFrame.onlineModePanel.TabConstraints.tabTitle"));

        pathLabel.setText(new PathCreator().getImagePath());
        newPathLabel.setText(new PathCreator().getImagePath());

        demoButton.setBackground(Color.decode(prefs.get("button_color", "#ED5E2F")));
        innerDemoPanel.setBackground(Color.decode(prefs.get("background_color", "#333333")));
        demoButton.setForeground(Color.decode(prefs.get("button_foreground", "#ffffff")));
        Font font = demoButton.getFont();
        demoButton.setFont(new Font(font.getName(), font.getStyle(), prefs.getInt("button_font_size", 20)));
        demoLabel.setForeground(Color.decode(prefs.get("text_color", "#ffffff")));
        String dark = prefs.get("dark_icon", "");
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/previous_thmb" + dark + ".png")));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/next_thmb" + dark + ".png")));

        //font options
        buttonFontSizeSpinner.setValue(prefs.getInt("button_font_size", 20));
        keyboardFontSizeSpinner.setValue(prefs.getInt("button_font_size", 34));

        //select true/false checkboxes in List
        Map<String, javax.swing.JCheckBox> checkBoxMap = new HashMap<>();
        checkBoxMap.put("osk", oskCheckbox);
        checkBoxMap.put("leavepw", leavePasswordCheckbox);
        checkBoxMap.put("thumbnails", thumbnailCheckbox);
        checkBoxMap.put("update_on_new_picture",jCheckBox2);

        pack();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                setVisible(false); //you can't see me!
                dispose();
                if (fwt != null) {
                    fwt.interrupt();
                }

                if (server != null) {
                    try {
                        server.stop();
                    } catch (Exception ex) {
                        Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        });

        jCheckBox1.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                Preferences prefs = Preferences.userNodeForPackage(PhotoboothFrame.class);
                if (jCheckBox1.isSelected()) {
                    prefs.put("dark_icon", "_dark");

                } else {
                    prefs.put("dark_icon", "");
                }
                String dark = prefs.get("dark_icon", "");
                jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/next_thmb" + dark + ".png")));
                jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/previous_thmb" + dark + ".png")));
            }

        });

        for (Map.Entry<String, javax.swing.JCheckBox> entry : checkBoxMap.entrySet()) {
            javax.swing.JCheckBox checkbox = entry.getValue();
            String key = entry.getKey();
            checkbox.setSelected(Preferences.userNodeForPackage(StartFrame.class).getBoolean(key, false));

            checkbox.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    Preferences prefs = Preferences.userNodeForPackage(PhotoboothFrame.class);
                    if (checkbox.isSelected()) {
                        Preferences.userNodeForPackage(StartFrame.class).put(key, "true");

                    } else {
                        Preferences.userNodeForPackage(StartFrame.class).put(key, "false");
                    }

                }

            });
        }

        if (prefs.get("dark_icon", "").equals("_dark")) {
            jCheckBox1.setSelected(true);
        }

        JLabel lab = new JLabel(java.util.ResourceBundle.getBundle("properties/Bundle").getString("StartFrame.mainPanel.TabConstraints.tabTitle"));
        lab.setPreferredSize(new Dimension(200, 30));
        mainTabbedPane.setTabComponentAt(0, lab);

        widthSpinner.setValue(prefs.getInt("image_width", 1400));

        widthSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (Integer.parseInt(widthSpinner.getValue().toString()) < 1) {
                    widthSpinner.setValue(new Integer(1));
                }

                prefs.put("image_width", widthSpinner.getValue().toString());
            }
        });

        heightSpinner.setValue(prefs.getInt("image_height", 800));

        heightSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (Integer.parseInt(heightSpinner.getValue().toString()) < 1) {
                    heightSpinner.setValue(new Integer(1));
                }

                prefs.put("image_height", heightSpinner.getValue().toString());
            }
        });
        
        buttonFontSizeSpinner.setValue(prefs.getInt("button_font_size", 20));

        buttonFontSizeSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (Integer.parseInt(buttonFontSizeSpinner.getValue().toString()) < 1) {
                    buttonFontSizeSpinner.setValue(new Integer(1));
                }

                prefs.put("button_font_size", buttonFontSizeSpinner.getValue().toString());
                Font font = demoButton.getFont();
                demoButton.setFont(new Font(font.getName(),font.getStyle(),(int) buttonFontSizeSpinner.getValue()));
            }
        });
        
        keyboardFontSizeSpinner.setValue(prefs.getInt("keyboard_font_size", 32));

        keyboardFontSizeSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (Integer.parseInt(keyboardFontSizeSpinner.getValue().toString()) < 1) {
                    keyboardFontSizeSpinner.setValue(new Integer(1));
                }
                prefs.put("keyboard_font_size", keyboardFontSizeSpinner.getValue().toString());             
            }
        });
        if(skipSetup){
            jButton1.doClick();
        }

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

        mainTabbedPane = new javax.swing.JTabbedPane();
        mainPanel = new javax.swing.JPanel();
        welcomeLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        pathLabel = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        supportLabel = new javax.swing.JLabel();
        startPhotoViewButton = new javax.swing.JButton();
        configPanel = new javax.swing.JPanel();
        configTabbedPane = new javax.swing.JTabbedPane();
        generalPanel = new javax.swing.JPanel();
        changePathButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        newPathLabel = new javax.swing.JLabel();
        oskCheckbox = new javax.swing.JCheckBox();
        leavePasswordCheckbox = new javax.swing.JCheckBox();
        thumbnailCheckbox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        heightLabel = new javax.swing.JLabel();
        heightSpinner = new javax.swing.JSpinner();
        widthLabel = new javax.swing.JLabel();
        widthSpinner = new javax.swing.JSpinner();
        jCheckBox2 = new javax.swing.JCheckBox();
        appearancePanel = new javax.swing.JPanel();
        demoPanel = new javax.swing.JPanel();
        innerDemoPanel = new javax.swing.JPanel();
        demoButton = new javax.swing.JButton();
        demoLabel = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        changeBackgroundColorButton = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton9 = new javax.swing.JButton();
        buttonFontSizeLabel = new javax.swing.JLabel();
        keyboardFontSizeLabel = new javax.swing.JLabel();
        buttonFontSizeSpinner = new javax.swing.JSpinner();
        keyboardFontSizeSpinner = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("properties/Bundle"); // NOI18N
        setTitle(bundle.getString("StartFrame.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(500, 570));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        mainTabbedPane.setPreferredSize(new java.awt.Dimension(500, 400));

        mainPanel.setLayout(new java.awt.GridBagLayout());

        welcomeLabel.setFont(new java.awt.Font("Noto Sans", 0, 18)); // NOI18N
        welcomeLabel.setText(bundle.getString("StartFrame.welcomeLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 159;
        gridBagConstraints.insets = new java.awt.Insets(40, 0, 20, 0);
        mainPanel.add(welcomeLabel, gridBagConstraints);

        jButton1.setText(bundle.getString("StartFrame.jButton1.text")); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(190, 40));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        mainPanel.add(jButton1, gridBagConstraints);

        jButton2.setText(bundle.getString("StartFrame.jButton2.text")); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(190, 40));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        mainPanel.add(jButton2, gridBagConstraints);

        jLabel2.setText(bundle.getString("StartFrame.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        mainPanel.add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        mainPanel.add(pathLabel, gridBagConstraints);

        jButton3.setText(bundle.getString("StartFrame.jButton3.text")); // NOI18N
        jButton3.setMaximumSize(new java.awt.Dimension(190, 40));
        jButton3.setPreferredSize(new java.awt.Dimension(190, 40));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        mainPanel.add(jButton3, gridBagConstraints);

        supportLabel.setText(bundle.getString("StartFrame.supportLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 40, 0);
        mainPanel.add(supportLabel, gridBagConstraints);

        startPhotoViewButton.setText(bundle.getString("StartFrame.startPhotoViewButton.text")); // NOI18N
        startPhotoViewButton.setPreferredSize(new java.awt.Dimension(190, 40));
        startPhotoViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startPhotoViewButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        mainPanel.add(startPhotoViewButton, gridBagConstraints);

        mainTabbedPane.addTab(bundle.getString("StartFrame.mainPanel.TabConstraints.tabTitle"), mainPanel); // NOI18N

        configPanel.setPreferredSize(new java.awt.Dimension(420, 277));
        configPanel.setLayout(new java.awt.BorderLayout());

        generalPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        generalPanel.setVerifyInputWhenFocusTarget(false);
        generalPanel.setLayout(new java.awt.GridBagLayout());

        changePathButton.setText(bundle.getString("StartFrame.changePathButton.text")); // NOI18N
        changePathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePathButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        generalPanel.add(changePathButton, gridBagConstraints);

        jLabel5.setText(bundle.getString("StartFrame.jLabel5.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        generalPanel.add(jLabel5, gridBagConstraints);

        newPathLabel.setText(bundle.getString("StartFrame.newPathLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        generalPanel.add(newPathLabel, gridBagConstraints);

        oskCheckbox.setText(bundle.getString("StartFrame.oskCheckbox.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        generalPanel.add(oskCheckbox, gridBagConstraints);

        leavePasswordCheckbox.setText(bundle.getString("StartFrame.leavePasswordCheckbox.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        generalPanel.add(leavePasswordCheckbox, gridBagConstraints);

        thumbnailCheckbox.setText(bundle.getString("StartFrame.thumbnailCheckbox.text_1")); // NOI18N
        thumbnailCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thumbnailCheckboxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        generalPanel.add(thumbnailCheckbox, gridBagConstraints);

        jLabel1.setText(bundle.getString("StartFrame.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        generalPanel.add(jLabel1, gridBagConstraints);

        heightLabel.setText(bundle.getString("StartFrame.heightLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        generalPanel.add(heightLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        generalPanel.add(heightSpinner, gridBagConstraints);

        widthLabel.setText(bundle.getString("StartFrame.widthLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 5.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        generalPanel.add(widthLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 5.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        generalPanel.add(widthSpinner, gridBagConstraints);

        jCheckBox2.setText(bundle.getString("StartFrame.jCheckBox2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 700.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        generalPanel.add(jCheckBox2, gridBagConstraints);

        configTabbedPane.addTab(bundle.getString("StartFrame.generalPanel.TabConstraints.tabTitle_1"), generalPanel); // NOI18N

        java.awt.GridBagLayout appearancePanelLayout = new java.awt.GridBagLayout();
        appearancePanelLayout.columnWidths = new int[] {0, 10, 0, 10, 0};
        appearancePanelLayout.rowHeights = new int[] {0, 10, 0, 10, 0, 10, 0, 10, 0};
        appearancePanel.setLayout(appearancePanelLayout);

        innerDemoPanel.setBackground(new java.awt.Color(253, 253, 190));
        innerDemoPanel.setLayout(new java.awt.GridBagLayout());

        demoButton.setBackground(new java.awt.Color(36, 42, 106));
        demoButton.setFont(new java.awt.Font("Noto Sans", 0, 24)); // NOI18N
        demoButton.setForeground(new java.awt.Color(254, 254, 254));
        demoButton.setText(bundle.getString("StartFrame.demoButton.text")); // NOI18N
        demoButton.setBorder(new javax.swing.border.MatteBorder(null));
        demoButton.setBorderPainted(false);
        demoButton.setContentAreaFilled(false);
        demoButton.setFocusPainted(false);
        demoButton.setMargin(new java.awt.Insets(10, 10, 10, 10));
        demoButton.setMaximumSize(new java.awt.Dimension(336, 70));
        demoButton.setMinimumSize(new java.awt.Dimension(336, 70));
        demoButton.setOpaque(true);
        demoButton.setPreferredSize(new java.awt.Dimension(200, 60));
        demoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                demoButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 20, 20);
        innerDemoPanel.add(demoButton, gridBagConstraints);

        demoLabel.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        demoLabel.setText(bundle.getString("StartFrame.demoLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        innerDemoPanel.add(demoLabel, gridBagConstraints);

        jButton8.setBorderPainted(false);
        jButton8.setFocusPainted(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        innerDemoPanel.add(jButton8, gridBagConstraints);

        jButton7.setBorderPainted(false);
        jButton7.setFocusPainted(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        innerDemoPanel.add(jButton7, gridBagConstraints);

        javax.swing.GroupLayout demoPanelLayout = new javax.swing.GroupLayout(demoPanel);
        demoPanel.setLayout(demoPanelLayout);
        demoPanelLayout.setHorizontalGroup(
            demoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(innerDemoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
        );
        demoPanelLayout.setVerticalGroup(
            demoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(innerDemoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        appearancePanel.add(demoPanel, gridBagConstraints);

        changeBackgroundColorButton.setText(bundle.getString("StartFrame.changeBackgroundColorButton.text")); // NOI18N
        changeBackgroundColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeBackgroundColorButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        appearancePanel.add(changeBackgroundColorButton, gridBagConstraints);

        jButton5.setText(bundle.getString("StartFrame.jButton5.text")); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        appearancePanel.add(jButton5, gridBagConstraints);

        jButton4.setText(bundle.getString("StartFrame.jButton4.text")); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        appearancePanel.add(jButton4, gridBagConstraints);

        jButton6.setText(bundle.getString("StartFrame.jButton6.text")); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        appearancePanel.add(jButton6, gridBagConstraints);

        jCheckBox1.setText(bundle.getString("StartFrame.jCheckBox1.text")); // NOI18N
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        appearancePanel.add(jCheckBox1, gridBagConstraints);

        jButton9.setText(bundle.getString("StartFrame.jButton9.text")); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        appearancePanel.add(jButton9, gridBagConstraints);

        buttonFontSizeLabel.setText(bundle.getString("StartFrame.buttonFontSizeLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        appearancePanel.add(buttonFontSizeLabel, gridBagConstraints);

        keyboardFontSizeLabel.setText(bundle.getString("StartFrame.keyboardFontSizeLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        appearancePanel.add(keyboardFontSizeLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        appearancePanel.add(buttonFontSizeSpinner, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        appearancePanel.add(keyboardFontSizeSpinner, gridBagConstraints);

        configTabbedPane.addTab(bundle.getString("StartFrame.appearancePanel.TabConstraints.tabTitle"), appearancePanel); // NOI18N

        configPanel.add(configTabbedPane, java.awt.BorderLayout.CENTER);

        mainTabbedPane.addTab(bundle.getString("StartFrame.configPanel.TabConstraints.tabTitle"), configPanel); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(mainTabbedPane, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (Preferences.userNodeForPackage(StartFrame.class).getBoolean("multiSelect", true)) {
            //FileWatcher fw = new FileWatcher();
            MultiSelectPhotoboothFrame myFrame = new MultiSelectPhotoboothFrame();
            //myFrame.getContentPane().setBackground(java.awt.Color.DARK_GRAY);
            if (!SystemUtils.IS_OS_WINDOWS) {
                device.setFullScreenWindow(myFrame);
            }

            myFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            myFrame.setVisible(true);
        } else {
            //FileWatcher fw = new FileWatcher();
            PhotoboothFrame myFrame = new PhotoboothFrame();
            //myFrame.getContentPane().setBackground(java.awt.Color.DARK_GRAY);
            if (!SystemUtils.IS_OS_WINDOWS) {
                device.setFullScreenWindow(myFrame);
            }

            myFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            myFrame.setVisible(true);
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        UploadFrame uf = new UploadFrame();
        uf.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (server == null || !server.isRunning()) {
            this.fw = new SlideshowFileWatcher();
            fwt = new Thread(fw);
            fwt.start();

            server = new Server();
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(8084);
            server.addConnector(connector);

            // The filesystem paths we will map
            String homePath = new PathCreator().getWebPath();
            String pwdPath = new PathCreator().getWebPath();

            // Setup the basic application "context" for this application at "/"
            // This is also known as the handler tree (in jetty speak)
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setResourceBase(pwdPath);
            context.setContextPath("/");
            server.setHandler(context);

//            ServletHolder holderDynamic = new ServletHolder(new GetActImageServlet(fw));
//            context.addServlet(holderDynamic, "/getActImage/*");
//
//            //
//            context.addServlet(new ServletHolder(new GetAllImagesServlet(fw)), "/getAllImages/*");

            // add special pathspec of "/home/" content mapped to the homePath
            ServletHolder holderHome = new ServletHolder("static-home", DefaultServlet.class);
            holderHome.setInitParameter("resourceBase", homePath);
            holderHome.setInitParameter("dirAllowed", "true");
            holderHome.setInitParameter("pathInfoOnly", "true");
            context.addServlet(holderHome, "/*");

            // Lastly, the default servlet for root content (always needed, to satisfy servlet spec)
            // It is important that this is last.
            ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
            holderPwd.setInitParameter("dirAllowed", "true");
            context.addServlet(holderPwd, "/");

            try {
                server.start();
                new LinkDialog(this, rootPaneCheckingEnabled, java.util.ResourceBundle.getBundle("properties/Bundle").getString("StartFrame.messageSuccess")).setVisible(true);

                //server.join();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);

            }
        } else {
            new LinkDialog(this, rootPaneCheckingEnabled, java.util.ResourceBundle.getBundle("properties/Bundle").getString("StartFrame.messageAlreadyRun")).setVisible(true);

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void demoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_demoButtonActionPerformed

    }//GEN-LAST:event_demoButtonActionPerformed

    private void changeBackgroundColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeBackgroundColorButtonActionPerformed
        Color newColor = JColorChooser.showDialog(
                this, java.util.ResourceBundle.getBundle("properties/Bundle").getString("StartFrame.changeBackgroundColorButton.text"),
                innerDemoPanel.getBackground());
        if (newColor != null) {
            String hexColor = Integer.toHexString(newColor.getRGB() & 0xffffff);
            hexColor = "#" + hexColor;
            Preferences prefs = Preferences.userNodeForPackage(PhotoboothFrame.class);
            prefs.put("background_color", hexColor);
            innerDemoPanel.setBackground(Color.decode(hexColor));
            CssDialog cssd = new CssDialog(this, rootPaneCheckingEnabled, hexColor);
            cssd.setVisible(true);

        }

    }//GEN-LAST:event_changeBackgroundColorButtonActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Color newColor = JColorChooser.showDialog(
                this, java.util.ResourceBundle.getBundle("properties/Bundle").getString("StartFrame.jButton5.text"),
                demoButton.getBackground());
        if (newColor != null) {
            String hexColor = Integer.toHexString(newColor.getRGB() & 0xffffff);
            hexColor = "#" + hexColor;
            Preferences prefs = Preferences.userNodeForPackage(PhotoboothFrame.class);
            prefs.put("button_color", hexColor);
            demoButton.setBackground(Color.decode(hexColor));
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Color newColor = JColorChooser.showDialog(
                this, java.util.ResourceBundle.getBundle("properties/Bundle").getString("StartFrame.jButton4.text"),
                demoButton.getForeground());
        if (newColor != null) {
            String hexColor = Integer.toHexString(newColor.getRGB() & 0xffffff);
            hexColor = "#" + hexColor;
            Preferences prefs = Preferences.userNodeForPackage(PhotoboothFrame.class);
            prefs.put("button_foreground", hexColor);
            demoButton.setForeground(Color.decode(hexColor));
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        Color newColor = JColorChooser.showDialog(
                this, java.util.ResourceBundle.getBundle("properties/Bundle").getString("StartFrame.jButton6.text"),
                demoLabel.getForeground());
        if (newColor != null) {
            String hexColor = Integer.toHexString(newColor.getRGB() & 0xffffff);
            hexColor = "#" + hexColor;
            Preferences prefs = Preferences.userNodeForPackage(PhotoboothFrame.class);
            prefs.put("text_color", hexColor);
            demoLabel.setForeground(Color.decode(hexColor));
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        Preferences prefs = Preferences.userNodeForPackage(PhotoboothFrame.class);
        prefs.remove("button_color");
        prefs.remove("text_color");
        prefs.remove("button_foreground");
        prefs.remove("background_color");
        prefs.remove("dark_icon");
        prefs.remove("button_font_size");
        prefs.remove("keyboard_font_size");
        demoButton.setBackground(Color.decode(prefs.get("button_color", "#ED5E2F")));
        Font font = demoButton.getFont();
        demoButton.setFont(new Font(font.getName(),font.getStyle(),prefs.getInt("button_font_size", 20)));
        
        innerDemoPanel.setBackground(Color.decode(prefs.get("background_color", "#333333")));
        demoButton.setForeground(Color.decode(prefs.get("button_foreground", "#ffffff")));
        demoLabel.setForeground(Color.decode(prefs.get("text_color", "#ffffff")));
        String dark = prefs.get("dark_icon", "");
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/next_thmb" + dark + ".png")));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/previous_thmb" + dark + ".png")));

        buttonFontSizeSpinner.setValue(prefs.getInt("button_font_size", 20));
        keyboardFontSizeSpinner.setValue(prefs.getInt("keyboard_font_size", 32));

    }//GEN-LAST:event_jButton9ActionPerformed

    private void changePathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePathButtonActionPerformed
        PathCreator pc = new PathCreator();
        JFileChooser fc = new JFileChooser(pc.getImagePath());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.showOpenDialog(this);
        Preferences prefs = Preferences.userNodeForPackage(StartFrame.class);
        String path = fc.getSelectedFile().getPath();
        if (path.substring(path.length() - 2).equals("/.")) {
            path = path.substring(0, path.length() - 1);
        }
        if (path.substring(path.length() - 3).equals("/..")) {

            path = path.substring(0, path.length() - 2);
        }

        prefs.put("hot_folder_path", path);
        newPathLabel.setText(path);
        pathLabel.setText(path);
    }//GEN-LAST:event_changePathButtonActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void thumbnailCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thumbnailCheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_thumbnailCheckboxActionPerformed

    private void startPhotoViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startPhotoViewButtonActionPerformed
        Preferences prefs = Preferences.userNodeForPackage(PhotoboothFrame.class);
        PhotoViewFrame myFrame = new PhotoViewFrame();  
        myFrame.setVisible(true);
                
    }//GEN-LAST:event_startPhotoViewButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JPanel appearancePanel;
    javax.swing.JLabel buttonFontSizeLabel;
    javax.swing.JSpinner buttonFontSizeSpinner;
    javax.swing.JButton changeBackgroundColorButton;
    javax.swing.JButton changePathButton;
    javax.swing.JPanel configPanel;
    javax.swing.JTabbedPane configTabbedPane;
    javax.swing.JButton demoButton;
    javax.swing.JLabel demoLabel;
    javax.swing.JPanel demoPanel;
    javax.swing.JPanel generalPanel;
    javax.swing.JLabel heightLabel;
    javax.swing.JSpinner heightSpinner;
    javax.swing.JPanel innerDemoPanel;
    javax.swing.JButton jButton1;
    javax.swing.JButton jButton2;
    javax.swing.JButton jButton3;
    javax.swing.JButton jButton4;
    javax.swing.JButton jButton5;
    javax.swing.JButton jButton6;
    javax.swing.JButton jButton7;
    javax.swing.JButton jButton8;
    javax.swing.JButton jButton9;
    javax.swing.JCheckBox jCheckBox1;
    javax.swing.JCheckBox jCheckBox2;
    javax.swing.JLabel jLabel1;
    javax.swing.JLabel jLabel2;
    javax.swing.JLabel jLabel5;
    javax.swing.JLabel keyboardFontSizeLabel;
    javax.swing.JSpinner keyboardFontSizeSpinner;
    javax.swing.JCheckBox leavePasswordCheckbox;
    javax.swing.JPanel mainPanel;
    javax.swing.JTabbedPane mainTabbedPane;
    javax.swing.JLabel newPathLabel;
    javax.swing.JCheckBox oskCheckbox;
    javax.swing.JLabel pathLabel;
    javax.swing.JButton startPhotoViewButton;
    javax.swing.JLabel supportLabel;
    javax.swing.JCheckBox thumbnailCheckbox;
    javax.swing.JLabel welcomeLabel;
    javax.swing.JLabel widthLabel;
    javax.swing.JSpinner widthSpinner;
    // End of variables declaration//GEN-END:variables
}
