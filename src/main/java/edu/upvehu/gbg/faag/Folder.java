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

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author German Bordel
 */
class Folder implements Serializable, Comparable<Folder>, Cloneable{
    private static final Logger LOG = Logger.getLogger(Folder.class.getName());

    String label, folder;
    Color bgColor, textColor;
    int x, y, width, height;

    Folder(String label, String folder, Color bgColor, Color textColor, int x, int y) {
        this.label = label;
        this.folder = folder;
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.x = x;
        this.y = y;
    }

    @Override
    protected Folder clone() throws CloneNotSupportedException {
        return (Folder)super.clone(); 
    }
        
    String toXML(){
        return "<Folder"
                //+ " label=\""+StringEscapeUtils.escapeXml11(folder.substring(folder.lastIndexOf("\\")+1))+"\""
                + " label=\""+StringEscapeUtils.escapeXml(label)+"\""
                + " folder=\""+StringEscapeUtils.escapeXml(folder.replace('\\', '/'))+"\""
                + " bgColor=\""+NamedColor.getColorNameorHEx(bgColor)+"\""
                + " textColor=\""+NamedColor.getColorNameorHEx(textColor)+"\""
                + " x=\""+x+"\""
                + " y=\""+y+"\""
                +" />";
                }

    @Override
    public int compareTo(Folder o) {
        return folder.compareTo(o.folder);
    }

    public boolean isInside(Point p) {
        return x < p.getX() && y < p.getY() && x + width > p.getX() && y + height > p.getY();
    }
    
    int generationalGap(Folder relative) {
        String thisFolder=folder, relativeFolder=relative.folder;
        while (thisFolder.endsWith("/")) thisFolder=thisFolder.substring(0,thisFolder.length()-1);
        while (relativeFolder.endsWith("/")) relativeFolder=relativeFolder.substring(0,relativeFolder.length()-1);
        if (relativeFolder.startsWith(thisFolder)) return relativeFolder.substring(thisFolder.length()).split("/").length-1;
        else if (thisFolder.startsWith(relativeFolder)) return -(thisFolder.substring(relativeFolder.length()).split("/").length-1);
        else return Integer.MIN_VALUE;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.label);
        hash = 71 * hash + Objects.hashCode(this.folder);
        hash = 71 * hash + Objects.hashCode(this.bgColor);
        hash = 71 * hash + Objects.hashCode(this.textColor);
        hash = 71 * hash + this.x;
        hash = 71 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Folder other = (Folder) obj;
        if (x != other.x || y != other.y
                || !Objects.equals(this.label, other.label)
                || !Objects.equals(this.folder, other.folder)
                || !Objects.equals(this.bgColor, other.bgColor)
                || !Objects.equals(this.textColor, other.textColor))
            return false;
        return true;
    }
    
}
