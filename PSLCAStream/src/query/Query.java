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
 *
 * @author vinicius franca, evandrino barros
 */
public class Query {
    private int queryID;
    private int lastResultId;
    private List<String> queryTerms;
    private List<Integer> results;
    private HashMap<Integer, Integer> machedTerms; //needs be a hash HashMaps<id do no, machedValue>()

    public Query(int queryID, List<String> queryTerms) {
        this.results = new ArrayList<Integer>();
        this.queryID = queryID;
        this.queryTerms = queryTerms;
        this.lastResultId = -1;
        this.machedTerms = new HashMap<>();
    }

    public void addResult(Integer nodeId){
        this.results.add(nodeId);
    }
    
    public void printQueryTerms(){
        for(String a: this.queryTerms){
            System.out.print(a+" ");
        }
        System.out.println("");
    }
    
    public void increaseMachedTermsById(Integer id) {
        Integer mt;
        if((mt = machedTerms.get(id)) != null)
            this.machedTerms.put(id, mt+1);
        else
            this.machedTerms.put(id, 1);
    }

    public int getQueryID() {
        return queryID;
    }

    public void setQueryID(int queryID) {
        this.queryID = queryID;
    }

    public int getLastResultId() {
        return lastResultId;
    }

    public void setLastResultId(int lastResultId) {
        this.lastResultId = lastResultId;
    }

    public List<String> getQueryTerms() {
        return queryTerms;
    }

    public void setQueryTerms(List<String> queryTerms) {
        this.queryTerms = queryTerms;
    }

    public List<Integer> getResults() {
        return results;
    }

    public void addResult(int result) {
        this.results.add(result);
    }

    public HashMap<Integer, Integer> getMachedTerms() {
        return this.machedTerms;
    }
}
