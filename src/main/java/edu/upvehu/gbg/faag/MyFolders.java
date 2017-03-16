/*
 * The MIT License
 *
 * Copyright 2017 gbord.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package edu.upvehu.gbg.faag;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//TODO que las lineas sean flechas


/**
 *
 * @author German
 */
public class MyFolders extends javax.swing.JFrame {
    
    private static final String CONFIG_FILENAME=System.getProperty("user.home")+"/AppData/Local/MyFolders_Config.xml";
    //TODO controlar los cambios de expansión/borrados de carpetas para sugerir grabar antes de salir.
    //TODO implementar undo/redo
    static MyFolders theInstance=null;
    //static Keychecker teclado = new Keychecker();
    static boolean controlDown;
    
    /**
     * Creates new form MainFrame
     */
    private MyFolders() {
        initComponents();
        setLocationByPlatform(true);
        addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent we) {
                        //    if (teclado.isControlDown()) System.exit(0);
                        if (controlDown) {
                            System.exit(0);
                        }
                        String ObjButtons[] = {"Yes", "No"};
                        int PromptResult = JOptionPane.showOptionDialog(theInstance, "Close the app.?", "MyFolders", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
                        if (PromptResult == JOptionPane.YES_OPTION) {
                            System.exit(0);
                        }
                    }
                });
        loadFromXML();
        setVisible(true);
        foldersJPanel1.setComponentPopupMenu(mainJPopupMenu);
    }

    public static MyFolders newInstance(){
        if (theInstance==null) theInstance=new MyFolders();
        return theInstance;
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainJPopupMenu = new javax.swing.JPopupMenu();
        parentJMenuItem = new javax.swing.JMenuItem();
        renameJMenuItem = new javax.swing.JMenuItem();
        expandJMenuItem = new javax.swing.JMenuItem();
        hideTreeJMenuItem = new javax.swing.JMenuItem();
        hideJMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        bgColorJMenuItem = new javax.swing.JMenuItem();
        fgColorJMenuItem = new javax.swing.JMenuItem();
        expandStyleJjMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        loadJMenuItem = new javax.swing.JMenuItem();
        saveJMenuItem = new javax.swing.JMenuItem();
        jDialog1 = new javax.swing.JDialog();
        jColorChooser1 = new javax.swing.JColorChooser();
        errorJDialog = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        errorMsg = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        highlightJTextField = new javax.swing.JTextField();
        foldersJPanel1 = new edu.upvehu.gbg.faag.FoldersJPanel();

        parentJMenuItem.setText("Display parent folder");
        mainJPopupMenu.add(parentJMenuItem);

        renameJMenuItem.setText("Rename");
        mainJPopupMenu.add(renameJMenuItem);

        expandJMenuItem.setText("Display subfolders");
        expandJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expandJMenuItemActionPerformed(evt);
            }
        });
        mainJPopupMenu.add(expandJMenuItem);

        hideTreeJMenuItem.setText("Hide subfolders");
        hideTreeJMenuItem.setActionCommand("Hide all subfolders");
        hideTreeJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideTreeJMenuItemActionPerformed(evt);
            }
        });
        mainJPopupMenu.add(hideTreeJMenuItem);

        hideJMenuItem.setText("Hide folder");
        hideJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideJMenuItemActionPerformed(evt);
            }
        });
        mainJPopupMenu.add(hideJMenuItem);
        mainJPopupMenu.add(jSeparator1);

        bgColorJMenuItem.setText("Background color");
        bgColorJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bgColorJMenuItemActionPerformed(evt);
            }
        });
        mainJPopupMenu.add(bgColorJMenuItem);

        fgColorJMenuItem.setText("Foregroun color");
        fgColorJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fgColorJMenuItemActionPerformed(evt);
            }
        });
        mainJPopupMenu.add(fgColorJMenuItem);

        expandStyleJjMenuItem.setText("Expand style");
        expandStyleJjMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expandStyleJjMenuItemActionPerformed(evt);
            }
        });
        mainJPopupMenu.add(expandStyleJjMenuItem);
        mainJPopupMenu.add(jSeparator2);

        loadJMenuItem.setText("Load");
        loadJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadActionPerformed(evt);
            }
        });
        mainJPopupMenu.add(loadJMenuItem);

        saveJMenuItem.setText("Save");
        saveJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        mainJPopupMenu.add(saveJMenuItem);

        jDialog1.setModal(true);
        jDialog1.getContentPane().add(jColorChooser1, java.awt.BorderLayout.CENTER);

        errorMsg.setEditable(false);
        errorMsg.setRows(50);
        jScrollPane1.setViewportView(errorMsg);

        errorJDialog.getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("My Folders ((c)2016)");
        setPreferredSize(new java.awt.Dimension(1000, 600));

        jPanel1.setBackground(new java.awt.Color(188, 188, 188));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        highlightJTextField.setColumns(10);
        highlightJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                highlightJTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                highlightJTextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                highlightJTextFieldKeyTyped(evt);
            }
        });
        jPanel1.add(highlightJTextField);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);
        getContentPane().add(foldersJPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadFromXML(){
        try {
            foldersJPanel1.folders.clear();
            SAXParser sp=SAXParserFactory.newInstance().newSAXParser();
            sp.parse(new FileInputStream(CONFIG_FILENAME),new DefaultHandler(){
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                super.startElement(uri, localName, qName, attributes); 
                switch (qName) {
                    case "Folders": setPreferredSize(new Dimension(Integer.parseInt(attributes.getValue("width")), Integer.parseInt(attributes.getValue("height")))); pack(); break;
                    case "Folder": foldersJPanel1.add(new Folder(
                            attributes.getValue("label"),
                            attributes.getValue("folder"),
                            NamedColor.getColorFromName(attributes.getValue("bgColor")),
                            NamedColor.getColorFromName(attributes.getValue("textColor")),
                            Integer.parseInt(attributes.getValue("x")),
                            Integer.parseInt(attributes.getValue("y"))
                            ));break;
                }
            }
           });
           repaint(); 
            
        } catch (Throwable ex) { popErrorDialog(ex);
            //TODO replantearse este hardcode analizando el sistema de ficheros y en general haciendolo más llevadero
            foldersJPanel1.folders.add(new Folder("raíz", "C:\\", Color.yellow, Color.black, 100, 100));
        }
    }
    
    
    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        try (PrintStream ps = new PrintStream(new FileOutputStream(CONFIG_FILENAME));){
           ps.printf("<?xml version=\"1.1\" encoding=\"UTF-8\"?>\n<Folders width=\"%d\" height=\"%d\">\n",getWidth(),getHeight());
           for (Folder f:foldersJPanel1.getFolders()) ps.printf("\t%s\n",f.toXML());
           ps.printf("</Folders>\n");
          } catch (Throwable ex) {popErrorDialog(ex);}
        
    }//GEN-LAST:event_saveActionPerformed

    private void loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadActionPerformed
          loadFromXML();
    }//GEN-LAST:event_loadActionPerformed

    private void hideJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideJMenuItemActionPerformed
        foldersJPanel1.hideFolder();
    }//GEN-LAST:event_hideJMenuItemActionPerformed

    private void expandJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expandJMenuItemActionPerformed
    File[] selected;    
    JFileChooser chooser = new JFileChooser(foldersJPanel1.getFocusedFolder().folder);
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setMultiSelectionEnabled(true);
    int returnVal = chooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
            foldersJPanel1.expandFolder(chooser.getSelectedFiles());
        
    }//GEN-LAST:event_expandJMenuItemActionPerformed

    private void hideTreeJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideTreeJMenuItemActionPerformed
       foldersJPanel1.hideFolderTree();
    }//GEN-LAST:event_hideTreeJMenuItemActionPerformed

    private void highlightJTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_highlightJTextFieldKeyTyped
        String v=highlightJTextField.getText();
        foldersJPanel1.highlightFolders(evt.getKeyChar()=='\u0008' ? v : (v+evt.getKeyChar()));
        controlDown=evt.isControlDown();
    }//GEN-LAST:event_highlightJTextFieldKeyTyped

    private void highlightJTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_highlightJTextFieldKeyPressed
       controlDown=evt.isControlDown();
    }//GEN-LAST:event_highlightJTextFieldKeyPressed

    private void highlightJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_highlightJTextFieldKeyReleased
       controlDown=false;
    }//GEN-LAST:event_highlightJTextFieldKeyReleased

    private void bgColorJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bgColorJMenuItemActionPerformed
      Color newColor = JColorChooser.showDialog(
                     this,
                     "Choose Background Color",
                     foldersJPanel1.getFolderColor());
        if (newColor!=null) foldersJPanel1.setFolderBgColor(newColor);
    }//GEN-LAST:event_bgColorJMenuItemActionPerformed

    private void expandStyleJjMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expandStyleJjMenuItemActionPerformed
        foldersJPanel1.expandFolderStyle();
    }//GEN-LAST:event_expandStyleJjMenuItemActionPerformed

    private void fgColorJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fgColorJMenuItemActionPerformed
         Color newColor = JColorChooser.showDialog(
                     this,
                     "Choose Background Color",
                     foldersJPanel1.getFolderColor());
        if (newColor!=null) foldersJPanel1.setFolderTextColor(newColor);
    }//GEN-LAST:event_fgColorJMenuItemActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem bgColorJMenuItem;
    private javax.swing.JDialog errorJDialog;
    private javax.swing.JTextArea errorMsg;
    private javax.swing.JMenuItem expandJMenuItem;
    private javax.swing.JMenuItem expandStyleJjMenuItem;
    private javax.swing.JMenuItem fgColorJMenuItem;
    private edu.upvehu.gbg.faag.FoldersJPanel foldersJPanel1;
    private javax.swing.JMenuItem hideJMenuItem;
    private javax.swing.JMenuItem hideTreeJMenuItem;
    private javax.swing.JTextField highlightJTextField;
    private javax.swing.JColorChooser jColorChooser1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JMenuItem loadJMenuItem;
    private javax.swing.JPopupMenu mainJPopupMenu;
    private javax.swing.JMenuItem parentJMenuItem;
    private javax.swing.JMenuItem renameJMenuItem;
    private javax.swing.JMenuItem saveJMenuItem;
    // End of variables declaration//GEN-END:variables


    JPopupMenu getPopupMenu() {
        return mainJPopupMenu;
    }
    private static final Logger LOG = Logger.getLogger(MyFolders.class.getName());


    
    void popErrorDialog(Throwable t){
         errorJDialog.setTitle(t.getMessage());
            StringWriter s=new StringWriter();
            t.printStackTrace(new PrintWriter(s));
            errorMsg.setText(s.toString());
            errorJDialog.pack();
            errorJDialog.setLocationRelativeTo(this);
            errorJDialog.setVisible(true);
    }

    void popupMenuFolder() {customizePopupMenu(true);}
    void popupMenuBackgroud() {customizePopupMenu(false);}
    void customizePopupMenu(boolean b) {
        parentJMenuItem.setVisible(b);
        renameJMenuItem.setVisible(b);
        expandJMenuItem.setVisible(b);
        hideJMenuItem.setVisible(b);
        hideTreeJMenuItem.setVisible(b);
        bgColorJMenuItem.setVisible(b);
        fgColorJMenuItem.setVisible(b);
        expandStyleJjMenuItem.setVisible(b);
    }
    
    
}
