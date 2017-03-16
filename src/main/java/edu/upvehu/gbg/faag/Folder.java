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
import java.awt.Point;
import java.io.Serializable;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author German
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
                + " folder=\""+StringEscapeUtils.escapeXml(folder)+"\""
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
    
    
    
    
    
    
    
    
}
