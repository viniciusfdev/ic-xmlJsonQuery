/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import exception.PSLCAStreamException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import query.Query;
import query.QueryGroupHash;

/**
 *
 * @author vinicius franca, evandrino barros
 */
public class TaskControl implements Runnable{
    
    private FileReader file;
    private SearchEngine search;
    private QueryGroupHash queryIndex;
    
    public TaskControl(FileReader file, QueryGroupHash queryIndex) {
        this.file = file;
        this.search = search;
        this.queryIndex = queryIndex;
    }
    
    /**
     * Automatically call when the parser start initializing the search.
     */
    @Override
    public void run() {
        
        try {
            
            XMLReader xr = XMLReaderFactory.createXMLReader();
            if(queryIndex != null){
                if(!queryIndex.getQueryGroupHash().isEmpty()){
                    search = new SearchEngine(queryIndex);
                    xr.setContentHandler(search);
                    xr.setErrorHandler(search);
                    xr.parse(new InputSource(file));
                    
                }else{
                    throw new PSLCAStreamException("Query Index => não possui consultas");
                }
            }else{
                throw new PSLCAStreamException("Query Index => argumento nulo");
            }
            
        } catch (SAXException ex) {
            Logger.getLogger(TaskControl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TaskControl.class.getName()).log(Level.SEVERE, null, ex);
        }
	
    }
    
 
}
