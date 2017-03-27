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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

    //TODO implement undo/redo
    
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
            java.util.logging.Logger.getLogger(FaagGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //Time for the splash screen
        try {Thread.sleep(2000);} catch (InterruptedException ignore) {}
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FaagGUI.newInstance();
                } catch (Exception ex) {
                    System.err.println("Sorry, something went wrong");
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
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
            return  info = new String(Files.readAllBytes(Paths.get(Info.class.getResource("/html/info.html").toURI())), StandardCharsets.UTF_8);
        } catch (URISyntaxException | java.nio.file.FileSystemNotFoundException | IOException  ex) {
           ex.printStackTrace();
           return "<html><head><title>ERROR</title> </head><body><h1>Folders-at-a-glance</h1><h2>Sorry, Error reading the info file.</h2></body></html>";
        }
        
        
        
        
//        try {
//            System.out.println(Info.class.getClassLoader().getResourceAsStream("/html/info.html"));
//            BufferedReader br=new BufferedReader(new InputStreamReader(Info.class.getr.getClassLoader().getResourceAsStream("/html/info.html")));
//            StringBuffer sb=new StringBuffer();
//            for(String linea=br.readLine();linea!=null;linea=br.readLine()) sb.append(linea);
//            return sb.toString();
//        } catch (IOException  ex) {
//           return "<html><head><title>ERROR</title> </head><body><h1>Folders-at-a-glance</h1><h2>Sorry, Error reading the info file.</h2></body></html>";
//        }
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