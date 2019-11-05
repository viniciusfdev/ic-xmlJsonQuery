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
    private HashMap<Integer, TermOcurrence> matchedTerms;

    public Query(int queryID, List<String> queryTerms) {
        this.results = new ArrayList<Integer>();
        this.queryID = queryID;
        this.queryTerms = queryTerms;
        this.lastResultId = -1;
        this.matchedTerms = new HashMap<>();
    }
    
    /**
     * Print all query terms.
     */
    public void printQueryTerms(){
        for(String a: this.queryTerms){
            System.out.print(a+" ");
        }
        System.out.println("");
    }
    
    /**
     * Increment a node combination with have a relation with one term.
     * @param id
     * @param term 
     */
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
    
    /**
     * Return the number of combinations in one node.
     * @param nodeId
     * @return 
     */
    public Integer nMatches(Integer nodeId){
        Integer matches;
        if((matches = matchedTerms.get(nodeId).getnOcurrences()) != null)
            return matches;
        else return 0;
    }

    @Override
    public Query clone(){ 
        try{
            return (Query)super.clone();
        }catch(CloneNotSupportedException ex){
            System.out.println(ex.getMessage());
        }
        return null;
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
