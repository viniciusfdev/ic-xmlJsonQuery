package node;

import query.Query;

import java.util.BitSet;
import java.util.HashSet;

/**
 * @version 2.0
 * @author Evandrino Barros, Jônatas Tonholo
 */
public class LCAStackNode {
    private String label;
    private int tagId;
    private int nodeHeight;
    private BitSet termInstances;
    private BitSet nonSLCAbitmap;
    private HashSet<Query> queryList;
    private long matchingTerms;

    /**
     * LCAStackNode Constructor
     *
     * @param label
     * @param tagId
     * @param XMLheight
     * @param numberOfTerms
     * @param numberOfQueries
     */
   public LCAStackNode (String label, int tagId, int XMLheight, int numberOfTerms, int numberOfQueries) {
        this.label = label;
        this.tagId = tagId;
        this.nodeHeight = XMLheight;
        this.matchingTerms = 0;
        this.termInstances = new BitSet(); // 0..n-1 => all zero bits
        this.nonSLCAbitmap = new BitSet(); // 0..n => all zero bits
        this.queryList = new HashSet<Query>();
    }

    /**
     * Put the Query in Can't be SLCA bitmap
     * @param query
     */
    public void putQueryInCantBeSLCABitmap (Query query) {
        this.nonSLCAbitmap.set(query.getQueryNumber(), true);
    }

    /**
     * Verify if Can be SLCA
     *
     * @param query
     * @return
     */
    public boolean canBeSLCA (Query query) {
        return !this.nonSLCAbitmap.get(query.getQueryNumber());
    }

    /**
     * Get the SLCA Flag
     * @param query
     * @return SLCA Flag
     */
    public String getStringSLCAFlag (Query query) {
        if((Boolean)this.nonSLCAbitmap.get(query.getGroupNumber())) {
            return new String("Non-SLCA");
        } else {
            return new String("SLCA");
        }
    }

    /**
     * Print the Term Instances
     * TODO: Verificar se está funcionando corretamente com o StringBuilder, se não, retornar com implementação original
     */
    public void printTermInstances () {
        if(this.queryList == null && this.queryList.isEmpty()) {
            System.out.println("         ("+this.nodeHeight+") "+this.label+"["+this.tagId+"]"+
                    " - nenhuma consulta");
            return;
        }
        StringBuilder queryString = new StringBuilder();
        queryString.append(" - Consultas: ");
        for(Query query : queryList) {
            queryString.append(query.getQueryNumber());
            queryString.append(query.getPositions());
            queryString.append(", ");
        }

        System.out.println("         ("+this.nodeHeight+") "+this.label+"["+this.tagId+"]"
                +this.termInstances.toString()+ queryString.toString());
    }

    /**
     * Print the Query List
     */
    public void printQueryList () {
        System.out.print("         Queries: ");
        for (Query query: this.queryList){
            System.out.print("Query number= "+query.getQueryNumber() + ",");
        }
        System.out.println();
    }

    /**
     * Verify if the Matches Query Bitmaps
     * @param queryBitmap
     * @return
     */
    public boolean MatchesQueryBitMaps (BitSet queryBitmap) {
        BitSet nodeBitmap = (BitSet) this.termInstances.clone(); //FIXME: Verificar necessidade do clone
        nodeBitmap.and(queryBitmap);
        return nodeBitmap.equals(queryBitmap);
    }

    /**
     * Return the match Query
     * @param query
     * @return matchingValue
     */
    public boolean  matchQuery(Query query){
        boolean matchingValue = false;
        for (Integer i : query.getTermPositions()) {
            if (this.termInstances.get(i.intValue()) == false) {
                matchingValue = false;
                break;
            }
            matchingValue = true;
        }
        return matchingValue;
    }

    /**
     * Methode that's returns the Non-SLCA bitmap
     *
     * @return nonSLCAbitmap
     */
    public BitSet getCantBeSLCABitmap () {
        return this.nonSLCAbitmap;
    }

    /**
     * Set the Non-SLCA bitmap
     * FIXME: Anteriormente chamado de copyCantBeSLCABitmap
     * @param bitmap
     */
    public void setCantBeSLCABitmap (BitSet bitmap) {
        this.nonSLCAbitmap = bitmap;
    }


    /**
     * Get Node Height
     * @return nodeHeight
     */
    public int getNodeHeight() {
        return nodeHeight;
    }

    /**
     * Set the Node Height
     * FIXME: Anteriormente chamado de getNodeHeight
     * @param tagHeight
     */
    public void setNodeHeight(int tagHeight) {
        this.nodeHeight = tagHeight;
    }

    /**
     * Get the Label
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the Tag ID
     * @return
     */
    public int getTagId() {
        return tagId;
    }

    /**
     * Get the Term Instances
     * @return
     */
    public BitSet getTermInstances() {
        return termInstances;
    }

    /**
     * Gets matchingTerms.
     *
     * @return Value of matchingTerms.
     */
    public long getMatchingTerms() {
        return matchingTerms;
    }

    /**
     * Increment Matching Terms by 1 unit
     * @return
     */
    public long incrementMatchingTerms () {
        this.matchingTerms++;
        return this.matchingTerms;
    }

    /**
     * Increment Matching Terms by an increment
     * @param increment
     * @return
     */
    public long incrementMatchingTerms (long increment) {
        this.matchingTerms+=increment;
        return this.matchingTerms;
    }

    /**
     * Gets queryList.
     *
     * @return Value of queryList.
     */
    public HashSet<Query> getQueryList() {
        return queryList;
    }

    /**
     * Gets nonSLCAbitmap.
     *
     * @return Value of nonSLCAbitmap.
     */
    public BitSet getNonSLCAbitmap() {
        return nonSLCAbitmap;
    }

}
