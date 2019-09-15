/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

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
        this.queryGroupHash = new HashMap<>();
    }

    public void addQuery(String term, Query query){
        queryGroupHash.put(term, (List<Query>) query);
    }
    
    public HashMap<String, List<Query>> getQueryGroupHash() {
        return queryGroupHash;
    }

    public void setQueryGroupHash(HashMap<String, List<Query>> queryGroupHash) {
        this.queryGroupHash = queryGroupHash;
    }
    
    
}

