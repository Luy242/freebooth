/*
 * Copyright (C) 2016 Johannes
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import com.freebooth.mailWizard.MailConfig;
import com.freebooth.onlineMode.MailWorkerDirect;
import com.freebooth.utilities.PathCreator;
import com.freebooth.photobooth.PhotoboothFrame;
import com.freebooth.photobooth.ShareOptionsPanel;

/**
 *
 * @author Johannes
 */
public class OSKSaveDialog extends javax.swing.JDialog implements ActionListener {

    int lastCaretPosition = 0;
    int caretPosition = 0;
    String selectedText = null;
    boolean manuelSetCaretPosition = false;
    String imageName;
    Preferences onlinePrefs;
    PhotoboothFrame pf;

    /**
     * Creates new form oskSaveDialog
     */
    public OSKSaveDialog(java.awt.Frame parent, boolean modal,String imageName, PhotoboothFrame pf) {
        super(parent, true);
        
        initComponents();
        this.pf = pf;
        Preferences prefs = Preferences.userNodeForPackage(PhotoboothFrame.class);
        onlinePrefs = Preferences.userNodeForPackage(ShareOptionsPanel.class);
        getContentPane().setBackground(Color.decode(prefs.get("background_color", "#333333")));
        
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.decode(prefs.get("button_color", "#ED5E2F"))));
        IconFontSwing.register(FontAwesome.getIconFont());
        Color backgroundColor = Color.decode(prefs.get("button_color", "#ED5E2F"));
        Color foregroundColor = Color.decode(prefs.get("button_foreground", "#ffffff"));

        Icon icon = IconFontSwing.buildIcon(FontAwesome.CARET_SQUARE_O_UP, 30, foregroundColor);
        shiftButton.setIcon(icon);
        shiftButton.setBackground(backgroundColor);
        for (JButton button : getAllButtons()) {
            button.setBackground(backgroundColor);
            button.setForeground(foregroundColor);
            button.addActionListener(this);
        }
        if (onlinePrefs.getBoolean("onlineMode", false)) {
            jLabel1.setText(java.util.ResourceBundle.getBundle("properties/Bundle").getString("SaveDialog.jLabel1.onlineText"));
            saveButton.setText(java.util.ResourceBundle.getBundle("properties/Bundle").getString("SaveDialog.jButton3.textOnline"));
        }
        jLabel1.setForeground(Color.decode(prefs.get("text_color", "#ffffff")));
        jLabel2.setForeground(Color.decode(prefs.get("text_color", "#ffffff")));

        this.imageName = imageName;

        float scaleFactor = prefs.getInt("image_width", 1400)/1400;
        
        this.setSize(new Dimension(Math.round(this.getSize().width *scaleFactor), Math.round(this.getSize().height *scaleFactor)));
        
        scaleFactor = scaleFactor * 1.2f;
        this.setSize(new Dimension(Math.round(this.getSize().width *scaleFactor), Math.round(this.getSize().height *scaleFactor)));
        for(JButton button: getAllButtons()){
            button.setFont(new Font(button.getFont().getName(),button.getFont().getStyle() , prefs.getInt("keyboard_font_size", 32)));
            button.setPreferredSize(new Dimension(Math.round(button.getPreferredSize().width *scaleFactor), Math.round(button.getPreferredSize().height *scaleFactor)));
            button.setMaximumSize(new Dimension(Math.round(button.getMaximumSize().width *scaleFactor), Math.round(button.getMaximumSize().height *scaleFactor)));
            button.setMinimumSize(new Dimension(Math.round(button.getMinimumSize().width *scaleFactor), Math.round(button.getMinimumSize().height *scaleFactor)));
        }
        pack();
        setLocationRelativeTo(pf);
        
        if (onlinePrefs.getBoolean("onlineMode", false)) {
            jLabel1.setText(java.util.ResourceBundle.getBundle("properties/Bundle").getString("SaveDialog.jLabel1.onlineText"));
            saveButton.setText(java.util.ResourceBundle.getBundle("properties/Bundle").getString("SaveDialog.jButton3.textOnline"));
        }
        this.toFront();
        setVisible(true);
        
        
        

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

        button1 = new javax.swing.JButton();
        button2 = new javax.swing.JButton();
        button3 = new javax.swing.JButton();
        button4 = new javax.swing.JButton();
        button5 = new javax.swing.JButton();
        button6 = new javax.swing.JButton();
        button7 = new javax.swing.JButton();
        button8 = new javax.swing.JButton();
        button9 = new javax.swing.JButton();
        button0 = new javax.swing.JButton();
        buttonQ = new javax.swing.JButton();
        buttonW = new javax.swing.JButton();
        buttonE = new javax.swing.JButton();
        buttonR = new javax.swing.JButton();
        buttonT = new javax.swing.JButton();
        buttonY = new javax.swing.JButton();
        buttonU = new javax.swing.JButton();
        buttonI = new javax.swing.JButton();
        buttonO = new javax.swing.JButton();
        buttonP = new javax.swing.JButton();
        buttonA = new javax.swing.JButton();
        buttonS = new javax.swing.JButton();
        buttonD = new javax.swing.JButton();
        buttonF = new javax.swing.JButton();
        buttonG = new javax.swing.JButton();
        buttonH = new javax.swing.JButton();
        buttonJ = new javax.swing.JButton();
        buttonK = new javax.swing.JButton();
        buttonL = new javax.swing.JButton();
        buttonZ = new javax.swing.JButton();
        buttonX = new javax.swing.JButton();
        buttonC = new javax.swing.JButton();
        buttonV = new javax.swing.JButton();
        buttonB = new javax.swing.JButton();
        buttonN = new javax.swing.JButton();
        buttonM = new javax.swing.JButton();
        buttonPoint = new javax.swing.JButton();
        buttonSpace = new javax.swing.JButton();
        buttonDotCom = new javax.swing.JButton();
        buttonDotDe = new javax.swing.JButton();
        buttonAt = new javax.swing.JButton();
        buttonComma = new javax.swing.JButton();
        buttonUnderline = new javax.swing.JButton();
        backspaceButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        shiftButton = new javax.swing.JToggleButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(50, 0), new java.awt.Dimension(50, 0), new java.awt.Dimension(40, 32767));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(50, 0), new java.awt.Dimension(50, 0), new java.awt.Dimension(40, 32767));
        buttonMinus = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(400, 800));
        setUndecorated(true);
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {50, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 50};
        getContentPane().setLayout(layout);

        button1.setBackground(new java.awt.Color(102, 102, 102));
        button1.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        button1.setText("1");
        button1.setContentAreaFilled(false);
        button1.setMaximumSize(new java.awt.Dimension(45, 65));
        button1.setMinimumSize(new java.awt.Dimension(45, 65));
        button1.setOpaque(true);
        button1.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(button1, gridBagConstraints);

        button2.setBackground(new java.awt.Color(102, 102, 102));
        button2.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        button2.setText("2");
        button2.setContentAreaFilled(false);
        button2.setMaximumSize(new java.awt.Dimension(45, 65));
        button2.setMinimumSize(new java.awt.Dimension(45, 65));
        button2.setOpaque(true);
        button2.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(button2, gridBagConstraints);

        button3.setBackground(new java.awt.Color(102, 102, 102));
        button3.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        button3.setText("3");
        button3.setContentAreaFilled(false);
        button3.setMaximumSize(new java.awt.Dimension(45, 65));
        button3.setMinimumSize(new java.awt.Dimension(45, 65));
        button3.setOpaque(true);
        button3.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(button3, gridBagConstraints);

        button4.setBackground(new java.awt.Color(102, 102, 102));
        button4.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        button4.setText("4");
        button4.setContentAreaFilled(false);
        button4.setMaximumSize(new java.awt.Dimension(45, 65));
        button4.setMinimumSize(new java.awt.Dimension(45, 65));
        button4.setOpaque(true);
        button4.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(button4, gridBagConstraints);

        button5.setBackground(new java.awt.Color(102, 102, 102));
        button5.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        button5.setText("5");
        button5.setContentAreaFilled(false);
        button5.setMaximumSize(new java.awt.Dimension(45, 65));
        button5.setMinimumSize(new java.awt.Dimension(45, 65));
        button5.setOpaque(true);
        button5.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(button5, gridBagConstraints);

        button6.setBackground(new java.awt.Color(102, 102, 102));
        button6.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        button6.setText("6");
        button6.setContentAreaFilled(false);
        button6.setMaximumSize(new java.awt.Dimension(45, 65));
        button6.setMinimumSize(new java.awt.Dimension(45, 65));
        button6.setOpaque(true);
        button6.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(button6, gridBagConstraints);

        button7.setBackground(new java.awt.Color(102, 102, 102));
        button7.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        button7.setText("7");
        button7.setContentAreaFilled(false);
        button7.setMaximumSize(new java.awt.Dimension(45, 65));
        button7.setMinimumSize(new java.awt.Dimension(45, 65));
        button7.setOpaque(true);
        button7.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(button7, gridBagConstraints);

        button8.setBackground(new java.awt.Color(102, 102, 102));
        button8.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        button8.setText("8");
        button8.setContentAreaFilled(false);
        button8.setMaximumSize(new java.awt.Dimension(45, 65));
        button8.setMinimumSize(new java.awt.Dimension(45, 65));
        button8.setOpaque(true);
        button8.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(button8, gridBagConstraints);

        button9.setBackground(new java.awt.Color(102, 102, 102));
        button9.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        button9.setText("9");
        button9.setContentAreaFilled(false);
        button9.setMaximumSize(new java.awt.Dimension(45, 65));
        button9.setMinimumSize(new java.awt.Dimension(45, 65));
        button9.setOpaque(true);
        button9.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(button9, gridBagConstraints);

        button0.setBackground(new java.awt.Color(102, 102, 102));
        button0.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        button0.setText("0");
        button0.setContentAreaFilled(false);
        button0.setMaximumSize(new java.awt.Dimension(45, 65));
        button0.setMinimumSize(new java.awt.Dimension(45, 65));
        button0.setOpaque(true);
        button0.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(button0, gridBagConstraints);

        buttonQ.setBackground(new java.awt.Color(102, 102, 102));
        buttonQ.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonQ.setText("q");
        buttonQ.setContentAreaFilled(false);
        buttonQ.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonQ.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonQ.setOpaque(true);
        buttonQ.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonQ, gridBagConstraints);

        buttonW.setBackground(new java.awt.Color(102, 102, 102));
        buttonW.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonW.setText("w");
        buttonW.setContentAreaFilled(false);
        buttonW.setMaximumSize(new java.awt.Dimension(55, 65));
        buttonW.setMinimumSize(new java.awt.Dimension(55, 65));
        buttonW.setOpaque(true);
        buttonW.setPreferredSize(new java.awt.Dimension(55, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonW, gridBagConstraints);

        buttonE.setBackground(new java.awt.Color(102, 102, 102));
        buttonE.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonE.setText("e");
        buttonE.setContentAreaFilled(false);
        buttonE.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonE.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonE.setOpaque(true);
        buttonE.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonE, gridBagConstraints);

        buttonR.setBackground(new java.awt.Color(102, 102, 102));
        buttonR.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonR.setText("r");
        buttonR.setContentAreaFilled(false);
        buttonR.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonR.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonR.setOpaque(true);
        buttonR.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonR, gridBagConstraints);

        buttonT.setBackground(new java.awt.Color(102, 102, 102));
        buttonT.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonT.setText("t");
        buttonT.setContentAreaFilled(false);
        buttonT.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonT.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonT.setOpaque(true);
        buttonT.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonT, gridBagConstraints);

        buttonY.setBackground(new java.awt.Color(102, 102, 102));
        buttonY.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonY.setText("y");
        buttonY.setContentAreaFilled(false);
        buttonY.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonY.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonY.setOpaque(true);
        buttonY.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonY, gridBagConstraints);

        buttonU.setBackground(new java.awt.Color(102, 102, 102));
        buttonU.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonU.setText("u");
        buttonU.setContentAreaFilled(false);
        buttonU.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonU.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonU.setOpaque(true);
        buttonU.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonU, gridBagConstraints);

        buttonI.setBackground(new java.awt.Color(102, 102, 102));
        buttonI.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonI.setText("i");
        buttonI.setContentAreaFilled(false);
        buttonI.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonI.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonI.setOpaque(true);
        buttonI.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 16;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonI, gridBagConstraints);

        buttonO.setBackground(new java.awt.Color(102, 102, 102));
        buttonO.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonO.setText("o");
        buttonO.setContentAreaFilled(false);
        buttonO.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonO.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonO.setOpaque(true);
        buttonO.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 18;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonO, gridBagConstraints);

        buttonP.setBackground(new java.awt.Color(102, 102, 102));
        buttonP.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonP.setText("p");
        buttonP.setContentAreaFilled(false);
        buttonP.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonP.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonP.setOpaque(true);
        buttonP.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonP, gridBagConstraints);

        buttonA.setBackground(new java.awt.Color(102, 102, 102));
        buttonA.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonA.setText("a");
        buttonA.setContentAreaFilled(false);
        buttonA.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonA.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonA.setOpaque(true);
        buttonA.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonA, gridBagConstraints);

        buttonS.setBackground(new java.awt.Color(102, 102, 102));
        buttonS.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonS.setText("s");
        buttonS.setContentAreaFilled(false);
        buttonS.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonS.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonS.setOpaque(true);
        buttonS.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonS, gridBagConstraints);

        buttonD.setBackground(new java.awt.Color(102, 102, 102));
        buttonD.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonD.setText("d");
        buttonD.setContentAreaFilled(false);
        buttonD.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonD.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonD.setOpaque(true);
        buttonD.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonD, gridBagConstraints);

        buttonF.setBackground(new java.awt.Color(102, 102, 102));
        buttonF.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonF.setText("f");
        buttonF.setContentAreaFilled(false);
        buttonF.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonF.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonF.setOpaque(true);
        buttonF.setPreferredSize(new java.awt.Dimension(45, 65));
        buttonF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonFActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonF, gridBagConstraints);

        buttonG.setBackground(new java.awt.Color(102, 102, 102));
        buttonG.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonG.setText("g");
        buttonG.setContentAreaFilled(false);
        buttonG.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonG.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonG.setOpaque(true);
        buttonG.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonG, gridBagConstraints);

        buttonH.setBackground(new java.awt.Color(102, 102, 102));
        buttonH.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonH.setText("h");
        buttonH.setContentAreaFilled(false);
        buttonH.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonH.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonH.setOpaque(true);
        buttonH.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonH, gridBagConstraints);

        buttonJ.setBackground(new java.awt.Color(102, 102, 102));
        buttonJ.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonJ.setText("j");
        buttonJ.setContentAreaFilled(false);
        buttonJ.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonJ.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonJ.setOpaque(true);
        buttonJ.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonJ, gridBagConstraints);

        buttonK.setBackground(new java.awt.Color(102, 102, 102));
        buttonK.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonK.setText("k");
        buttonK.setContentAreaFilled(false);
        buttonK.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonK.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonK.setOpaque(true);
        buttonK.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonK, gridBagConstraints);

        buttonL.setBackground(new java.awt.Color(102, 102, 102));
        buttonL.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonL.setText("l");
        buttonL.setContentAreaFilled(false);
        buttonL.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonL.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonL.setOpaque(true);
        buttonL.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonL, gridBagConstraints);

        buttonZ.setBackground(new java.awt.Color(102, 102, 102));
        buttonZ.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonZ.setText("z");
        buttonZ.setContentAreaFilled(false);
        buttonZ.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonZ.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonZ.setOpaque(true);
        buttonZ.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonZ, gridBagConstraints);

        buttonX.setBackground(new java.awt.Color(102, 102, 102));
        buttonX.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonX.setText("x");
        buttonX.setContentAreaFilled(false);
        buttonX.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonX.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonX.setOpaque(true);
        buttonX.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonX, gridBagConstraints);

        buttonC.setBackground(new java.awt.Color(102, 102, 102));
        buttonC.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonC.setText("c");
        buttonC.setContentAreaFilled(false);
        buttonC.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonC.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonC.setOpaque(true);
        buttonC.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonC, gridBagConstraints);

        buttonV.setBackground(new java.awt.Color(102, 102, 102));
        buttonV.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonV.setText("v");
        buttonV.setContentAreaFilled(false);
        buttonV.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonV.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonV.setOpaque(true);
        buttonV.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonV, gridBagConstraints);

        buttonB.setBackground(new java.awt.Color(102, 102, 102));
        buttonB.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonB.setText("b");
        buttonB.setContentAreaFilled(false);
        buttonB.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonB.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonB.setOpaque(true);
        buttonB.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonB, gridBagConstraints);

        buttonN.setBackground(new java.awt.Color(102, 102, 102));
        buttonN.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonN.setText("n");
        buttonN.setContentAreaFilled(false);
        buttonN.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonN.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonN.setOpaque(true);
        buttonN.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonN, gridBagConstraints);

        buttonM.setBackground(new java.awt.Color(102, 102, 102));
        buttonM.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonM.setText("m");
        buttonM.setContentAreaFilled(false);
        buttonM.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonM.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonM.setOpaque(true);
        buttonM.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 16;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonM, gridBagConstraints);

        buttonPoint.setBackground(new java.awt.Color(102, 102, 102));
        buttonPoint.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonPoint.setText(".");
        buttonPoint.setContentAreaFilled(false);
        buttonPoint.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonPoint.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonPoint.setOpaque(true);
        buttonPoint.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 18;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonPoint, gridBagConstraints);

        buttonSpace.setBackground(new java.awt.Color(102, 102, 102));
        buttonSpace.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonSpace.setContentAreaFilled(false);
        buttonSpace.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonSpace.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonSpace.setOpaque(true);
        buttonSpace.setPreferredSize(new java.awt.Dimension(45, 65));
        buttonSpace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSpaceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonSpace, gridBagConstraints);

        buttonDotCom.setBackground(new java.awt.Color(102, 102, 102));
        buttonDotCom.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        buttonDotCom.setText(".com");
        buttonDotCom.setContentAreaFilled(false);
        buttonDotCom.setMaximumSize(new java.awt.Dimension(95, 65));
        buttonDotCom.setMinimumSize(new java.awt.Dimension(95, 65));
        buttonDotCom.setOpaque(true);
        buttonDotCom.setPreferredSize(new java.awt.Dimension(95, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonDotCom, gridBagConstraints);

        buttonDotDe.setBackground(new java.awt.Color(102, 102, 102));
        buttonDotDe.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        buttonDotDe.setText(".de");
        buttonDotDe.setContentAreaFilled(false);
        buttonDotDe.setMaximumSize(new java.awt.Dimension(80, 65));
        buttonDotDe.setMinimumSize(new java.awt.Dimension(80, 65));
        buttonDotDe.setOpaque(true);
        buttonDotDe.setPreferredSize(new java.awt.Dimension(80, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonDotDe, gridBagConstraints);

        buttonAt.setBackground(new java.awt.Color(102, 102, 102));
        buttonAt.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonAt.setText("@");
        buttonAt.setContentAreaFilled(false);
        buttonAt.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonAt.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonAt.setOpaque(true);
        buttonAt.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        getContentPane().add(buttonAt, gridBagConstraints);

        buttonComma.setBackground(new java.awt.Color(102, 102, 102));
        buttonComma.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonComma.setText(",");
        buttonComma.setContentAreaFilled(false);
        buttonComma.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonComma.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonComma.setOpaque(true);
        buttonComma.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonComma, gridBagConstraints);

        buttonUnderline.setBackground(new java.awt.Color(102, 102, 102));
        buttonUnderline.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonUnderline.setText("_");
        buttonUnderline.setContentAreaFilled(false);
        buttonUnderline.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonUnderline.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonUnderline.setOpaque(true);
        buttonUnderline.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonUnderline, gridBagConstraints);

        backspaceButton.setBackground(new java.awt.Color(102, 102, 102));
        backspaceButton.setFont(new java.awt.Font("Arial Unicode MS", 0, 27)); // NOI18N
        backspaceButton.setText("⌫");
        backspaceButton.setContentAreaFilled(false);
        backspaceButton.setMaximumSize(new java.awt.Dimension(45, 65));
        backspaceButton.setMinimumSize(new java.awt.Dimension(45, 65));
        backspaceButton.setOpaque(true);
        backspaceButton.setPreferredSize(new java.awt.Dimension(45, 65));
        backspaceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backspaceButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        getContentPane().add(backspaceButton, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Noto Sans", 0, 24)); // NOI18N
        jLabel1.setForeground(java.awt.Color.white);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("properties/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("SaveDialog.jLabel1.text_1")); // NOI18N
        jLabel1.setMinimumSize(new java.awt.Dimension(700, 84));
        jLabel1.setName(""); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.insets = new java.awt.Insets(30, 0, 0, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        jTextField2.setBackground(java.awt.Color.lightGray);
        jTextField2.setFont(new java.awt.Font("Noto Sans", 0, 30)); // NOI18N
        jTextField2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTextField2.setPreferredSize(new java.awt.Dimension(400, 50));
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 60;
        getContentPane().add(jTextField2, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setForeground(java.awt.Color.white);
        jLabel2.setText(bundle.getString("SaveDialog.jLabel2.text_1")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 10, 0);
        getContentPane().add(jLabel2, gridBagConstraints);

        saveButton.setBackground(new java.awt.Color(64, 64, 64));
        saveButton.setFont(new java.awt.Font("Noto Sans", 0, 24)); // NOI18N
        saveButton.setForeground(java.awt.Color.white);
        saveButton.setText(bundle.getString("SaveDialog.jButton3.text_1")); // NOI18N
        saveButton.setBorderPainted(false);
        saveButton.setContentAreaFilled(false);
        saveButton.setFocusPainted(false);
        saveButton.setMaximumSize(new java.awt.Dimension(120, 65));
        saveButton.setMinimumSize(new java.awt.Dimension(120, 65));
        saveButton.setOpaque(true);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        getContentPane().add(saveButton, gridBagConstraints);

        cancelButton.setBackground(new java.awt.Color(50, 50, 50));
        cancelButton.setFont(new java.awt.Font("Noto Sans", 0, 24)); // NOI18N
        cancelButton.setForeground(java.awt.Color.white);
        cancelButton.setText(bundle.getString("SaveDialog.jButton5.text_1")); // NOI18N
        cancelButton.setToolTipText(bundle.getString("SaveDialog.jButton5.toolTipText_1")); // NOI18N
        cancelButton.setBorderPainted(false);
        cancelButton.setContentAreaFilled(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setMaximumSize(new java.awt.Dimension(100, 65));
        cancelButton.setMinimumSize(new java.awt.Dimension(100, 65));
        cancelButton.setOpaque(true);
        cancelButton.setPreferredSize(new java.awt.Dimension(106, 28));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        getContentPane().add(cancelButton, gridBagConstraints);

        shiftButton.setBackground(new java.awt.Color(102, 102, 102));
        shiftButton.setBorderPainted(false);
        shiftButton.setContentAreaFilled(false);
        shiftButton.setMaximumSize(new java.awt.Dimension(45, 65));
        shiftButton.setMinimumSize(new java.awt.Dimension(45, 65));
        shiftButton.setOpaque(true);
        shiftButton.setPreferredSize(new java.awt.Dimension(45, 65));
        shiftButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shiftButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(shiftButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 22;
        gridBagConstraints.gridy = 1;
        getContentPane().add(filler1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        getContentPane().add(filler2, gridBagConstraints);

        buttonMinus.setBackground(new java.awt.Color(102, 102, 102));
        buttonMinus.setFont(new java.awt.Font("Tahoma", 0, 34)); // NOI18N
        buttonMinus.setText("-");
        buttonMinus.setContentAreaFilled(false);
        buttonMinus.setMaximumSize(new java.awt.Dimension(45, 65));
        buttonMinus.setMinimumSize(new java.awt.Dimension(45, 65));
        buttonMinus.setOpaque(true);
        buttonMinus.setPreferredSize(new java.awt.Dimension(45, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(buttonMinus, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained

    }//GEN-LAST:event_jTextField2FocusGained

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (onlinePrefs.getBoolean("onlineMode", false)) {
            MailConfig config = new MailConfig(onlinePrefs.get("mail_provider", ""),
                    onlinePrefs.get("mail_username", ""), 
                    onlinePrefs.get("mail_password",""),
                    onlinePrefs.get("mail_mail",""),
                    onlinePrefs.get("mail_message", java.util.ResourceBundle.getBundle("properties/Bundle").getString("MailConfigFrame.editText.text")),
                    onlinePrefs.get("mail_subject", java.util.ResourceBundle.getBundle("properties/Bundle").getString("MailConfigFrame.editSubject.text")), 
                    onlinePrefs.get("mail_encryption","SSL"),
                    onlinePrefs.get("mail_host", ""), 
                    onlinePrefs.getInt("mail_port",0),
                    onlinePrefs.getBoolean("sendWatermarked", true));
            String[] mails = jTextField2.getText().split(",");
            ArrayList<String> mailList = new ArrayList<>();
            ArrayList<String> images = new ArrayList<>();
            for(int i = 0; i < mails.length; i++){
                mailList.add(mails[i]);
                images.add(imageName);
            }
            
            
            
                
            MailWorkerDirect worker = new MailWorkerDirect(mailList, images, new PathCreator().getImagePath(), config, pf);
            worker.execute();
            
        } else {

            try {
                String sophPath = new PathCreator().getImagePath() + "mails.soph";

                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(sophPath, true)));
                String[] mails = jTextField2.getText().split(",");
                Date actDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                String dateFormat = formatter.format(actDate);
                for (String mail : mails) {
                    mail = mail.trim();
                    out.println(imageName + "," + mail + "," + dateFormat +",0");

                }

                out.close();

            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
        //proc.destroy();
        this.dispose();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        
        this.dispose();

    }//GEN-LAST:event_cancelButtonActionPerformed

    private void shiftButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shiftButtonActionPerformed
        if (shiftButton.isSelected()) {
            for (JButton button : getCasesensitiveButtons()) {
                button.setText(button.getText().toUpperCase());
            }
        } else {
            for (JButton button : getCasesensitiveButtons()) {
                button.setText(button.getText().toLowerCase());
            }
        }
        jTextField2.requestFocus();
    }//GEN-LAST:event_shiftButtonActionPerformed

    private void buttonFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonFActionPerformed

    private void buttonSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSpaceActionPerformed

        String text = jTextField2.getText().substring(0, jTextField2.getCaretPosition()) + " " + jTextField2.getText().substring(jTextField2.getCaretPosition());
        int lengthOfRestString = jTextField2.getText().substring(jTextField2.getCaretPosition()).length();
        jTextField2.setText(text);
        jTextField2.requestFocus();

        caretPosition = jTextField2.getText().length() - lengthOfRestString;

        jTextField2.setCaretPosition(caretPosition);

        if (shiftButton.isSelected()) {
            for (JButton button : getCasesensitiveButtons()) {
                button.setText(button.getText().toLowerCase());
                shiftButton.setSelected(false);
            }
        }
    }//GEN-LAST:event_buttonSpaceActionPerformed

    private void backspaceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backspaceButtonActionPerformed

        String text = jTextField2.getText().substring(0, jTextField2.getCaretPosition() - 1) + jTextField2.getText().substring(jTextField2.getCaretPosition());
        int lengthOfRestString = jTextField2.getText().substring(jTextField2.getCaretPosition()).length();
        jTextField2.setText(text);
        jTextField2.requestFocus();

        caretPosition = jTextField2.getText().length() - lengthOfRestString;

        jTextField2.setCaretPosition(caretPosition);

    }//GEN-LAST:event_backspaceButtonActionPerformed


    private List<JButton> getCasesensitiveButtons() {
        List<JButton> list = new ArrayList<>();

        list.add(buttonQ);
        list.add(buttonW);
        list.add(buttonE);
        list.add(buttonR);
        list.add(buttonT);
        list.add(buttonY);
        list.add(buttonU);
        list.add(buttonI);
        list.add(buttonO);
        list.add(buttonP);
        list.add(buttonA);
        list.add(buttonS);
        list.add(buttonD);
        list.add(buttonF);
        list.add(buttonG);
        list.add(buttonH);
        list.add(buttonZ);
        list.add(buttonJ);
        list.add(buttonK);
        list.add(buttonL);
        list.add(buttonX);
        list.add(buttonC);
        list.add(buttonV);
        list.add(buttonB);
        list.add(buttonN);
        list.add(buttonM);
        return list;
    }

    private List<JButton> getAllButtons() {
        List<JButton> list = new ArrayList<>();
        list.addAll(getKeyBoardButtons());
        
        list.add(saveButton);

        list.add(cancelButton);
        return list;

    }
    
    private List<JButton> getKeyBoardButtons() {
        List<JButton> list = new ArrayList<>();
        list.addAll(getCasesensitiveButtons());
        list.add(button0);
        list.add(button1);
        list.add(button2);
        list.add(button3);
        list.add(button4);
        list.add(button5);
        list.add(button6);
        list.add(button7);
        list.add(button8);
        list.add(button9);
        list.add(buttonPoint);
        list.add(buttonSpace);
        list.add(buttonDotCom);
        list.add(buttonMinus);
        list.add(buttonDotDe);
        list.add(buttonAt);
        list.add(buttonComma);
        list.add(buttonUnderline);
        list.add(backspaceButton);

        return list;

    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backspaceButton;
    private javax.swing.JButton button0;
    private javax.swing.JButton button1;
    private javax.swing.JButton button2;
    private javax.swing.JButton button3;
    private javax.swing.JButton button4;
    private javax.swing.JButton button5;
    private javax.swing.JButton button6;
    private javax.swing.JButton button7;
    private javax.swing.JButton button8;
    private javax.swing.JButton button9;
    private javax.swing.JButton buttonA;
    private javax.swing.JButton buttonAt;
    private javax.swing.JButton buttonB;
    private javax.swing.JButton buttonC;
    private javax.swing.JButton buttonComma;
    private javax.swing.JButton buttonD;
    private javax.swing.JButton buttonDotCom;
    private javax.swing.JButton buttonDotDe;
    private javax.swing.JButton buttonE;
    private javax.swing.JButton buttonF;
    private javax.swing.JButton buttonG;
    private javax.swing.JButton buttonH;
    private javax.swing.JButton buttonI;
    private javax.swing.JButton buttonJ;
    private javax.swing.JButton buttonK;
    private javax.swing.JButton buttonL;
    private javax.swing.JButton buttonM;
    private javax.swing.JButton buttonMinus;
    private javax.swing.JButton buttonN;
    private javax.swing.JButton buttonO;
    private javax.swing.JButton buttonP;
    private javax.swing.JButton buttonPoint;
    private javax.swing.JButton buttonQ;
    private javax.swing.JButton buttonR;
    private javax.swing.JButton buttonS;
    private javax.swing.JButton buttonSpace;
    private javax.swing.JButton buttonT;
    private javax.swing.JButton buttonU;
    private javax.swing.JButton buttonUnderline;
    private javax.swing.JButton buttonV;
    private javax.swing.JButton buttonW;
    private javax.swing.JButton buttonX;
    private javax.swing.JButton buttonY;
    private javax.swing.JButton buttonZ;
    private javax.swing.JButton cancelButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton saveButton;
    private javax.swing.JToggleButton shiftButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        List<JButton> notNormalButtons = new ArrayList<>();
        notNormalButtons.add(saveButton);
        notNormalButtons.add(cancelButton);
        notNormalButtons.add(buttonSpace);

        notNormalButtons.add(backspaceButton);
        System.out.println("test");

        if (!notNormalButtons.contains(e.getSource()) && e.getSource() instanceof JButton) {
            JButton pressedButton = (JButton) e.getSource();
            String text = jTextField2.getText().substring(0, jTextField2.getCaretPosition()) + pressedButton.getText() + jTextField2.getText().substring(jTextField2.getCaretPosition());
            int lengthOfRestString = jTextField2.getText().substring(jTextField2.getCaretPosition()).length();
            jTextField2.setText(text);
            jTextField2.requestFocus();
            
            caretPosition = jTextField2.getText().length() - lengthOfRestString;

            jTextField2.setCaretPosition(caretPosition);

            if (shiftButton.isSelected()) {
                for (JButton button : getCasesensitiveButtons()) {
                    button.setText(button.getText().toLowerCase());
                    shiftButton.setSelected(false);
                }
            }
        }

    }
}
