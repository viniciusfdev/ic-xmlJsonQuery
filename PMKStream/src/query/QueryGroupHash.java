package query;

import exception.MKStreamException;

import java.util.*;

/**
 * @author Evandrino Barros, Jônatas Tonholo
 * @version 2.0
 */
public class QueryGroupHash {
    private HashMap<String, ArrayList<QueryGroup>> groupHash;
    private int numberOfGroups;
    private int eachGroupSize;
    private boolean ELCASemantics = false;
    private ArrayList<QueryGroup> groupList;
    private ArrayList<Query> completeListOfQueries;

    /**
     * Constructor of the Query Group Hash
     *
     * @param ELCASemantics
     */
    public QueryGroupHash(boolean ELCASemantics) {
        this.ELCASemantics = ELCASemantics;
        this.groupList = new ArrayList<QueryGroup>();
        this.completeListOfQueries = new ArrayList<Query>();
    }

    /**
     * Verify if the Query contains the Term
     *
     * @param query
     * @param term
     * @return
     */
    
    public ArrayList<QueryGroup> getGroupList(){
        return this.groupList;
    }
    
    public boolean queryContainsTerm(String query, String term) {
        boolean termInQuery = false;
        String allTerms[] = query.trim().split(" "); //TODO: verify performance with ArrayList
        for (String queryTerm : allTerms) {
            if (term.equals(queryTerm)) {
                termInQuery = true;
                break;
            }
        }
        return termInQuery;
    }

    /**
     * Verify if the QueryA contains shared Terms with the QueryB
     *
     * @param queryA
     * @param queryB
     * @return
     */
    public boolean doTheyHaveSharedTerms(String queryA, String queryB) {
        String vetTermsA[] = queryA.trim().split(" ");
        String vetTermsB[] = queryB.trim().split(" ");
        boolean oneQueryContainsTheOther = true;
        if (queryA.length() >= queryB.length()) {
            for (String termB : vetTermsB) {
                if (!queryContainsTerm(queryA, termB)) {
                    oneQueryContainsTheOther = false;
                    break;
                }
            }
        } else {
            for (String termA : vetTermsA) {
                if (!queryContainsTerm(queryB, termA)) {
                    oneQueryContainsTheOther = false;
                    break;
                }
            }
        }
        return oneQueryContainsTheOther;
    }

