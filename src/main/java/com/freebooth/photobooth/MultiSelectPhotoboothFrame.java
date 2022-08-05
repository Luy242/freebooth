
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

import com.freebooth.fileWatcher.ImageDecorator;
import com.freebooth.fileWatcher.PhotoboothFileWatcher;
import com.freebooth.fileWatcher.ThumbDecorator;
import com.freebooth.fileWatcher.WatermarkDecorator;
import com.freebooth.utilities.BILoader;
import com.freebooth.utilities.PathCreator;
import utilities.StretchIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import net.coobird.thumbnailator.Thumbnails;
import com.freebooth.utilities.AltTabStopper;
import com.freebooth.utilities.FileExistenceChecker;

/**
 *
 * @author johannes
 */
public class MultiSelectPhotoboothFrame extends Photobooth {

    int actImageIndex;
    List<String> images;
    List<String> selectedImages;
    Thread tfw;
    String path;
    Preferences prefs;
    AltTabStopper ats;

    /**
     * Creates new form PhotoboothForm
     *
     * @param fw
     */
    public MultiSelectPhotoboothFrame() {

        setUndecorated(true);
        initComponents();

        images = new CopyOnWriteArrayList<>();
        selectedImages = new ArrayList<>();
        actImageIndex = -1;

        //sendButton.setOpaque(true);
        //selectButton.setOpaque(true);
        //cancelButton.setOpaque(true);
        //final PhotoboothFrame frame = this;
        tfw = new Thread(new PhotoboothFileWatcher(new PathCreator().getImagePath(),this, false));
        tfw.start();

        prefs = Preferences.userNodeForPackage(MultiSelectPhotoboothFrame.class);
        Preferences prefsWatermark = Preferences.userNodeForPackage(WatermarkPanel.class);
        sendButton.setBackground(Color.GRAY);
        selectButton.setBackground(Color.decode(prefs.get("button_color", "#ED5E2F")));
        cancelButton.setBackground(Color.GRAY);
        getContentPane().setBackground(Color.decode(prefs.get("background_color", "#333333")));
        System.out.println(prefs.get("background_color", "#333333"));
        sendButton.setForeground(Color.LIGHT_GRAY);
        selectButton.setForeground(Color.decode(prefs.get("button_color", "#ffffff")));
        cancelButton.setForeground(Color.LIGHT_GRAY);
        imageLabel.setForeground(Color.decode(prefs.get("text_color", "#ffffff")));
        selectedCounter.setForeground(Color.decode(prefs.get("text_color", "#ffffff")));
        String dark = prefs.get("dark_icon", "");
        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/next_small3" + dark + ".png")));
        prevButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/previous_small3" + dark + ".png")));
        List<javax.swing.JButton> buttonList = new ArrayList<>();
        buttonList.add(sendButton);
        buttonList.add(cancelButton);
        buttonList.add(selectButton);

        for (javax.swing.JButton button : buttonList) {
            Font font = button.getFont();
            button.setFont(new Font(font.getName(), font.getStyle(), prefs.getInt("button_font_size", 20)));
        }
        path = new PathCreator().getImagePath();
        String labelText = imageLabel.getText();
        String[] lines = labelText.split("<br />");
        labelText = lines[0] + "<br />" + lines[1] + "<br />" + path + "<br />" + lines[2] + "<br />" + lines[3];

        imageLabel.setMaximumSize(new Dimension(prefs.getInt("image_width", 1400), prefs.getInt("image_height", 1400)));
        imageLabel.setMinimumSize(new Dimension(prefs.getInt("image_width", 1400), prefs.getInt("image_height", 1400)));
        imageLabel.setPreferredSize(new Dimension(prefs.getInt("image_width", 1400), prefs.getInt("image_height", 1400)));

        imageLabel.setText(labelText);
        System.out.println(path);
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        ImageDecorator decorator = new WatermarkDecorator(new ThumbDecorator(false));
        for (File file : listOfFiles) {
            if (!file.isDirectory()) {
                String fileName = file.getName();
                String fileEnding = fileName.substring(fileName.lastIndexOf("."));
                boolean allFilesExist = FileExistenceChecker.thumbExists(fileName) && FileExistenceChecker.watermarkExists(fileName);
                File decoratedFile;
                if (file.isFile() && (fileEnding.equals(".jpg") || fileEnding.equals(".JPG"))) {
                    if (allFilesExist) {
                        decoratedFile = decorator.processImage(file, null);
                    } else {
                        BufferedImage image = BILoader.loadImage(path + File.separator + file.getName());
                        decoratedFile = decorator.processImage(file, image);
                    }
                    images.add(decoratedFile.getName());
                }
            }
        }

        // on arrowkeys change pic
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Next"); //$NON-NLS-1$
        getRootPane().getActionMap().put("Next", new AbstractAction() { //$NON-NLS-1$
            public void actionPerformed(ActionEvent e) {
                if (actImageIndex == images.size() - 1) {
                    actImageIndex = 0;
                } else {
                    actImageIndex++;
                }

                updateImage();
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Prev"); //$NON-NLS-1$
        getRootPane().getActionMap().put("Prev", new AbstractAction() { //$NON-NLS-1$
            public void actionPerformed(ActionEvent e) {
                if (actImageIndex == 0) {
                    actImageIndex = images.size() - 1;
                } else {
                    actImageIndex--;
                }

                updateImage();
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "First"); //$NON-NLS-1$
        getRootPane().getActionMap().put("First", new AbstractAction() { //$NON-NLS-1$
            public void actionPerformed(ActionEvent e) {
                actImageIndex = 0;

                updateImage();
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "Last"); //$NON-NLS-1$
        getRootPane().getActionMap().put("Last", new AbstractAction() { //$NON-NLS-1$
            public void actionPerformed(ActionEvent e) {
                actImageIndex = images.size() - 1;

                updateImage();
            }
        });

        actImageIndex = images.size() - 1;
        updateImage();

        if (prefs.getBoolean("leavepw", false)) {
            ats = AltTabStopper.create(this);
        }

    }

    public synchronized void addImage(String path) {
        images.add(path);
        if(!prefs.getBoolean("update_on_new_picture", false)) {
            actImageIndex = images.size() - 1;
            updateImage();
        }
    }

    public synchronized void deleteImage(String path) {
        images.remove(path);

        actImageIndex = images.size() - 1;
        updateImage();
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

        prevButton = new javax.swing.JButton();
        imageLabel = new javax.swing.JLabel();
        nextButton = new javax.swing.JButton();
        sendButton = new javax.swing.JButton();
        imageNumber = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        selectButton = new javax.swing.JButton();
        selectedCounter = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAutoRequestFocus(false);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusable(false);
        setResizable(false);
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0, 10, 0, 10, 0, 10, 0, 10, 0};
        layout.rowHeights = new int[] {0, 10, 0, 10, 0};
        getContentPane().setLayout(layout);

        prevButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/previous_small3.png"))); // NOI18N
        prevButton.setBorderPainted(false);
        prevButton.setContentAreaFilled(false);
        prevButton.setEnabled(false);
        prevButton.setFocusable(false);
        prevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        getContentPane().add(prevButton, gridBagConstraints);

        imageLabel.setFont(new java.awt.Font("Noto Sans", 0, 18)); // NOI18N
        imageLabel.setForeground(java.awt.Color.white);
        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/photobooth_start.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("properties/Bundle"); // NOI18N
        imageLabel.setText(bundle.getString("MultiSelectPhotoboothFrame.imageLabel.text_1")); // NOI18N
        imageLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        imageLabel.setIconTextGap(20);
        imageLabel.setMaximumSize(new java.awt.Dimension(1400, 800));
        imageLabel.setMinimumSize(new java.awt.Dimension(1400, 800));
        imageLabel.setName(""); // NOI18N
        imageLabel.setPreferredSize(new java.awt.Dimension(1400, 800));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.weightx = 200.0;
        gridBagConstraints.weighty = 200.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        getContentPane().add(imageLabel, gridBagConstraints);

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/next_small3.png"))); // NOI18N
        nextButton.setToolTipText(bundle.getString("MultiSelectPhotoboothFrame.nextButton.toolTipText_1")); // NOI18N
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setEnabled(false);
        nextButton.setFocusable(false);
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        getContentPane().add(nextButton, gridBagConstraints);

        sendButton.setBackground(new java.awt.Color(36, 42, 106));
        sendButton.setFont(new java.awt.Font("Noto Sans", 0, 20)); // NOI18N
        sendButton.setForeground(new java.awt.Color(254, 254, 254));
        sendButton.setText(bundle.getString("MultiSelectPhotoboothFrame.sendButton.text_1")); // NOI18N
        sendButton.setBorder(new javax.swing.border.MatteBorder(null));
        sendButton.setBorderPainted(false);
        sendButton.setContentAreaFilled(false);
        sendButton.setEnabled(false);
        sendButton.setFocusPainted(false);
        sendButton.setFocusable(false);
        sendButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        sendButton.setMaximumSize(new java.awt.Dimension(336, 50));
        sendButton.setMinimumSize(new java.awt.Dimension(336, 50));
        sendButton.setOpaque(true);
        sendButton.setPreferredSize(new java.awt.Dimension(336, 50));
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 20);
        getContentPane().add(sendButton, gridBagConstraints);

        imageNumber.setForeground(java.awt.Color.white);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        getContentPane().add(imageNumber, gridBagConstraints);

        cancelButton.setBackground(new java.awt.Color(36, 42, 106));
        cancelButton.setFont(new java.awt.Font("Noto Sans", 0, 20)); // NOI18N
        cancelButton.setForeground(new java.awt.Color(254, 254, 254));
        cancelButton.setText(bundle.getString("MultiSelectPhotoboothFrame.cancelButton.text")); // NOI18N
        cancelButton.setBorder(new javax.swing.border.MatteBorder(null));
        cancelButton.setBorderPainted(false);
        cancelButton.setContentAreaFilled(false);
        cancelButton.setEnabled(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        cancelButton.setMaximumSize(new java.awt.Dimension(336, 50));
        cancelButton.setMinimumSize(new java.awt.Dimension(336, 50));
        cancelButton.setOpaque(true);
        cancelButton.setPreferredSize(new java.awt.Dimension(336, 50));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 20);
        getContentPane().add(cancelButton, gridBagConstraints);

        selectButton.setBackground(new java.awt.Color(36, 42, 106));
        selectButton.setFont(new java.awt.Font("Noto Sans", 0, 20)); // NOI18N
        selectButton.setForeground(new java.awt.Color(254, 254, 254));
        selectButton.setText(bundle.getString("MultiSelectPhotoboothFrame.selectButton.text")); // NOI18N
        selectButton.setBorder(new javax.swing.border.MatteBorder(null));
        selectButton.setBorderPainted(false);
        selectButton.setContentAreaFilled(false);
        selectButton.setFocusPainted(false);
        selectButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        selectButton.setMaximumSize(new java.awt.Dimension(336, 50));
        selectButton.setMinimumSize(new java.awt.Dimension(336, 50));
        selectButton.setOpaque(true);
        selectButton.setPreferredSize(new java.awt.Dimension(336, 50));
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 20);
        getContentPane().add(selectButton, gridBagConstraints);

        selectedCounter.setText(bundle.getString("MultiSelectPhotoboothFrame.selectedCounter.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        getContentPane().add(selectedCounter, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        if (actImageIndex == images.size() - 1) {
            actImageIndex = 0;
        } else {
            actImageIndex++;
        }

        updateImage();
    }//GEN-LAST:event_nextButtonActionPerformed

    private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevButtonActionPerformed
        if (actImageIndex == 0) {
            actImageIndex = images.size() - 1;
        } else {
            actImageIndex--;
        }

        updateImage();

    }//GEN-LAST:event_prevButtonActionPerformed

    public void showAsDisabled() {
        sendButton.setEnabled(false);
        cancelButton.setEnabled(false);
        selectButton.setEnabled(false);
        imageLabel.setEnabled(false);
        nextButton.setEnabled(false);
        prevButton.setEnabled(false);
        sendButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setBackground(Color.LIGHT_GRAY);
        selectButton.setBackground(Color.LIGHT_GRAY);
        sendButton.setForeground(Color.GRAY);
        cancelButton.setForeground(Color.GRAY);
        selectButton.setForeground(Color.GRAY);
        getContentPane().setBackground(java.awt.Color.GRAY);
    }

    public void showAsEnabled() {

        selectButton.setEnabled(true);
        selectButton.setBackground(Color.decode(prefs.get("button_color", "#ED5E2F")));
        selectButton.setForeground(Color.decode(prefs.get("button_foreground", "#ffffff")));
        imageLabel.setEnabled(true);
        nextButton.setEnabled(true);
        prevButton.setEnabled(true);
        Preferences prefs = Preferences.userNodeForPackage(MultiSelectPhotoboothFrame.class);
        getContentPane().setBackground(Color.decode(prefs.get("background_color", "#333333")));
    }

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed

        showAsDisabled();
        if (!Preferences.userNodeForPackage(StartFrame.class).getBoolean("osk", false)) {
            MultiSelectSaveDialog sd = new MultiSelectSaveDialog(this, true, selectedImages, this);

            sd.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    showAsEnabled();
                }

            });
        } else {
            MultiSelectOSKSaveDialog sd = new MultiSelectOSKSaveDialog(this, true, selectedImages, this);
            sd.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    showAsEnabled();
                }

            });
        }
        selectedImages.clear();
        selectedCounter.setText(selectedImages.size() + " " + java.util.ResourceBundle.getBundle("properties/Bundle").getString("MultiSelectPhotoboothFrame.imageCount"));
        imageLabel.setBorder(BorderFactory.createEmptyBorder());
        selectButton.setText(java.util.ResourceBundle.getBundle("properties/Bundle").getString("MultiSelectPhotoboothFrame.selectButton.text"));

    }//GEN-LAST:event_sendButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        selectedImages.clear();
        sendButton.setEnabled(false);
        sendButton.setBackground(Color.LIGHT_GRAY);
        sendButton.setForeground(Color.GRAY);
        cancelButton.setEnabled(false);
        cancelButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.GRAY);
        selectButton.setText(java.util.ResourceBundle.getBundle("properties/Bundle").getString("MultiSelectPhotoboothFrame.selectButton.text"));
        selectedCounter.setText(selectedImages.size() + " " + java.util.ResourceBundle.getBundle("properties/Bundle").getString("MultiSelectPhotoboothFrame.imageCount"));
        imageLabel.setBorder(BorderFactory.createEmptyBorder());

    }//GEN-LAST:event_cancelButtonActionPerformed

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        if (selectedImages.contains(images.get(actImageIndex))) {
            selectedImages.remove(images.get(actImageIndex));
            selectButton.setText(java.util.ResourceBundle.getBundle("properties/Bundle").getString("MultiSelectPhotoboothFrame.selectButton.text"));
            if (selectedImages.size() == 0) {
                sendButton.setEnabled(false);
                sendButton.setBackground(Color.LIGHT_GRAY);
                sendButton.setForeground(Color.GRAY);
                cancelButton.setEnabled(false);
                cancelButton.setBackground(Color.LIGHT_GRAY);
                cancelButton.setForeground(Color.GRAY);

                imageLabel.setBorder(BorderFactory.createEmptyBorder());
            }
        } else {
            selectedImages.add(images.get(actImageIndex));
            sendButton.setEnabled(true);
            sendButton.setBackground(Color.decode(prefs.get("button_color", "#ED5E2F")));
            sendButton.setForeground(Color.decode(prefs.get("button_foreground", "#FFFFFF")));
            cancelButton.setEnabled(true);
            cancelButton.setBackground(Color.decode(prefs.get("button_color", "#ED5E2F")));
            cancelButton.setForeground(Color.decode(prefs.get("button_foreground", "#FFFFFFF")));
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.decode(prefs.get("button_color", "#ED5E2F"))));
            selectButton.setText(java.util.ResourceBundle.getBundle("properties/Bundle").getString("MultiSelectPhotoboothFrame.selectButton.text2"));
        }
        selectedCounter.setText(selectedImages.size() + " " + java.util.ResourceBundle.getBundle("properties/Bundle").getString("MultiSelectPhotoboothFrame.imageCount"));
    }//GEN-LAST:event_selectButtonActionPerformed

    @Override
    public boolean close() {

        setVisible(false); //you can't see me!
        dispose();
        tfw.interrupt();
        if (ats != null) {
            ats.stop();
        }
        return true;
    }

    public void updateImage() {
        //    ImageIO.setUseCache(false);
        //System.out.println(path);
        if (actImageIndex != -1) {
            if (actImageIndex >= 0) {
                //sendButton.setEnabled(true);
                showAsEnabled();
                imageLabel.setText("");
            }
            if (selectedImages.contains(images.get(actImageIndex))) {
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.decode(prefs.get("button_color", "#ED5E2F"))));
                selectButton.setText(java.util.ResourceBundle.getBundle("properties/Bundle").getString("MultiSelectPhotoboothFrame.selectButton.text2"));
            } else {
                imageLabel.setBorder(BorderFactory.createEmptyBorder());
                selectButton.setText(java.util.ResourceBundle.getBundle("properties/Bundle").getString("MultiSelectPhotoboothFrame.selectButton.text"));
            }
            BufferedImage imgBI = null;
            String actPath = "";
            try {
												   
																		
                actPath = path + images.get(actImageIndex);
						
												
				 
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println(images.toString());
            }
            Preferences prefsWatermark = Preferences.userNodeForPackage(WatermarkPanel.class);

            if (prefs.getBoolean("thumbnails", true) && new File(new PathCreator().getThumbPath() + images.get(actImageIndex)).exists()) {
                actPath = new PathCreator().getThumbPath() + images.get(actImageIndex);
            } else if (prefsWatermark.getBoolean("watermark", false) && new File(new PathCreator().getWatermarkPath()+ images.get(actImageIndex)).exists()) {
                actPath = new PathCreator().getWatermarkPath()+ images.get(actImageIndex);
            }

            BufferedImage bi = BILoader.loadImage(actPath);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(bi, "jpg", baos);
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                baos.close();
                StretchIcon si = new StretchIcon(imageInByte);
                imageLabel.setIcon(si);
                imageNumber.setText(actImageIndex + 1 + "/" + images.size());
            } catch (IOException ex) {
                Logger.getLogger(MultiSelectPhotoboothFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (prefs.getBoolean("thumbnails", true) && !new File(new PathCreator().getThumbPath() + images.get(actImageIndex)).exists()) {
                try {
                   Thumbnails.of(new PathCreator().getImagePath() + images.get(actImageIndex)).size(prefs.getInt("image_height", 800), prefs.getInt("image_width", 1400)).toFile(new PathCreator().getThumbPath() + images.get(actImageIndex));
                } catch (IOException ex) {
                    Logger.getLogger(MultiSelectPhotoboothFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

    public BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JLabel imageNumber;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton prevButton;
    private javax.swing.JButton selectButton;
    private javax.swing.JLabel selectedCounter;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables
}
