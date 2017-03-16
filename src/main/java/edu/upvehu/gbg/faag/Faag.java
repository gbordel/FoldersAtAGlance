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

/**
 *
 * @author German Bordel
 */
public class Faag {
     /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MyFolders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        try {Thread.sleep(2000);} catch (InterruptedException ignore) {}
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MyFolders.newInstance();
//                theInstance.setLocationByPlatform(true);
//                theInstance.addWindowListener(new WindowAdapter() {
//                    @Override
//                    public void windowClosing(WindowEvent we) {
//                        //    if (teclado.isControlDown()) System.exit(0);
//                        if (controlDown) {
//                            System.exit(0);
//                        }
//                        String ObjButtons[] = {"Yes", "No"};
//                        int PromptResult = JOptionPane.showOptionDialog(theInstance, "Close the app.?", "MyFolders", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
//                        if (PromptResult == JOptionPane.YES_OPTION) {
//                            System.exit(0);
//                        }
//                    }
//                });
//                theInstance.loadFromXML();
//                theInstance.setVisible(true);
//                theInstance.foldersJPanel1.setComponentPopupMenu(theInstance.mainJPopupMenu);
            }
        });
    
    }
}
