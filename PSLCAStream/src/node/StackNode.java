/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package node;

import java.util.List;
import query.Query;
import query.QueryGroupHash;

/**
 * Parsing Stack Node to process open nodes during
 * parser.
 * 
 * @author vinicius franca, evandrino barros
 */
public class StackNode {
    private int nodeId;
    private long matchedTerms;
    private QueryGroupHash usedQueries;

    public StackNode(int nodeId, QueryGroupHash usedQueries) {
        this.nodeId = nodeId;
        this.usedQueries = usedQueries;
    }
    
    public StackNode(int nodeId) {
        this.nodeId = nodeId;
        this.usedQueries = usedQueries;
    }
    
    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public QueryGroupHash getUsedQueries() {
        return usedQueries;
    }

    public void setUsedQueries(QueryGroupHash usedQueries) {
        this.usedQueries = usedQueries;
    }
    
    public void addUsedQueries(String term, List<Query> usedQueries) {
        for(Query query: usedQueries){
            this.usedQueries.addQueries(term, query);
        }
    }

    public long getMatchedTerms() {
        return matchedTerms;
    }

    public void setMatchedTerms(long matchedTerms) {
        this.matchedTerms = matchedTerms;
    }
    
    
    
}
