/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private String[] XMLFileList;
    private String XMLFilePath;
    private HashMap<Query, List<Integer>> results;

    /**
     * 
     * @param queriesFileName
     * @param XMLFileList The list of documents 
     * @param XMLFilePath 
     */
    public QueryProcessor(String queriesFileName, String[] XMLFileList, String XMLFilePath) {
        threads = new ArrayList<Thread>();
        this.XMLFileList = XMLFileList;
        this.XMLFilePath = XMLFilePath;
    }
    
    /**
     * Initializes and separates the queries for parallel process.
     */
    public void start(){
        
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

    public String[] getXMLFileList() {
        return XMLFileList;
    }

    public void setXMLFileList(String[] XMLFileList) {
        this.XMLFileList = XMLFileList;
    }

    public String getXMLFilePath() {
        return XMLFilePath;
    }

    public void setXMLFilePath(String XMLFilePath) {
        this.XMLFilePath = XMLFilePath;
    }

    public HashMap<Query, List<Integer>> getResults() {
        return results;
    }

    public void setResults(HashMap<Query, List<Integer>> results) {
        this.results = results;
    }
    
    

}
