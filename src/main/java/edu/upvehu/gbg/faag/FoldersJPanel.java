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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;



public class FoldersJPanel extends JPanel{
    
    static final int MARGEN=5;
    final static BasicStroke NON_DASHED =
            new BasicStroke(1.0f);
    final static BasicStroke DASHED =
            new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, new float[]{10.0f}, 0.0f);
    //List<Folder> folders=new ArrayList<>();
    Set<Folder> folders=new TreeSet<>();
    private Folder grabbedFolder=null, focusedFolder=null;
    private int grabbedXOffset, grabbedYOffset;
    private String searchFolders="";
    
    Polygon arrowHead = new Polygon();  

AffineTransform tx = new AffineTransform();
    
    
    
    public FoldersJPanel(){
        //TODO ver si el triangulo puede declararse como campo
        arrowHead.addPoint( 0,5);
        arrowHead.addPoint( -5, -5);
        arrowHead.addPoint( 5,-5);
        
        
        
       
        
        
          addMouseListener(new MouseAdapter() {
            @Override
              public void mouseClicked(MouseEvent e) {
                  
                  
                  if (!SwingUtilities.isLeftMouseButton(e)) return;
                  for (Folder f : folders)
                      if (f.x < e.getX() && f.y < e.getY() && f.x + f.width > e.getX() && f.y + f.height > e.getY()) {
                          try {
                              focusedFolder=f;
                              Desktop.getDesktop().open(new File(f.folder));
                              MyFolders.theInstance.setState(JFrame.ICONIFIED);
                              
                          } catch (NullPointerException ex) {
                              JOptionPane.showMessageDialog(null, "No directory to open", "MyFolders", JOptionPane.ERROR_MESSAGE);
                          } catch (IllegalArgumentException ex) {
                              //JOptionPane.showMessageDialog(null, "The directory does not exist", "MyFolders", JOptionPane.ERROR_MESSAGE);
                            String[] options={"Yes","No"};  
                            int PromptResult = JOptionPane.showOptionDialog(null,
                            "<html>The directory does not exist.<br/>Discard it and all their subfolders?",
                            "The directory does not exist.",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                            if (PromptResult == JOptionPane.YES_OPTION) hideFolderTree();
                          } catch (UnsupportedOperationException ex) {
                              JOptionPane.showMessageDialog(null, "Your computer does not support this action", "MyFolders", JOptionPane.ERROR_MESSAGE);
                          } catch (IOException ex) {
                              JOptionPane.showMessageDialog(null, "No app to open the directory (or it fails)", "MyFolders", JOptionPane.ERROR_MESSAGE);
                          } catch (SecurityException ex) {
                              JOptionPane.showMessageDialog(null, "Can't open due to a security policy", "MyFolders", JOptionPane.ERROR_MESSAGE);
                          }
                          break;
                      }
              }
        
            @Override
            public void mousePressed(MouseEvent e) {
              
                  
                  focusedFolder=null;
                  for (Folder f : folders) if (f.isInside(e.getPoint())) {
                  focusedFolder=f;
                  break;
                  }
                  
                  if (focusedFolder==null) {
                       MyFolders.theInstance.popupMenuBackgroud();
                      return;
                  }
                
                  MyFolders.theInstance.popupMenuFolder();
                  
                  if (SwingUtilities.isLeftMouseButton(e)) {
                      grabbedFolder = focusedFolder;
                      grabbedXOffset = e.getX();
                      grabbedYOffset = e.getY();  
                  }
                      
                      
                      
               
                      
                  
//                  if (SwingUtilities.isLeftMouseButton(e)) {
//                  for (Folder f : folders) if (f.isInside(e.getPoint())) {grabbedFolder = f; break;}
////                  if (f.x < e.getX() && f.y < e.getY() && f.x + f.width > e.getX() && f.y + f.height > e.getY())  folderGrabbed = f;
//                  grabbedXOffset = e.getX();
//                  grabbedYOffset = e.getY();
//              }
//              if (SwingUtilities.isRightMouseButton(e)) {
//                 
//              for (Folder f : folders)
//                  if (f.isInside(e.getPoint())) {
////                  if (f.x < e.getX() && f.y < e.getY() && f.x + f.width > e.getX() && f.y + f.height > e.getY()) {
//                     setComponentPopupMenu(MyFolders.theInstance.getItemMenu());  //-----------------------------------
//                     focusedFolder=f;
//                     return;
//                  }
//                setComponentPopupMenu(MyFolders.theInstance.getBgMenu());         //-------------------------------------------------
//                focusedFolder=null;
//              }   
//              repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                 
                 if (SwingUtilities.isLeftMouseButton(e)) grabbedFolder=null;
                 repaint();
            }

        });
        
        addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent e) {
                
                if (!SwingUtilities.isLeftMouseButton(e)) return;
                if (grabbedFolder != null) {
//                    if (MyFolders.teclado.isControlDown()) {
                    if (MyFolders.controlDown) {
                        for (Folder f:folders) if (generationGap(f.folder, grabbedFolder.folder)>=0) {
                            f.x += e.getX() - grabbedXOffset;
                            f.y += e.getY() - grabbedYOffset;
                        }}
                    else {
                        grabbedFolder.x += e.getX() - grabbedXOffset;
                        grabbedFolder.y += e.getY() - grabbedYOffset;
                        }
                    grabbedXOffset = e.getX();
                    grabbedYOffset = e.getY();
                } 
                repaint();
            }
            
        });
    }
    
    public void add(Folder f) {
        folders.add(f);
    }

    public Folder getFocusedFolder() {
        return focusedFolder;
    }
    
    

    @Override
    public void paint(Graphics g) {
        super.paint(g); 
        Graphics2D g2=(Graphics2D)g;
        g2.setFont(new Font("serif",Font.BOLD,18));
        FontMetrics metrica=g2.getFontMetrics();
        for (Folder f:folders) {
            f.width=metrica.stringWidth(f.label)+2*MARGEN;
            f.height=metrica.getHeight()+2*MARGEN;
        }
        AffineTransform savedAT = g2.getTransform();
        for (Folder f:folders) {  //para cada carpeta f1 buscaremos el ancestro más próximo
            Folder ancestor=null;
            int minDiffGen=Integer.MAX_VALUE;
            for (Folder ancestorCandidate:folders) { //f2 todos los candidatos a ancestro
                if (f==ancestorCandidate) continue; //Si es la misma no es ancestro
                int diffGen=generationGap(f.folder,ancestorCandidate.folder);
                if (diffGen==-1) continue;
                if (diffGen<minDiffGen) { //si hemos consegido un mínimo nos lo quedamos
                        minDiffGen=diffGen;
                        ancestor=ancestorCandidate;
                    }
            }
            if (minDiffGen<Integer.MAX_VALUE) {
                int x2=f.x+f.width/2, y2=f.y+f.height/2, x1=ancestor.x+ancestor.width/2, y1=ancestor.y+ancestor.height/2;
                g2.setStroke(minDiffGen==1?NON_DASHED:DASHED);
                g2.drawLine(x1,y1,x2,y2);
                tx.setToIdentity();
                tx.translate((x1+x2)/2,(y1+y2)/2);
                tx.rotate((Math.atan2(y2-y1, x2-x1)-Math.PI/2d));  
                g2.setTransform(tx);   
                g2.fill(arrowHead);
                g2.setTransform(savedAT);
            }
        }
        
        
        //La representación de carpetas se hace a la inversa para que al seleccionar se pueda elegir la primera encontrada
        List<Folder> fff=new ArrayList<>(folders);
        Comparator cmp = Collections.reverseOrder();  
        Collections.sort(fff, cmp); 
        for (Folder f : fff) {
            //g2.setColor(f.bgColor.darker());
            g2.setColor(f.bgColor);
            //Si se está agarrando carpeta se aplica blanqueamiento al nodo (con tecla control al subarbol)
            //Si hay busqueda se aplica a los nodos coincidentes
            
            
            //Folders are being grabbed
            if (grabbedFolder != null) {
                int gap = generationGap(f.folder, grabbedFolder.folder);
                if (gap == 0 || (MyFolders.controlDown && gap > 0)) g2.setColor(Color.white);
                g2.fillRoundRect(f.x, f.y, f.width, f.height, MARGEN, MARGEN);
                g2.setColor((gap == 0 || (MyFolders.controlDown && gap > 0)) ? Color.BLACK : f.textColor);
                g2.drawString(f.label, f.x + MARGEN, f.y + metrica.getAscent() + MARGEN);
            } 
            //If searching folders and this is not a match, overshadow it 
            else if (!searchFolders.isEmpty() && !f.label.toLowerCase().contains(searchFolders)) {
                g2.setColor(Color.black);
                g2.fillRoundRect(f.x, f.y, f.width, f.height, MARGEN, MARGEN);
                g2.setColor(Color.lightGray);
                g2.drawString(f.label, f.x + MARGEN, f.y + metrica.getAscent() + MARGEN);
            }
            //Normal representation
            else {
                g2.fillRoundRect(f.x, f.y, f.width, f.height, MARGEN, MARGEN);
                g2.setColor(f.textColor);
                g2.drawString(f.label, f.x + MARGEN, f.y + metrica.getAscent() + MARGEN);
            }
            //paint a border
            if (grabbedFolder != null) {
                int gap = generationGap(f.folder, grabbedFolder.folder);
                if (gap == 0 || (MyFolders.controlDown && gap > 0)) g2.setStroke(new BasicStroke(3));
            }
            g2.setColor(Color.gray);
            g2.drawRoundRect(f.x, f.y, f.width, f.height, MARGEN, MARGEN);
        }
    }
        
    int generationGap(String descendiente, String ascendiente) {
        if (!descendiente.startsWith(ascendiente)) return -1; //Si no es prefijo, no es ancestro
        String[] f1Splitted=descendiente.split("\\\\");
        String[] f2Splitted=ascendiente.split("\\\\");
        int i=0;for (;i<f2Splitted.length;i++) if (!f1Splitted[i].equals(f2Splitted[i])) break;  
        if (i==f2Splitted.length)  //Si se han coincidido todas las carpetas de path2 es ancestro
             return f1Splitted.length-f2Splitted.length; //el nivel de "ancestritud" es la diferencia en el número de carpetas en el path
        return -1;
    }
    
    

    Set<Folder> getFolders() {
        return Collections.unmodifiableSet(folders);
    }

    void setFolders(Set<Folder> list) {
        folders=list;
    }
        
    void hideFolder(){
        folders.remove(focusedFolder);
        repaint();
    }
    
    void expandFolder(File[] dirList){
        int i=0;
        for (File f: dirList) {
            i++;
            Folder newFolder;
            try {
                newFolder = focusedFolder.clone();
                newFolder.folder=f.getCanonicalPath();
                newFolder.label=f.getName();
                newFolder.x=focusedFolder.x+i*MARGEN;
                newFolder.y=focusedFolder.y+i*MARGEN;
                folders.add(newFolder);
              
            } catch (CloneNotSupportedException ignore){/*Shouldn't occur*/
            } catch(IOException ex) {MyFolders.theInstance.popErrorDialog(ex);
            }
        }
        repaint();
    }
 
           
    void hideFolderTree(){
        List<Folder> toHide=new ArrayList<>();
        for (Folder f:folders) 
//            Antes la opción solo eliminaba el primer nivel de subcarpetas
//            if (popupMenuFolder!=f && f.folder.startsWith(popupMenuFolder.folder) && f.folder.substring(popupMenuFolder.folder.length()).split("\\\\").length==2)
            if (f.folder.startsWith(focusedFolder.folder))
            toHide.add(f);
        folders.removeAll(toHide);
        repaint();
        
    }
    
    void highlightFolders(String s) {
        searchFolders=s.toLowerCase();
        repaint();
    }

    Color getFolderColor() {
        return focusedFolder.bgColor;
    }

    void setFolderBgColor(Color c) {
        focusedFolder.bgColor=c;
        repaint();
    }

    void setFolderTextColor(Color c) {
        focusedFolder.textColor=c;
        repaint();
    }

    void expandFolderStyle() {
        for (Folder f:folders)
            if (generationGap(f.folder,focusedFolder.folder)>0) {
                f.bgColor=focusedFolder.bgColor;
                f.textColor=focusedFolder.textColor;
            }
        repaint();
    }
}
