/*
 * The MIT License
 *
 * Copyright 2017 Germán Bordel.
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
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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

    private static final int MARGIN=5;
    private final BasicStroke SOLID_STROKE=new BasicStroke(1.0f);
    private final BasicStroke DASHED_STROKE=new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, new float[]{10.0f}, 0.0f);

    private Set<Folder> folders=new TreeSet<>();
    private Folder grabbedFolder=null, focusedFolder=null;
    private int grabbedXOffset, grabbedYOffset;
    private String searchFolders="";

    /*
        API ====================================================================
    */

    //Getters - setters (wide sense)
    public Set<Folder> getFolders(){ return folders;}
    public void  setFolders(Set<Folder> folders){ this.folders=folders;}
    public Folder getFocusedFolder() {return focusedFolder;}
    public Color getFocusedtFolderBgColor() { return focusedFolder.bgColor; }
    public Color getFocusedtFolderFgColor() { return focusedFolder.textColor; }

    //Constructor
    public FoldersJPanel(){
        Behaviour behaviour=new Behaviour();
        addMouseListener(behaviour);
        addMouseMotionListener(behaviour);
    }

    //Menu options support
    public void hideFolder(){
        folders.remove(focusedFolder);
        repaint();
    }

    public void expandFolder(File[] dirList){
        System.out.println("----------------");
        for (Folder f:folders) System.out.println(f.folder);
        System.out.println("----------------");
        int i=0;
        if (focusedFolder==null) focusedFolder=new Folder("", "", Color.LIGHT_GRAY, Color.BLACK, 20, 20);
        for (File f: dirList) {
            i++;
            Folder newFolder;
            try {
                newFolder = focusedFolder.clone();
                newFolder.folder=f.getCanonicalPath().replace('\\','/');
                newFolder.label=f.getName().replace('\\','/');
                newFolder.x=focusedFolder.x+i*MARGIN;
                newFolder.y=focusedFolder.y+i*MARGIN;
                folders.add(newFolder);
            } catch (CloneNotSupportedException ignore){/*Shouldn't occur*/
            } catch(IOException ex) {FaagGUI.theInstance.popErrorDialog(ex);
            }
        }
        repaint();
    }

    public void hideFolderTree(){
        List<Folder> toHide=new ArrayList<>();
        for (Folder f:folders)
            if (f.folder.startsWith(focusedFolder.folder))
                toHide.add(f);
        folders.removeAll(toHide);
        repaint();
    }

    public void highlightFolders(String s) {
        searchFolders=s.toLowerCase();
        repaint();
    }


    public void setFolderBgColor(Color c) {
        focusedFolder.bgColor=c;
        repaint();
    }

    public void setFolderTextColor(Color c) {
        focusedFolder.textColor=c;
        repaint();
    }

    public void expandFolderStyle() {
        for (Folder f:folders)
            if (focusedFolder.generationalGap(f)>0) {
                f.bgColor=focusedFolder.bgColor;
                f.textColor=focusedFolder.textColor;
            }
        repaint();
    }

    public void addParentFolder() {
        String parentPath=focusedFolder.folder.substring(0,focusedFolder.folder.lastIndexOf("/"));
        String parentLabel=parentPath.substring(parentPath.lastIndexOf("/")+1);
        folders.add(new Folder(parentLabel,parentPath,focusedFolder.bgColor,focusedFolder.textColor,focusedFolder.x-10,focusedFolder.y-10));
        repaint();
    }

    public void renameFolder(String label) {
        focusedFolder.label=label;
        repaint();
    }


    /*
        THE PAINT ==============================================================
    */

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2=(Graphics2D)g;
        g2.setFont(new Font("serif",Font.BOLD,18));
        FontMetrics metrica=g2.getFontMetrics();
        for (Folder f:folders) {
            f.width=metrica.stringWidth(f.label)+2*MARGIN;
            f.height=metrica.getHeight()+2*MARGIN;
        }
        for (Folder f:folders) {  //para cada carpeta f1 buscaremos el ancestro más próximo
            Folder closestAncestor=closestAncestor(f);
            if (closestAncestor!=null) {
                int genGap=closestAncestor.generationalGap(f);
                if (genGap>Integer.MIN_VALUE)
                    drawMiddleArrowedLine(closestAncestor.x+closestAncestor.width/2, closestAncestor.y+closestAncestor.height/2, f.x+f.width/2, f.y+f.height/2, g2,genGap!=1);
            }
        }

        //La representación de carpetas se hace a la inversa para que al seleccionar se pueda elegir la primera encontrada
        List<Folder> fff=new ArrayList<>(folders);
        Comparator cmp = Collections.reverseOrder();
        Collections.sort(fff, cmp);
        g2.setStroke(SOLID_STROKE);
        for (Folder f : fff) {
            g2.setColor(f.bgColor);
            //Si se está agarrando carpeta se aplica blanqueamiento al nodo (con tecla control al subarbol)
            //Si hay busqueda se aplica a los nodos coincidentes

            //Folders are being grabbed
            if (grabbedFolder != null) {
                int gap = grabbedFolder.generationalGap(f);
                if (gap == 0 || (FaagGUI.controlDown && gap > 0)) g2.setColor(Color.white);
                g2.fillRoundRect(f.x, f.y, f.width, f.height, MARGIN, MARGIN);
                g2.setColor((gap == 0 || (FaagGUI.controlDown && gap > 0)) ? Color.BLACK : f.textColor);
                g2.drawString(f.label, f.x + MARGIN, f.y + metrica.getAscent() + MARGIN);
            }
            //If searching folders and this is not a match, overshadow it
            else if (!searchFolders.isEmpty() && !f.label.toLowerCase().contains(searchFolders)) {
                g2.setColor(Color.black);
                g2.fillRoundRect(f.x, f.y, f.width, f.height, MARGIN, MARGIN);
                g2.setColor(Color.lightGray);
                g2.drawString(f.label, f.x + MARGIN, f.y + metrica.getAscent() + MARGIN);
            }
            //Normal representation
            else {
                g2.fillRoundRect(f.x, f.y, f.width, f.height, MARGIN, MARGIN);
                g2.setColor(f.textColor);
                g2.drawString(f.label, f.x + MARGIN, f.y + metrica.getAscent() + MARGIN);
            }
            //paint a border
            if (grabbedFolder != null) {
                int gap = grabbedFolder.generationalGap(f);
                if (gap == 0 || (FaagGUI.controlDown && gap > 0)) g2.setStroke(new BasicStroke(3));
            }
            g2.setColor(Color.gray);
            g2.drawRoundRect(f.x, f.y, f.width, f.height, MARGIN, MARGIN);
        }
    }

    /*
        AUXILIARY FUNCTIONS ====================================================
    */
    private Folder closestAncestor(Folder f) {
        Folder ancestor=null;
        int genGap, minGenGap=Integer.MAX_VALUE;
        for (Folder ancestorCandidate:folders) {
            if (f==ancestorCandidate) continue;
            if ((genGap=ancestorCandidate.generationalGap(f))>0 && genGap<minGenGap) {
                    minGenGap=genGap;
                    ancestor=ancestorCandidate;
                }
        }
        return ancestor;
    }

    private void drawMiddleArrowedLine(int x1, int y1, int x2, int y2, Graphics2D g2d, boolean dashed){
        g2d.setStroke(dashed
            ?DASHED_STROKE
            :SOLID_STROKE
        );
        g2d.drawLine(x1,y1,x2,y2);
        AffineTransform tx = new AffineTransform();
        tx.translate((x1+x2)/2,(y1+y2)/2);
        tx.rotate((Math.atan2(y2-y1, x2-x1)-Math.PI/2d));
        Point[] arrowhead={new Point(0,5),new Point(-5,-5),new Point(5,-5)};
        tx.transform(arrowhead, 0, arrowhead, 0, 3);
        Polygon pol=new Polygon();
        for (Point p:arrowhead)pol.addPoint(p.x, p.y);
        g2d.fill(pol);
    }

    /*
        BEHAVIOUR ==============================================================
    */

 class Behaviour implements MouseListener, MouseMotionListener {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e)) return;
                for (Folder f : folders)
                    if (f.x < e.getX() && f.y < e.getY() && f.x + f.width > e.getX() && f.y + f.height > e.getY()) {
                          try {
                              focusedFolder=f;
                              Desktop.getDesktop().open(new File(f.folder));
                              FaagGUI.theInstance.setState(JFrame.ICONIFIED);
                          } catch (NullPointerException ex) {
                              JOptionPane.showMessageDialog(null, "No directory to open", "MyFolders", JOptionPane.ERROR_MESSAGE);
                          } catch (IllegalArgumentException ex) {
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
                Point point=e.getPoint();
                for (Folder f : folders) if (f.isInside(point)) {
                    focusedFolder=f;
                    break;
                }

                if (focusedFolder==null) {
                    FaagGUI.theInstance.popupMenuBackgroud();
                    return;
                }

                FaagGUI.theInstance.popupMenuFolder();

                if (SwingUtilities.isLeftMouseButton(e)) {
                    grabbedFolder = focusedFolder;
                    grabbedXOffset = point.x;
                    grabbedYOffset = point.y;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                 if (SwingUtilities.isLeftMouseButton(e)) grabbedFolder=null;
                 repaint();
            }


            @Override
            public void mouseDragged(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e)) return;
                if (grabbedFolder != null) {
                    if (FaagGUI.controlDown) {
                        for (Folder f:folders) if (grabbedFolder.generationalGap(f)>=0) {
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

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
 }
}