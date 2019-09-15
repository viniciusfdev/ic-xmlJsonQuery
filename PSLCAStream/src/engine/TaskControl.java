/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import query.QueryGroupHash;

/**
 *
 * @author vinicius franca, evandrino barros
 */
public class TaskControl implements Runnable{
    
    private FileReader xmlFile;
    private SearchEngine search;
    private QueryGroupHash queryIndex;

    public TaskControl(FileReader xmlFile, SearchEngine search, QueryGroupHash queryIndex) {
        this.xmlFile = xmlFile;
        this.search = search;
        this.queryIndex = queryIndex;
    }

    @Override
    public void run() {
        XMLReader xr;
        try {
            
            xr = XMLReaderFactory.createXMLReader();
            if(queryIndex != null)
                if(queryIndex)
                    SearchEngine handler = new SearchEngine(queryIndex);
                else
                    System.out.println("");
            else
            xr.setContentHandler(handler);
            xr.setErrorHandler(handler);
            FileReader fr = new FileReader(new File("xmlfile.xml").getAbsolutePath());
            xr.parse(new InputSource(fr));
            
        } catch (SAXException ex) {
            Logger.getLogger(TaskControl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TaskControl.class.getName()).log(Level.SEVERE, null, ex);
        }
	
    }
    
}
