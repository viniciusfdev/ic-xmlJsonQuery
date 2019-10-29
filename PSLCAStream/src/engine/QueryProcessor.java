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
import query.Term;

/**
 * Process the queries for each Document.
 * 
 * @author vinicius
 */
public class QueryProcessor {

    private boolean semantic;
    private int nThreads = 1;
    private List<Thread> threads;
    private String queryFileName;
    private String[] xmlFileList;
    private QueryGroupHash[] queryIndex;
    private HashMap<Query, List<Integer>> results;
    /**
     * 
     * @param queriesFileName
     * @param xmlFileList The list of documents 
     * @param sematic
     */
    public QueryProcessor(String queryFileName, String[] xmlFileList, boolean semantic) {
        this.semantic = semantic;
        this.xmlFileList = xmlFileList;
        this.threads = new ArrayList<Thread>();
        this.queryFileName = queryFileName;
    }
    
    /**
     * Initializes and separates the queries for parallel process and
     * process multiple queries for each document D and set the results for each it.
     */
    public void multipleQueriesStart(){
      
        
        for(String xmlFileName: xmlFileList){
            try {                
                //nThreads = Runtime.getRuntime().availableProcessors();
                nThreads = 1;
                queryIndex = new QueryGroupHash[nThreads];
                if(buildQueryIndexGroup(nThreads))
                    for(int i = 0; i < nThreads ; i++){
                        threads.add(new Thread(new TaskControl(new FileReader
                            (new File("src/xml/"+xmlFileName).getAbsolutePath()), queryIndex[i], semantic)));
                    }
                else
                    threads.add(new Thread(new TaskControl(new FileReader
                            (new File("src/xml/"+xmlFileName).getAbsolutePath()), queryIndex[0], semantic)));
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
     * Creates the query_index data and returns true if exist groups enough to
     * distribute into n threads
     */
    public Boolean buildQueryIndexGroup(int nThreads){
        int nQueriesPerGroup = this.countQueriesFile()/nThreads;
        try {
            BufferedReader queryFile = new BufferedReader(new FileReader(new File("src/query_test/"+queryFileName).getAbsolutePath()));
            List<List<Query>> queryGroup = new ArrayList<>();
            List<List<String>> termGroup = new ArrayList<>();
            queryGroup.add(new ArrayList<>());
            termGroup.add(new ArrayList<>());
            int i = 0, index = 0;
            String line = "";
            
            for(;(line = queryFile.readLine()) != null; i++){
                if(i == nQueriesPerGroup*(index+1) && nQueriesPerGroup > 0 && index+1 < nThreads){
                    queryGroup.add(new ArrayList<>());
                    termGroup.add(new ArrayList<>());
                    index++;
                }
                List<Term> terms = new ArrayList<>();
                for(String t: Arrays.asList(line.split("\\s+"))){
                    terms.add(new Term(t));
                }
                queryGroup.get(index).add(new Query(i, terms));
                for(String term: line.split("\\s+")){
                    if(!termGroup.get(index).contains(term))
                        termGroup.get(index).add(term);
                }
            }
            if(nQueriesPerGroup > 0)
            for(index = 0; index < nThreads; index++){
                queryIndex[index] = new QueryGroupHash();
                for(String term: termGroup.get(index)){
                    for(Query q: queryGroup.get(index)){
                        if(q.contains(term))
                            queryIndex[index].addQuery(term, q);
                    }
                }
            }
            else{
                queryIndex[0] = new QueryGroupHash();
                for(String term: termGroup.get(0)){
                    for(Query q: queryGroup.get(0)){
                        if(q.getQueryTerms().contains(term))
                            queryIndex[0].addQuery(term, q);
                    }
                }
            }
               
            if(nQueriesPerGroup > 0) return true;
            
        } catch (PSLCAStreamException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public int countQueriesFile(){
        int lines = 0;
        try {
            BufferedReader reader = null;
            reader = new BufferedReader(new FileReader(new File("src/query_test/"+queryFileName).getAbsolutePath()));
            while (reader.readLine() != null) lines++;
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IOException ex){
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }

    public QueryGroupHash[] getQueryIndex() {
        return queryIndex;
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
