/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vinicius franca, evandrino barros
 */
public class Query implements Cloneable {
    private int queryID;
    private int lastResultId;
    private List<String> queryTerms;
    private List<Integer> results;
    private HashMap<Integer, TermOcurrence> matchedTerms; //needs be a hash HashMaps<id do no, machedValue>()

    public Query(int queryID, List<String> queryTerms) {
        this.results = new ArrayList<Integer>();
        this.queryID = queryID;
        this.queryTerms = queryTerms;
        this.lastResultId = -1;
        this.matchedTerms = new HashMap<>();
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
    
    public void increaseMachedTermsById(Integer id, String term) {
        TermOcurrence mt;
        if((mt = matchedTerms.get(id)) != null){
            if(mt.getTermOcurrences().get(term) != null){
                if(!mt.getTermOcurrences().get(term))
                    mt.setOcurrence(term);
            }else{
                mt.getTermOcurrences().put(term, Boolean.TRUE);
            }
        }
        else{
            this.matchedTerms.put(id, new TermOcurrence(term));
        }
    }
    
    public Integer nMatches(Integer nodeId){
        Integer matches;
        if((matches = matchedTerms.get(nodeId).getnOcurrences()) != null)
            return matches;
        else return 0;
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

    public HashMap<Integer, TermOcurrence> getMatchedTerms() {
        return this.matchedTerms;
    }
}
