/*
 * The MIT License
 *
 * Copyright 2017 Germán Bordel.

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

    //TODO implement undo/redo

/**
 *
 * @author German Bordel
 */
public class Faag {
    static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String LOG_FILENAME=System.getProperty("user.home").replace('\\','/')+"/AppData/Local/Faag/FAAG_log.txt";

     /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        logger.setLevel(Level.INFO);
        try {
          FileHandler fh=new FileHandler(LOG_FILENAME);
          fh.setFormatter(new SimpleFormatter());
          logger.addHandler(fh);
        } catch (IOException | SecurityException ignore) { /*if no logger, error messages will be shown in Dialog windows*/  }

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
            java.util.logging.Logger.getLogger(FaagGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //Time for the splash screen
        try {Thread.sleep(1500);} catch (InterruptedException ignore) {}
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FaagGUI.newInstance();
                } catch (Exception ex) {
                    logger.logp(Level.SEVERE, "Faag.java", "main", "Unable to run the GUI", ex);
                }
            }
        });
    }
}

class Info {
    static String info=null;
    private Info(){};
    static String getInfo() {
        if (info!= null) return info;

         try { 
            BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("html/info.html")));
            {info="";String linea;while ((linea=reader.readLine())!=null) info+=linea+"\n";}
            return info;
        } catch (IOException | NullPointerException ex) {
           Faag.logger.logp(Level.SEVERE, "Info.java", "getInfo", "Unable to get the info file", ex);
           ex.printStackTrace();
           return "<html><head><title>ERROR</title> </head><body><h1>Folders-at-a-glance</h1><h2>Sorry, Error reading the info file.</h2></body></html>";
        }
    }

    //Path.get(URI) does not work properly for this case, so this is a workaroud
    static Path uriToPath(URI uri) throws URISyntaxException {
    String scheme = uri.getScheme();
        // only support legacy JAR URL syntax  jar:{uri}!/{entry} for now
        String spec = uri.getRawSchemeSpecificPart();
        System.out.println(spec);
        int sep = spec.indexOf("!/");
        if (sep != -1) spec = spec.substring(0, sep);
        return Paths.get(new URI(spec.substring(1))).toAbsolutePath();
    }
}