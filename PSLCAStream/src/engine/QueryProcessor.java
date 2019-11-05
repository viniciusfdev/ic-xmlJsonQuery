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

    private HashMap<Query, List<Integer>> results;
    private List<List<Query>> queryGroup;
    private QueryGroupHash[] queryIndex;
    private List<Query> queries;
    private String queryFileName;
    private int nQueriesPerGroup;
    private String[] xmlFileList;
    private List<Thread> threads;
    private boolean semantic;
    private int nThreads;
    /**
     * 
     * @param queriesFileName
     * @param xmlFileList The list of documents 
     * @param sematic
     */
    public QueryProcessor(String queryFileName, String[] xmlFileList, boolean semantic, int nThreads) {
        this.threads = new ArrayList<Thread>();
        this.queryGroup = new ArrayList<>();
        this.queries = new ArrayList<>();
        this.queryFileName = queryFileName;
        this.xmlFileList = xmlFileList;
        this.nQueriesPerGroup = 0;
        this.semantic = semantic;
        this.nThreads = nThreads;
    }
    
    /**
     * Initializes and separates the queries for parallel process and
     * process multiple queries for each document D and set the results for each it.
     */
    public void multipleQueriesStart(){
      
        
        for(String xmlFileName: xmlFileList){
            try {                
                if(nThreads > Runtime.getRuntime().availableProcessors())
                    nThreads = Runtime.getRuntime().availableProcessors();
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
                    //t.sleep(t.getId()*100);
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
        BufferedReader queryFile;
        Query queryAux = null;
        int countQueries = 0;
        String line = "";
        int index = 0;
        int aux = 0;
        int i = 1;
        
        try {
            queryFile = new BufferedReader(new FileReader(new File("src/query_test/"+queryFileName).getAbsolutePath()));
            while((line = queryFile.readLine()) != null){
                queries.add(new Query(i++, Arrays.asList(line.split("\\s+"))));
                countQueries++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (IOException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        nQueriesPerGroup = countQueries/nThreads;

        queryGroup.add(new ArrayList<>());
        queryGroup.get(index).add(queries.get(0).clone());
        queries.remove(0);

        while(countOverallSize(queryGroup) != countQueries){
            Query query = queryGroup.get(index).get(queryGroup.get(index).size()-1);
            if(queryGroup.get(index).size() == nQueriesPerGroup && index+1 < nThreads){
                queryGroup.add(new ArrayList<>());
                index++;
            }
            aux = 0;
            for(Query queryAv: queries){
                if(queryGroup.get(index).contains(queryAv)) continue;
                i = 0;
                for(String term: queryAv.getQueryTerms()){
                    if(getAllTerms(queryGroup.get(index)).contains(term)){
                        i++;
                    }
                }
                if(i > aux){
                    aux = i;
                    queryAux = queryAv;
                }                
            }
            if(aux != 0){
                queryGroup.get(index).add(queryAux.clone());
                queries.remove(queryAux);
            }
            else{
                Query randonQuery = null;
                for(Query q: queries){
                    if(!queryGroup.get(index).contains(q)){
                        randonQuery = q;
                        break;
                    }
                }
                queryGroup.get(index).add(randonQuery.clone());
                queries.remove(randonQuery);

            }
        }
    }
    
    /**
     * Creates the query_index data and returns true if exist groups enough to
     * distribute into n threads
     */
    public Boolean buildQueryIndexGroup(int nThreads){
        try {
            groupQueryWhithCommonTerms();
            if(nQueriesPerGroup > 0)
            for(int index = 0; index < nThreads; index++){
                queryIndex[index] = new QueryGroupHash();
                for(String term: getAllTerms(queryGroup.get(index))){
                    for(Query q: queryGroup.get(index)){
                        if(q.getQueryTerms().contains(term))
                            queryIndex[index].addQuery(term, q);
                    }
                }
            }
            else{
                queryIndex[0] = new QueryGroupHash();
                for(String term: getAllTerms(queryGroup.get(0))){
                    for(Query q: queryGroup.get(0)){
                        if(q.getQueryTerms().contains(term))
                            queryIndex[0].addQuery(term, q);
                    }
                }
            }
               
            if(nQueriesPerGroup > 0) return true;
            
        } catch (PSLCAStreamException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
        /**
     * Return a list of all terms in one group.
     * @param group
     * @return 
     */
    public static List<String> getAllTerms(List<Query> group){
        List<String> terms = new ArrayList<>();
        for(Query query: group){
            terms.addAll(query.getQueryTerms());
        }
        return terms;
    }
    
    /**
     * Count overall size of a List of list of query.
     * @param group
     * @return 
     */
    public static int countOverallSize(List<List<Query>> group){
        int size = 0;
        for(List<Query> list: group){
            size += list.size();
        }
        return size;
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
