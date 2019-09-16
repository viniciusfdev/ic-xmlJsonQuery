/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

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

    public Query(int queryID, int lastResultId, List<String> queryTerms, List<Integer> results) {
        this.queryID = queryID;
        this.lastResultId = lastResultId;
        this.queryTerms = queryTerms;
    }
    
    public void addResult(Integer nodeId){
        this.results.add(nodeId);
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

    public void setResults(List<Integer> results) {
        this.results = results;
    }
    
    
    
}
