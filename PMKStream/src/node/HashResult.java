package node;

import query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @version 2.0
 * @author Evandrino Barros, Jônatas Tonholo
 */
public class HashResult {
    private HashMap<Query,ArrayList<ResultNode>> hashResult;
    private long countNoSLCA;
    private long countOnlySLCA;

    /**
     * The Constructor
     * @param numberOfQueries
     */
    public HashResult(int numberOfQueries){
        this.hashResult = new HashMap(numberOfQueries*2,1.0f);
        resetCountNoSLCAAndOnlySLCA();
    }

    /**
     * Initialize the hash result
     * TODO: Está sendo utilizado?
     * @param query
     */
    public void initialize (Query query ) {
        this.hashResult.put(query, new ArrayList<ResultNode>());
    }

    /**
     * Reset Count of no SLCA and only SLCA
     */
    public void resetCountNoSLCAAndOnlySLCA () {
        this.countNoSLCA = 0;
        this.countOnlySLCA = 0;
    }

    /**
     * Count no SLCA and Only SLCA
     */
    public synchronized void countNoSLCAAndOnlySLCA () {
        HashMap tempHash = new HashMap();
        while(true) {
            try {
                tempHash = (HashMap)this.hashResult.clone();
                break;
            } catch (Exception e) {}
        }
        Set keyset = tempHash.keySet();
        Iterator i = keyset.iterator();
        while (i.hasNext()) {
            Query key = (Query) i.next();
            List<ResultNode> rNodeList = Collections.synchronizedList(this.hashResult.get(key));
            for (ResultNode rNode : rNodeList){
                if (rNode.getNodeType().contentEquals("onlySLCA"))
                    this.countOnlySLCA++;
                else
                    this.countNoSLCA++;
            }
        }
    }

    /**
     * Put the Result Node in the Hash Result
     * @param query
     * @param resultNode
     */
    public void putResultNode (Query query,  ResultNode resultNode) {
        ArrayList<ResultNode> resultList = this.hashResult.get(query);
        if (resultList != null) {
            resultList.add(resultNode);
        } else {
            ArrayList<ResultNode> list = new ArrayList<ResultNode>();
            list.add(resultNode);
            this.hashResult.put(query, list);
        }
    }

    /**
     * Print All Hash Results
     */
    public void printAllHashResult(){
        Set keyset = this.hashResult.keySet();
        Iterator i = keyset.iterator();
        System.out.println("query;resultNodeLabel;IdNode");
        while (i.hasNext()) {
            Query key = (Query) i.next();
            ArrayList<ResultNode> rNodeList = this.hashResult.get(key);
            //System.out.println("---- Resultados da Consulta "+key.getQueryNumber());
            for (ResultNode rnode: rNodeList){
                //System.out.println("    - TagId: "+rnode.getTagId()+
                //        "    - Label: "+rnode.getLabel()+
                //        "    - Node type: "+rnode.getNodeType()+
                //        "    - Query: "+ key.getQueryTerms());
                
                System.out.println(key.getQueryTerms()+";"+rnode.getLabel()+";"+rnode.getTagId());
            }
        }
    }
    
    
    

    /**
     * Gets hashResult.
     *
     * @return Value of hashResult.
     */
    public HashMap<Query, ArrayList<ResultNode>> getHashResult() {
        return this.hashResult;
    }

    /**
     * Gets countNoSLCA.
     *
     * @return Value of countNoSLCA.
     */
    public long getCountNoSLCA() {
        return countNoSLCA;
    }

    /**
     * Gets countOnlySLCA.
     *
     * @return Value of countOnlySLCA.
     */
    public long getCountOnlySLCA() {
        return countOnlySLCA;
    }
}
