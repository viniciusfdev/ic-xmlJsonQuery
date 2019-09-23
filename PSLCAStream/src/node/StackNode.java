/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package node;

import java.util.ArrayList;
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
    private int height;
    private long matchedTerms;
    private String label;
    private List<Query> usedQueries;

    public StackNode() {
        this.height = 0;
        this.nodeId = -1;
        this.matchedTerms = 0;
        this.usedQueries = new ArrayList<Query>();
    }

    public StackNode(String label, int nodeId) {
        this.height = 0;
        this.nodeId = nodeId;
        this.label = label;
        this.matchedTerms = 0;
        this.usedQueries = new ArrayList<Query>();
    }
    
    public StackNode(StackNode sn) {
        this.height = sn.getHeight();
        this.nodeId = sn.getNodeId();
        this.label = sn.getLabel();
        this.matchedTerms = sn.getMatchedTerms();
        this.usedQueries = sn.getUsedQueries();
    }
    
    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public List<Query> getUsedQueries() {
        return usedQueries;
    }

    public void addUsedQueries(List<Query> queries) {
        for(Query query: queries){
            if(this.usedQueries == null){
                this.usedQueries = new ArrayList<Query>();
            }
            if(!existQuery(query)){
                this.usedQueries.add(query);
            }
        }
    }

    public long getMatchedTerms() {
        return matchedTerms;
    }

    public void setMatchedTerms(long matchedTerms) {
        this.matchedTerms = matchedTerms;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    public boolean existQuery(Query query){
        for(Query q: usedQueries){
            if(q.getQueryID() == query.getQueryID()) return true;
        }
        return false;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
}
