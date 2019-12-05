/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import exception.PSLCAStreamException;
import java.io.File;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private File[] xmlFileList;
    private int nGroups;
    private long execTime;
    private SearchEngine search;
    private boolean semantic = true;
    private QueryGroupHash queryIndex;
 
    
    public TaskControl(File[] xmlFileList, QueryGroupHash queryIndex, boolean semantic, int nGroups) {
        this.xmlFileList = xmlFileList;
        this.execTime = 0;
        this.nGroups = nGroups;
        this.semantic = semantic;
        this.queryIndex = queryIndex;
    }
    
    /**
     * Automatically call when the parser start initializing the search.
     */
    @Override
    public void run() {
        int tam = queryIndex.getQueryGroupHash().size();
        int index = 0;
        int count = 0;
        try {
            Iterator it = queryIndex.getQueryGroupHash().entrySet().iterator();
            QueryGroupHash[] queryIndexGroups = new QueryGroupHash[nGroups];
            queryIndexGroups[0] = new QueryGroupHash();
            
            if(tam >= nGroups)
            while(it.hasNext()){
                if((count == (index+1)*(tam/nGroups)) && (index+1) < nGroups){
                    index++;
                    queryIndexGroups[index] = new QueryGroupHash();
                }
                Map.Entry pair = (Map.Entry)it.next();
                queryIndexGroups[index].getQueryGroupHash().put((String)pair.getKey(), (List<Query>)pair.getValue());
                count++;
            }
            
            for(QueryGroupHash queryIndex: queryIndexGroups){
                if(queryIndex != null){
                    if(!queryIndex.getQueryGroupHash().isEmpty()){
                        long initTime = System.currentTimeMillis();
                        for(File file: xmlFileList){
                            FileReader currentFile = new FileReader(file);
                            XMLReader xr = XMLReaderFactory.createXMLReader();
                            search = new SearchEngine(queryIndex, semantic);
                            xr.setContentHandler(search);
                            xr.setErrorHandler(search);
                            xr.parse(new InputSource(currentFile));
                            //search.printResultsByQuery();
                        }
                        long finalTime = System.currentTimeMillis();
                        execTime = finalTime - initTime;
                    }else{
                        throw new PSLCAStreamException("Query Index => não possui consultas");
                    }
                }else{
                    throw new PSLCAStreamException("Query Index => argumento nulo");
                }
            }
            
        } catch (SAXException ex) {
            Logger.getLogger(TaskControl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TaskControl.class.getName()).log(Level.SEVERE, null, ex);
        }
	
    }

    public long getExecTime() {
        return execTime;
    }
}
