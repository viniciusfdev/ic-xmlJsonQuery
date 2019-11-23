/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupwithcommonterms;

import java.util.List;

/**
 *
 * @author vinicius
 */
public class Query implements Cloneable{
    private int queryID;
    private List<String> queryTerms;

    public Query(int queryID, List<String> queryTerms) {
        this.queryID = queryID;
        this.queryTerms = queryTerms;
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

    public List<String> getQueryTerms() {
        return queryTerms;
    }

    public void setQueryTerms(List<String> queryTerms) {
        this.queryTerms = queryTerms;
    }
    
}
