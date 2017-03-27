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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author germán bordel
 */
class Config {
    private static final String CONFIG_FILENAME=System.getProperty("user.home").replace('\\','/')+"/AppData/Local/FAAG_Config.xml";
    
    
    int width, height;
    List<Folder> folders;
    
    Config(){ width=height=Integer.MIN_VALUE;}

    public Config(int width, int height, List<Folder> folders) {
        this.width = width;
        this.height = height;
        this.folders = folders;
    }
    
    
    static void loadFromXML(Config config) {
        config.folders=new ArrayList<>();
        try {
            SAXParser sp=SAXParserFactory.newInstance().newSAXParser();
            sp.parse(new FileInputStream(CONFIG_FILENAME),new DefaultHandler(){
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    super.startElement(uri, localName, qName, attributes);
                    switch (qName) {
                        case "Folders":
                            config.width=Integer.parseInt(attributes.getValue("width"));
                            config.height=Integer.parseInt(attributes.getValue("height"));
                            break;
                        case "Folder":
                            config.folders.add(new Folder(
                                    attributes.getValue("label"),
                                    attributes.getValue("folder"),
                                    NamedColor.getColorFromName(attributes.getValue("bgColor")),
                                    NamedColor.getColorFromName(attributes.getValue("textColor")),
                                    Integer.parseInt(attributes.getValue("x")),
                                    Integer.parseInt(attributes.getValue("y"))
                            ));
                            break;
                    }
                }
            });
        } catch (ParserConfigurationException | SAXException | IOException ex) {
        } finally {   
            if (config.width==Integer.MIN_VALUE) config.width=400;
            if (config.height==Integer.MIN_VALUE) config.height=300;
            int x=10, y=10;
            if (config.folders.size()==0) 
                for (File f:File.listRoots())
                    try {
                        config.folders.add(new Folder(f.getCanonicalPath().replace('\\','/'),f.getCanonicalPath().replace('\\','/'),Color.lightGray,Color.black,x+=10,y+=10));
                    } catch (IOException ignore) {}
        }
    }
    
    static void saveToXML(Config config) {            
        String dir=CONFIG_FILENAME.substring(0,CONFIG_FILENAME.lastIndexOf("/"));
        try {
            secureThePath(dir);
        } catch (IOException | SecurityException  ex) {
            FaagGUI.theInstance.popErrorDialog(ex);
            return;
        }
        try (PrintStream ps = new PrintStream(new FileOutputStream(CONFIG_FILENAME));)
        {                                
            ps.printf("<?xml version=\"1.1\" encoding=\"UTF-8\"?>\n<Folders width=\"%d\" height=\"%d\">\n",config.width,config.height);
            for (Folder f:config.folders) ps.printf("\t%s\n",f.toXML());
            ps.printf("</Folders>\n");
        } catch (FileNotFoundException ignore) {/*checked in advance*/}
    }
    
    static void secureThePath(String path) throws IOException {
        String dir=path;
        while (!new File(dir).exists())  dir=dir.substring(0,dir.lastIndexOf("/"));
        while (!dir.equals(path)) {
            String x=path.substring(dir.length());
            int cutPoint=x.indexOf("/",1);
            dir+=x.substring(0,cutPoint!=-1?cutPoint:x.length());
            new File(dir).mkdir();
        }
    }
    
    
}