    /**
     * Verify if GroupTerms contains any Common Terms with the NewQuery
     *
     * @param groupTerms
     * @param newQuery
     * @return
     */
    public boolean anyCommonTerms(String groupTerms, String newQuery) {
        String vetNewTerms[] = newQuery.trim().split(" ");
        for (String term : vetNewTerms) {
            if (groupTerms.contains(term)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the smaller group number
     *
     * @param tempGroupHash
     * @param maxAmountInGroup
     * @return
     */
    public int smallestGroup(HashMap<Integer, ArrayList<Query>> tempGroupHash, int maxAmountInGroup) {
        int minSize = maxAmountInGroup;
        int i = 1;
        Set<Map.Entry<Integer, ArrayList<Query>>> groups = tempGroupHash.entrySet();
        for (Map.Entry<Integer, ArrayList<Query>> group : groups) {
            int groupNumber = ((Integer) group.getKey()).intValue();

            ArrayList<Query> queryList = group.getValue();
            if (queryList.size() < minSize) {
                minSize = queryList.size();
                i = groupNumber;
            }
        }
        return i;
    }

    /**
     * Put the Query in the Hash Term
     * TODO: Verificar funcionamento em Debug
     *
     * @param groupTerms
     * @param groupNumber
     * @param queryString
     * @param size
     */
    public void putQueryInHashTerm(HashMap<Integer, HashMap<String, Integer>> groupTerms,
                                   Integer groupNumber, String queryString, int size) {

        HashMap<String, Integer> hashTerm = groupTerms.get(groupNumber);

        if (hashTerm == null) {
            hashTerm = new HashMap<String, Integer>();
            String[] terms = queryString.split(" ");
            for (int i = 0; i < terms.length; i++) {
                hashTerm.put(terms[i], i);
            }
            groupTerms.put(groupNumber, hashTerm);
        } else {
            Set<String> setTerms = hashTerm.keySet();
            String[] terms = queryString.split(" ");
            int setSize = setTerms.size();
            for (int i = 0; i < terms.length; i++)
                if (!hashTerm.containsKey(terms[i])) {
                    hashTerm.put(terms[i], setSize);
                    setSize++;
                }
            groupTerms.put(groupNumber, hashTerm);
        }
    }

    /**
     * Put the Query into the Group represented by the tempGroupHash
     *
     * @param tempGroupHash
     * @param group
     * @param newQueryNumber
     * @param queryString
     */
    public void putQueryInGroup(HashMap<Integer, ArrayList<Query>> tempGroupHash,
                                int group, int newQueryNumber, String queryString) {

        ArrayList<Query> tempQueryList = tempGroupHash.get(group);
        Query queryEntry = new Query(newQueryNumber, group, queryString, this.ELCASemantics);
        tempQueryList.add(queryEntry);
    }
    /**
     * Verify if HashTerms contains any Common Terms with the NewQuery
     *
     * @param hashTerms
     * @param newQuery
     * @return
     */
    public boolean anyCommonTerms(HashMap<String, Integer> hashTerms, String newQuery) {
        String vetNewTerms[] = newQuery.trim().split(" ");
        if (hashTerms != null) {
            for (String term : vetNewTerms) {
                if (hashTerms.containsKey(term)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Group Query With Queries With Any Common Terms
     * TODO: Debug para verificar o que faz o método
     * @param queries
     * @param maxQuantOfGroups
     * @param quantMaxPerGroup
     * @throws MKStreamException
     */
    public void groupQueryWithQueriesWithAnyCommonTerms(ArrayList<String> queries,
                                                        long maxQuantOfGroups, int quantMaxPerGroup) throws MKStreamException {

        if (queries == null) {
            throw new MKStreamException ("#ERROR: Queries can't be null");
        }

        if(queries.isEmpty()) {
            throw  new MKStreamException("#ERROR: The number of Queries must be bigger than zero");
        }

        this.eachGroupSize = quantMaxPerGroup;
        this.numberOfGroups = Math.round(maxQuantOfGroups);
        int size = queries.size();

        this.groupHash = new HashMap<String, ArrayList<QueryGroup>> (2 * queries.size(), 1.0f);

        //TODO: Debug
        HashMap<Integer, HashMap<String, Integer>> groupTerms =
                new HashMap<Integer, HashMap<String, Integer>>(2 * Math.round(maxQuantOfGroups), 1.0f);

        int groupNumber = 1;
        String queryString = queries.get(0).trim();
        putQueryInHashTerm(groupTerms, groupNumber, queryString, size);

        HashMap<Integer, ArrayList<Query>> tempGroupHash =
                new HashMap<Integer, ArrayList<Query>>(2 * Math.round(maxQuantOfGroups), 1.0f);

        ArrayList<Query> queryList = new ArrayList<Query>();

        int queryNumber = 1;
        Query firstQueryInGroup = new Query(queryNumber, groupNumber, queryString, this.ELCASemantics);
        //this.completeListOfQueries.add(Query);

        queryList.add(firstQueryInGroup);
        tempGroupHash.put(groupNumber, queryList);

        int currentGroupNumber;
        int newGroupNumber = 2;
        int newQueryNumber = 1;

        for (int n = 1; n < queries.size(); n++) {
            newQueryNumber = n + 1;
            queryString = queries.get(n);
            Set queryKeywordSet = tempGroupHash.entrySet();
            Iterator iterator = queryKeywordSet.iterator();
            currentGroupNumber = 1;
            boolean newGroup = true;
            while (iterator.hasNext()) {
                Map.Entry m = (Map.Entry) iterator.next();
                groupNumber = (Integer) m.getKey();

                if (this.anyCommonTerms(groupTerms.get(groupNumber), queryString)) {
                    ArrayList<Query> queriesInGroup = tempGroupHash.get(groupNumber);
                    if (queriesInGroup.size() < quantMaxPerGroup) {
                        currentGroupNumber = groupNumber;
                        newGroup = false;
                    } 
                }
            }

            if (newGroup) {
                if (newGroupNumber <= maxQuantOfGroups) { // not reached the maximum number of groups
                    ArrayList<Query> newGroupQueryList = new ArrayList<Query>();
                    tempGroupHash.put(newGroupNumber, newGroupQueryList);
                    putQueryInHashTerm(groupTerms, newGroupNumber, queryString, size);
                    putQueryInGroup(tempGroupHash, newGroupNumber, newQueryNumber, queryString);
                    newGroupNumber++;
                } else { // reached the maximum number of groups: search for a group with the lowest space group
                    int smallest = smallestGroup(tempGroupHash, quantMaxPerGroup);
                    putQueryInHashTerm(groupTerms, smallest, queryString, size);
                    putQueryInGroup(tempGroupHash, smallest, newQueryNumber, queryString);
                }
            } else {
                putQueryInHashTerm(groupTerms, currentGroupNumber, queryString, size);
                putQueryInGroup(tempGroupHash, currentGroupNumber, newQueryNumber, queryString);
            }
        }

        Set<Map.Entry<Integer,ArrayList<Query>>> groups = tempGroupHash.entrySet();
        for (Map.Entry<Integer, ArrayList<Query>> group : groups){
            groupNumber = ((Integer) group.getKey()).intValue();
            queryList = group.getValue();
            QueryGroup newQueryGroup = new QueryGroup(groupNumber, groupTerms.get(groupNumber), queryList);
            this.groupList.add(newQueryGroup);
            Set<String> termsInTheGroup = groupTerms.get(groupNumber).keySet();
            for (String term : termsInTheGroup) {
                ArrayList<QueryGroup> groupListPerTerm = this.groupHash.get(term);
                if (groupListPerTerm != null) {
                    groupListPerTerm.add(newQueryGroup);
                }
                else{
                    groupListPerTerm = new ArrayList<QueryGroup>(this.numberOfGroups);
                    groupListPerTerm.add(newQueryGroup);
                    this.groupHash.put(term, groupListPerTerm);
                }
            }
        }
    }

    /**
     * Print the Query Group Hash
     */
    public void print(){
        Set<Map.Entry<String,ArrayList<QueryGroup>>> groups = this.groupHash.entrySet();

        for (Map.Entry<String, ArrayList<QueryGroup>> group: groups){
            System.out.println("Termo: "+group.getKey());

            for (QueryGroup queryGroup : group.getValue()){
                System.out.println("  Grupo: " + queryGroup.getGroupNumber());
                System.out.println("    Termos do grupo: " + queryGroup.getGroupTerms());
                System.out.println("    Consultas: ");

                for (Query query: queryGroup.getQueryList()) {
                    System.out.println("      Num consulta: " + query.getQueryNumber() + " - Termos: " + query.getQueryTerms() +
                            " - posicoes: " + query.getTermPositions().toString());
                }

                //Set<Map.Entry<String,ArrayList<Query>>> entries = queryGroup.getInvertedList().entrySet();
                //for (Map.Entry<String, ArrayList<Query>> entry: entries){
                //    System.out.println("    Lista Invertida: ");
                //    System.out.print("       termo: "+entry.getKey()+" - consultas: " );
                //    for (Query query: entry.getValue()){
                //        System.out.print(query.getQueryNumber()+ ",");
                //    }
                //    System.out.println();
                //}
            }
            System.out.println();
        }
    }

    

    
   /* public void printHowManyQueriesPerTerm(){
        Set<Map.Entry<String,ArrayList<QueryGroup>>> groups = this.groupHash.entrySet();
        for (QueryGroup queryGroup : this.groupList) {
            System.out.println("  Grupo: " + queryGroup.getGroupNumber());
            for (Query query: queryGroup.getQueryList()) {
                System.out.println("      Num consulta: " + query.getQueryNumber() + " - Number of Comparisons: " + query.getNumberOfComparisons());
            }
        }
    }*/

    
    
    
    
    public void printHowManyQueriesPerTerm(){
        //Set<Map.Entry<String,ArrayList<QueryGroup>>> groups = this.groupHash.entrySet();
        for (QueryGroup queryGroup : this.groupList) {
            //System.out.println("Group number: "+queryGroup.getGroupNumber());
            Set<Map.Entry<String,ArrayList<Query>>> invList = queryGroup.getInvertedList().entrySet();
            //System.out.println("  Term: " + invList.getKey());
            System.out.println("Query terms occurrences in list of queries: ");
            System.out.println("    Number of distinct terms : "+invList.size());
            for (Map.Entry<String, ArrayList<Query>> entry: invList) {
                //if (entry.getValue().size()>1)
                    //System.out.println("Term: " + entry.getKey() + " - Number of Queries: " + entry.getValue().size());
                System.out.println(entry.getKey() + ";" + entry.getValue().size());
                
                                       
                                       
            }
        }
    }
    
    
    public void printHowManyOccurencesPerTermInDataSet(){
        //Set<Map.Entry<String,ArrayList<QueryGroup>>> groups = this.groupHash.entrySet();
        for (QueryGroup queryGroup : this.groupList) {
            System.out.println("Group number: "+queryGroup.getGroupNumber());
            Set<Map.Entry<String,Integer>> termInDataSetList = queryGroup.getTermOccurrenceNumberHashMap().entrySet();
            if (termInDataSetList==null)
                System.out.print("Value of termInDataSetList: null");
            //System.out.println("  Term: " + invList.getKey());
            System.out.println("Query terms occurrences in dataset: ");
            System.out.println("    Number of distinct terms in occuring in data set: "+termInDataSetList.size());
            for (Map.Entry<String, Integer> entry: termInDataSetList) {
                //if (entry.getValue().size()>1)
                //System.out.println("Term: " + entry.getKey() + " - Number of Queries: " + entry.getValue().size());
                System.out.println(entry.getKey() + ";" + entry.getValue());
            }
        }
    }
    
    

    
    
    public void printHowManyOccurencesPerTermInQueriesAndDataSets(){
        //Set<Map.Entry<String,ArrayList<QueryGroup>>> groups = this.groupHash.entrySet();
        for (QueryGroup queryGroup : this.groupList) {
            //System.out.println("Group number: "+queryGroup.getGroupNumber());
            Set<Map.Entry<String,ArrayList<Query>>> invList = queryGroup.getInvertedList().entrySet();
            //Set<Map.Entry<String,Integer>> termInDataSetList = queryGroup.getTermOccurrenceNumberHashMap().entrySet();
            //System.out.println("  Term: " + invList.getKey());
            System.out.println("Query terms occurrences in list of queries: ");
            System.out.println("    Number of distinct terms : "+invList.size());
            for (Map.Entry<String, ArrayList<Query>> entry: invList) {
                //if (entry.getValue().size()>1)
                //System.out.println("Term: " + entry.getKey() + " - Number of Queries: " + entry.getValue().size());
                Integer numberOfOccurrences = (queryGroup.getTermOccurrenceNumberHashMap().get(entry.getKey())==null)?0:
                queryGroup.getTermOccurrenceNumberHashMap().get(entry.getKey());
                System.out.println(entry.getKey() + ";" + entry.getValue().size()+";"+numberOfOccurrences);
                //System.out.println(";"+numberOfOccurrences);
            }
        }
    }
    
    
    
    public void printQueries(){
        Set<Map.Entry<String,ArrayList<QueryGroup>>> groups = this.groupHash.entrySet();
            for (QueryGroup queryGroup : this.groupList) {
                System.out.println("  Grupo: " + queryGroup.getGroupNumber());
                for (Query query: queryGroup.getQueryList()) {
                    System.out.println("      Num consulta: " + query.getQueryNumber() + " - Number of Comparisons: " + query.getNumberOfComparisons());
                }
            }
    }
    
    
    public long getTotalNumberOfQueryComparisons(){
        long numberOfQueryComparisons = 0;
        for (QueryGroup queryGroup : this.groupList) {
            for (Query query: queryGroup.getQueryList()) {
                numberOfQueryComparisons = numberOfQueryComparisons+query.getNumberOfComparisons();
            }
        }
        return numberOfQueryComparisons;
    }
    
    public int getNumberOfEvaluatedQueries(){
        int numberOfEvaluatedQueries = 0;
        for (QueryGroup queryGroup : this.groupList) {
            for (Query query: queryGroup.getQueryList()) {
                if (query.getNumberOfComparisons()>0)
                    numberOfEvaluatedQueries++;
            }
        }
        return numberOfEvaluatedQueries;
    }
    
    
    
    public int getNumberOfGroups() {
		return numberOfGroups;
	}

	public void setNumberOfGroups(int numberOfGroups) {
		this.numberOfGroups = numberOfGroups;
	}

	public int getEachGroupSize() {
		return eachGroupSize;
	}

	public void setEachGroupSize(int eachGroupSize) {
		this.eachGroupSize = eachGroupSize;
	}

	public void setGroupHash(HashMap<String, ArrayList<QueryGroup>> groupHash) {
		this.groupHash = groupHash;
	}

	/**
     * Gets groupList.
     *
     * @return Value of groupList.
     */
    //public ArrayList<QueryGroup> getGroupList() {
    //    return groupList;
    //}

    /**
     * Sets new groupList.
     *
     * @param groupList New value of groupList.
     */
    public void setGroupList(ArrayList<QueryGroup> groupList) {
        this.groupList = groupList;
    }

    
    
    /**
     * Gets groupHash.
     *
     * @return Value of groupHash.
     */
    public HashMap<String, ArrayList<QueryGroup>> getGroupHash() {
        return groupHash;
    }
    
    /**
     * Returns the amount of queries evaluated in all query groups that belongs to this QueryGroupHash
     * 
     */
    public int getTotalAmountOfEvalutedQueries() {
    	int i=0;
    	for (QueryGroup qg: groupList)
    		i= i + qg.getAmountOfEvaluatedQueris();
    	return i;
    } 	
}
