/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A structured group having each entry like: 
 * Key = term and Value = set of Query 
 * 
 * @author vinicius franca, evandrino barros
 */
public class QueryGroupHash {
    private HashMap<String, List<Query>> queryGroupHash;

    public QueryGroupHash() {
        this.queryGroupHash = new HashMap<String, List<Query>>();
    }
    
    public HashMap<String, List<Query>> getQueryGroupHash() {
        return queryGroupHash;
    }

    public void setQueryGroupHash(HashMap<String, List<Query>> queryGroupHash) {
        this.queryGroupHash = queryGroupHash;
    }
    
    public void printQueriesByTerm(){
        for(String s: this.queryGroupHash.keySet()){
            System.out.println("Term:["+s+"]");
            for(Query q: this.queryGroupHash.get(s)){
                System.out.print("QID["+q.getQueryID()+"]");
                q.printQueryTerms();
            }
            System.out.println("\n");
        }
    }
        
    public void addQuery(String term, Query query){
        if(!queryGroupHash.containsKey(term)){
            List<Query> queries = new ArrayList<Query>();
            queries.add(query);
            queryGroupHash.put(term, queries);
        }
        else queryGroupHash.get(term).add(query);
    }
    
    public List<Query> getQueries(String term){
        return queryGroupHash.get(term);
    }
}

