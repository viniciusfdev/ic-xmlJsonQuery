package query;

import node.LCAStackNode;

import java.util.*;

/**
 * @version 2.0
 * @author Evandrino Barros, Jônatas Tonholo
 */
public class QueryGroup {
    private int groupNumber;
    private HashMap<String, Integer> groupTerms;
    private int numberOfTerms;
    private int numberOfQueries = 0;
    private Stack<LCAStackNode> stack;
    private HashSet<Query> queryList;
    private HashMap<String,ArrayList<Query>> invertedList;
    // field for keeping the evaluated queries on group
    private HashSet<Query> evaluatedQuerySet;
    
    // added to count term occourencies
    private HashMap<String,Integer> termOccurrenceNumber;
    


    /**
     * Constructor of Query Group
     * @param groupNumber
     * @param allGroupTerms
     * @param queryList
     */
    public QueryGroup(int groupNumber, HashMap<String, Integer> allGroupTerms, ArrayList<Query> queryList) {
        this.groupNumber = groupNumber;
        this.groupTerms = allGroupTerms;
        this.queryList = new HashSet<Query>();
        this.queryList.addAll(queryList);
        Set termSet = groupTerms.keySet();
        numberOfTerms = termSet.size();
        this.invertedList = new HashMap<String, ArrayList<Query>>();
        this.numberOfQueries = queryList.size();
        this.stack = new Stack<LCAStackNode>();
        this.createInvertedList();
        this.setQueryBitMapsAndQueryBitPositions();
        this.evaluatedQuerySet = new HashSet<Query>();
        
        // added to count term occourencies
        this.termOccurrenceNumber = new HashMap<String, Integer>();
    }

    
    public void addOneToTermOccurrenceNumber(String term){
        //System.out.println("Query group number: "+this.groupNumber);
        //System.out.println("Inside addOneToTermOccurrenceNumber method.");
        if (this.termOccurrenceNumber.get(term) == null){
            //System.out.println("Didn't find a term query occurrence in data set for term: "+term);
         
            this.termOccurrenceNumber.put(term,1);
        }
        else {
            this.termOccurrenceNumber.put(term,this.termOccurrenceNumber.get(term)+1);
            //System.out.println("Find a term query occurrence in data set for term: "+term+
            //                   " - number of occurrences: "+this.termOccurrenceNumber.get(term));
        }
    }
    
    /**
     * Verify the Existence of the term
     * @param term
     * @return
     */
    public boolean verifyGetTermExistence (String term) {
        return (this.groupTerms.get(term) != null);
    }

    /**
     * Get the Term Position
     * @param term
     * @return
     */
    public int getTermPosition (String term) {
    	return groupTerms.get(term);
    }

    /**
     * Create the Inverted List
     */
    public void createInvertedList() {
        for (Query q : queryList) {
            String termList[] = q.getVectorOfQueryTerms();
            for(int i = 0; i< termList.length; i++) {
                ArrayList<Query> list = invertedList.get(termList[i]);
                if(list == null) {
                    list = new ArrayList<Query>();
                    invertedList.put(termList[i], list);
                }
                list.add(q);
            }
        }
    }

    /**
     * Add evaluated query to group
     */
    
    public void addEvaluatedQueryToGroup( HashSet<Query> SetOfQueries) {
        for (Query q : SetOfQueries) {
        	//System.out.println("q value"+q);
        	this.evaluatedQuerySet.add(q);
        	//System.out.println("Query been added: "+q.getQueryNumber());
        }
    }
    
    /**
     * return the number of evaluated query in the group
     */
    
    public int getAmountOfEvaluatedQueris() {
    	return evaluatedQuerySet.size();
    }
    
    
    /**
     * Add the Query to the Query List
     * @param query
     */
    
    public void addQueryToQueryList (Query query){
        if(!queryList.contains(query)) {
            queryList.add(query);
        }
    }

    /**
     * Set the Query Bitmaps and the Query Bit Positions
     */
    public void setQueryBitMapsAndQueryBitPositions () {
        for(Query q : queryList) {
            String[] queryTerms = q.getVectorOfQueryTerms();
            for (String term: queryTerms){
                Integer position = groupTerms.get(term);
                q.getTermPositions().add(position);
            }
        }
    }

    /**
     * Gets groupNumber.
     *
     * @return Value of groupNumber.
     */
    public int getGroupNumber() {
        return groupNumber;
    }

    /**
     * Gets groupTerms.
     *
     * @return Value of groupTerms.
     */
    public HashMap<String, Integer> getGroupTerms() {
        return groupTerms;
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
     * Gets invertedList.
     *
     * @return Value of invertedList.
     */
    public HashMap<String, ArrayList<Query>> getInvertedList() {
        return invertedList;
    }

    public HashMap<String,Integer> getTermOccurrenceNumberHashMap() {
        return this.termOccurrenceNumber;
    }


    
    /**
     * Gets stack.
     *
     * @return Value of stack.
     */
    public Stack<LCAStackNode> getStack() {
        return stack;
    }

    /**
     * Gets numberOfTerms.
     *
     * @return Value of numberOfTerms.
     */
    public int getNumberOfTerms() {
        return numberOfTerms;
    }
}
