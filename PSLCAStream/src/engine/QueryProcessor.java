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
    private int nProcessors = 1;
    private QueryGroupHash[] queryIndex;
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
                nProcessors = Runtime.getRuntime().availableProcessors();
                queryIndex = new QueryGroupHash[nProcessors];
                buildQueryIndex(nProcessors);
                
                for(int i = 0; i < nProcessors ; i++){
                    threads.add(new Thread(new TaskControl(new FileReader
                        (new File("src/xml/"+xmlFileName).getAbsolutePath()), queryIndex[i])));
                }
                for(Thread t: threads){
                    t.start();
                }
                for(Thread t: threads){
                    t.join();
                }
                
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
    public void buildQueryIndex(int TAM){
        try {
            int j = 0;
            BufferedReader queryFile = new BufferedReader(new FileReader(new File("src/query_test/"+queryFileName).getAbsolutePath()));
            List<Query> queries = new ArrayList<Query>();
            List<String> terms = new ArrayList<String>();
            String line = "";
            queryIndex[0] = new QueryGroupHash();
            int index = 0;
            for(int i = 0; (line = queryFile.readLine()) != null; i++){
                queries.add(new Query(i, Arrays.asList(line.split("\\s+"))));
                for(String term: line.split("\\s+")){
                    if(!terms.contains(term))terms.add(term);
                }
            }
            if(queries.size() < nProcessors){
                nProcessors = 1;
                TAM = 1;
            }
            for(String term: terms){
                if(j == (terms.size()/TAM)*(index+1)){
                    if(index+1 < TAM){
                        index++;
                        queryIndex[index] = new QueryGroupHash();
                    }
                }
                for(Query q: queries){
                    if(q.getQueryTerms().contains(term))
                        queryIndex[index].addQuery(term, q);
                }
                j++;
            }

        } catch (PSLCAStreamException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }

    public QueryGroupHash[] getQueryIndex() {
        return queryIndex;
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
