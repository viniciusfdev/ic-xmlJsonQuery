/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import exception.PSLCAStreamException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import query.Query;
import query.QueryGroupHash;

/**
 * Process the queries for each Document.
 * 
 * @author vinicius
 */
public class QueryProcessor {
    private QueryGroupHash queryIndex;
    private List<Thread> threads;
    private List<Query> queryList;
    private String queryFileName;
    private String[] xmlFileList;
    private HashMap<Query, List<Integer>> results;
    /**
     * 
     * @param queriesFileName
     * @param xmlFileList The list of documents 
     * @param xmlFilePath 
     */
    public QueryProcessor(String queryFileName, String[] xmlFileList) {
        threads = new ArrayList<Thread>();
        this.queryIndex = new QueryGroupHash();
        this.xmlFileList = xmlFileList;
        this.queryFileName = queryFileName;
    }
    
    /**
     * Initializes and separates the queries for parallel process and
     * process multiple queries for each document D and set the results for each it.
     */
    public void multipleQueriesStart(){
      
//        int numProcessors = Runtime.getRuntime().availableProcessors();
//        for(int i = 0; i < numProcessors ; i++){
//            
//        }
        
        for(String xmlFileName: xmlFileList){
            
            try {                
                buildQueryIndex();
                FileReader xmlFile = new FileReader(new File("src/xml/"+xmlFileName).getAbsolutePath());
                threads.add(new Thread(new TaskControl(xmlFile, queryIndex)));
                threads.get(0).start();
                threads.get(0).join();
                
            } catch (FileNotFoundException ex) {
                System.out.println("Error loading files");
                Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                System.out.println("Error in Thread");
                Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
            
    }
     
    /**
     * Group all queries with have common terms to accelerate the search engine.
     */
    public void groupQueryWhithCommonTerms(){
        
    }
    
    /**
     * Creates the query_index data.
     */
    public void buildQueryIndex(){
        try {
            
            BufferedReader queryFile = new BufferedReader(new FileReader(new File("src/query_test/"+queryFileName).getAbsolutePath()));
            List<Query> queries = new ArrayList<Query>();
            List<String> terms = new ArrayList<String>();
            String line = "";
            for(int i = 0; (line = queryFile.readLine()) != null; i++){
                queries.add(new Query(i++, Arrays.asList(line.split("(\\s+)|(\t)"))));
                for(String term: line.split("(\\s+)|(\t)")){
                    terms.add(term);
                }
            }
            for(String term: terms){
                for(Query q: queries){
                    if(q.getQueryTerms().contains(term))
                        queryIndex.addQueries(term, q);
                }
            }
            
        } catch (PSLCAStreamException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }

    public QueryGroupHash getQueryIndex() {
        return queryIndex;
    }

    public void setQueryIndex(QueryGroupHash queryIndex) {
        this.queryIndex = queryIndex;
    }

    public List<Query> getQueryList() {
        return queryList;
    }

    public void setQueryList(List<Query> queryList) {
        this.queryList = queryList;
    }

    public String[] getxmlFileList() {
        return xmlFileList;
    }

    public void setxmlFileList(String[] xmlFileList) {
        this.xmlFileList = xmlFileList;
    }

    public HashMap<Query, List<Integer>> getResults() {
        return results;
    }

    public void setResults(HashMap<Query, List<Integer>> results) {
        this.results = results;
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public void setThreads(List<Thread> threads) {
        this.threads = threads;
    }

    public String getQueryFileName() {
        return queryFileName;
    }

    public void setQueryFile(String queryFileName) {
        this.queryFileName = queryFileName;
    }

    public String[] getXmlFileList() {
        return xmlFileList;
    }

    public void setXmlFileList(String[] xmlFileList) {
        this.xmlFileList = xmlFileList;
    }

}
