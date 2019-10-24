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
    private String label;
    private List<Query> usedQueries;

    public StackNode() {
        this.height = 0;
        this.nodeId = -1;
        this.usedQueries = new ArrayList<Query>();
    }

    public StackNode(String label, int height, int nodeId) {
        this.height = height;
        this.nodeId = nodeId;
        this.label = label;
        this.usedQueries = new ArrayList<Query>();
    }
    
    public StackNode(StackNode sn) {
        this.height = sn.getHeight();
        this.nodeId = sn.getNodeId();
        this.label = sn.getLabel();
        this.usedQueries = sn.getUsedQueries();
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
    
    public void inheritMachedTerms(Integer fId, Integer cId){
        for(Query query: usedQueries){
            Integer fmt = query.getMachedTerms().get(fId);
            Integer cmt = query.getMachedTerms().get(cId);
            if(cmt != null){
                if(fmt != null)
                    query.getMachedTerms().put(fId, fmt+cmt);
                else
                    query.getMachedTerms().put(fId, cmt);
            }
        }
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
