/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import exception.PSLCAStreamException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
    private String queryFileName;
    private int nQueriesPerGroup;
    private List<Thread> threads;
    private List<Query> queries;
    private File[] xmlFileList;
    private boolean semantic;
    private int nThreads;
    private int nQueries;
    private int nGroups;
    
    /**
     * 
     * @param queriesFileName
     * @param xmlFileList The list of documents 
     * @param sematic
     */
    public QueryProcessor(String queryFileName, File[] xmlFileList, boolean semantic, int nQueries, int nThreads, int nGroups) {
        this.threads = new ArrayList<Thread>();
        this.queryGroup = new ArrayList<>();
        this.queries = new ArrayList<>();
        this.queryFileName = queryFileName;
        this.xmlFileList = xmlFileList;
        this.nQueriesPerGroup = 0;
        this.semantic = semantic;
        this.nThreads = nThreads;
        this.nQueries = nQueries;
        this.nGroups = nGroups;
    }
    
    /**
     * Initializes and separates the queries for parallel process and
     * process multiple queries for each document D and set the results for each it.
     */
    public void multipleQueriesStart(){
        queryIndex = new QueryGroupHash[nThreads];
        buildQueryIndexGroup(nThreads);
        for(File xmlFile: xmlFileList){
            System.out.println(xmlFile.getName());
            threads.clear();
            try {                
                if(nThreads > Runtime.getRuntime().availableProcessors())
                    nThreads = Runtime.getRuntime().availableProcessors();
                if(nQueriesPerGroup > 0)
                    for(int i = 0; i < nThreads ; i++){
                        threads.add(new Thread(new TaskControl(xmlFile, 
                                queryIndex[i], semantic, nGroups)));
                    }
                else
                    threads.add(new Thread(new TaskControl(xmlFile, 
                                queryIndex[0], semantic, nGroups)));
                for(Thread t: threads){
                    t.start();
                    //t.sleep(t.getId()*100); //ordena o resultado printado
                }
                for(Thread t: threads){
                    t.join();
                }
                
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
        Boolean groupQuery = true;
        BufferedReader queryFile;
        Query queryAux = null;
        int countQueries = 0;
        String line = "";
        int index = 0;
        int aux = 0;
        int i = 1;
        
        try {
            queryFile = new BufferedReader(new FileReader(new File("query_test/"+queryFileName).getAbsolutePath()));
            while((line = queryFile.readLine()) != null){
                queries.add(new Query(i++, Arrays.asList(line.split("\\s+"))));
                countQueries++;
                if(countQueries == nQueries) break;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (IOException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        nQueriesPerGroup = countQueries/nThreads;
        queryGroup.add(new ArrayList<>());
        
        long init = System.currentTimeMillis();
        if(groupQuery){
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
        else
        while(countOverallSize(queryGroup) != countQueries){
            if(queryGroup.get(index).size() == nQueriesPerGroup && index+1 < nThreads){
                queryGroup.add(new ArrayList<>());
                index++;
            }
            queryGroup.get(index).add(queries.remove(0));
        }
        long finalT = System.currentTimeMillis();
        System.out.println("Time to group "+nQueries+" queries:"+(finalT-init));
        writeGroupedQueries();
    }
    
    private void writeGroupedQueries(){
        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter("query_test/g"+queryFileName));
            
            for(List<Query> group: queryGroup){
                for(Query query: group){
                    String q = query.getQuery();
                    wr.write(q, 0, q.length());
                    wr.newLine();
                }
            }
            
            wr.close();
            System.out.println("Consultas agrupadas gravadas");
        } catch (IOException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Creates the query_index data and returns true if exist groups enough to
     * distribute into n threads
     */
    public void buildQueryIndexGroup(int nThreads){
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
               
            
        } catch (PSLCAStreamException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            reader = new BufferedReader(new FileReader(new File("query_test/"+queryFileName).getAbsolutePath()));
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
}
