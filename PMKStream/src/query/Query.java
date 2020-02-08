package query;

import node.ResultNode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @version 2.0
 * @author Evandrino Barros, Jônatas Tonholo
 */
public class Query {
    private int queryNumber;
    private String queryTerms;
    private String[] vectorOfQueryTerms;
    private int groupNumber;
    private int numberOfTerms;
    private ArrayList<Integer> termPositions;
    private ArrayList<ResultNode> resultList;
    private HashMap<String, ArrayList<Integer>> termOccur;
    private long numberOfComparisons;

    /**
     * Construtor padrão.
     *
     * @param queryNumber
     * @param groupNumber
     * @param queryTerms
     * @param ELCASemantics
     */
    public Query (int queryNumber, int groupNumber, String queryTerms, boolean ELCASemantics) {
        this.queryNumber = queryNumber;
        this.groupNumber = groupNumber;
        this.queryTerms = queryTerms;
        this.vectorOfQueryTerms = queryTerms.trim().split(" "); //TODO observe
        this.numberOfTerms = vectorOfQueryTerms.length;
        this.termPositions = new ArrayList<Integer>();
        this.resultList = new ArrayList<ResultNode>();
        this.numberOfComparisons = 0;

        if(ELCASemantics) {
            this.termOccur = new HashMap<String, ArrayList<Integer>>();
            for(String term : vectorOfQueryTerms) {
                this.termOccur.put(term, new ArrayList<Integer>());
            }
        }
    }

    public long getNumberOfComparisons (){
        return numberOfComparisons;
    }
    
    public void addOneToNumberOfComparisons(){
        this.numberOfComparisons++;
    }


    /**
     * <b>Binary Search</b> <br>
     * This Method implements a Binary Search for the term passed in param
     *
     * TODO: talvez esteja na classe errada
     * TODO: verificar o porquê do tagID
     * TODO: Verificar se está sendo chamada
     * @param term
     * @param tagId
     * @return
     */
    public int binarySearch(String term, int tagId) {
        int min = 0;
        int max;
        ArrayList<Integer> occurrenceList = this.termOccur.get(term);
        max = occurrenceList.size() == 0 ? 0 : occurrenceList.size() - 1;

        int middle = (min+max)/2;
        if(occurrenceList.size() != 0) {
            do {
                middle = (min+max)/2;
                if(tagId > occurrenceList.get(middle)) {
                    min = middle + 1;
                } else {
                    max = middle - 1;
                }
            }while((tagId != occurrenceList.get(middle)) && (min <= max));
        }
        return  middle;
    }

    /**
     * <b>Count Elements Greater Or Equal Than @param term for the @param tagId</b><br>
     * TODO: verificar se está na classe correta
     * TODO: Verificar funcionamento do método quando for chamado
     * TODO: Verificar se está sendo chamada
     * @param term
     * @param tagId
     * @param pos
     * @return
     */
    public int countElementsGreaterOrEqualThan (String term, Integer tagId, int pos) {
        ArrayList<Integer> list = this.termOccur.get(term);
        int i = pos;
        int returnValue;

        if (list.get(i) == tagId) {
            returnValue = list.size() - 1;
        } else if (list.size() == (i+1)) {
            if (tagId < list.get(list.size() - 1)) {   //tagId less than last element
                returnValue = 1;
            } else {        //tagId greater than last element
                returnValue= 0;
            }
        } else if (i == 0){     //tagId less than 1st element
            if (tagId > list.get(i)) {
                returnValue = (list.size() - 1);
            } else {
                returnValue= (list.size());
            }
        } else {
            if (list.get(i) < tagId) {
                i++;
            }
            returnValue = (list.size() - i);
        }
        return returnValue;
    }

    /**
     * <b>Return the Position For the First TagId Greater Than @param term and @param tagId passed</b><br>
     * TODO: Verificar funcionamento quando for chamada
     * TODO: Verificar se está sendo utilizada, ou se é a versão original
     * @param term
     * @param tagId
     * @param pos
     * @return
     */
    public int positionForFirstTagIdGreaterThan(String term, Integer tagId, int pos) {
        ArrayList<Integer> tagList = this.termOccur.get(term);

        if (tagList.isEmpty()) {
            return 0;
        }

        int posAtDocList = pos;
        if (tagList.get(posAtDocList) < tagId) {
            return posAtDocList + 1;
        } else {
            return posAtDocList;
        }
    }

    /**
     * <b>Return the Position For the First TagId Greater Than @param term and @param tagId passed</b><br>
     * <i>Versão original</i>
     * TODO: verificar se está sendo utilizado.
     * @deprecated
     * @param term
     * @param tagId
     * @return
     */
    @Deprecated
    public int positionForFirstTagIdGreaterThan(String term, Integer tagId) {
        ArrayList<Integer> tagList = this.termOccur.get(term);

        if (tagList.isEmpty()) {
            return 0;
        }

        int posAtDocList = this.binarySearch(term, tagId);
        if (tagList.get(posAtDocList)<tagId){
            return posAtDocList + 1;
        }
        else {
            return posAtDocList;
        }
    }

    /**
     * Get the position in the ArrayList of Term Positions
     * TODO: Verificar se está sendo utilizado
     * @return positions
     */
    public String getPositions() {
        String positions = new String("{");
        for(Integer i : this.termPositions) {
            positions = positions.concat(i.intValue() + " ");
        }
        positions = positions.trim().concat("}");
        return positions;
    }
    /**
     *
     * @return numberOfTerms
     */
    public int getNumberOfTerms() {
        return numberOfTerms;
    }

    /**
     *
     * @return groupNumber
     */
    public int getGroupNumber() {
        return groupNumber;
    }

    /**
     *
     * @return queryNumber
     */
    public int getQueryNumber() {
        return queryNumber;
    }

    /**
     *
     * @return queryTerms
     */
    public String getQueryTerms() {
        return queryTerms;
    }

    /**
     *
     * @return vectorOfQueryTerms
     */
    public String[] getVectorOfQueryTerms() {
        return vectorOfQueryTerms;
    }

    public String getQueryTermString(){
        String temp = new String();
        for (String str: vectorOfQueryTerms){
            temp = temp+" "+str;
        }
        return temp;
    }
    
    /**
     * Gets The Term Positions
     * @return
     */
    public ArrayList<Integer> getTermPositions() {
        return termPositions;
    }

    /**
     * Gets resultList.
     *
     * @return Value of resultList.
     */
    public ArrayList<ResultNode> getResultList() {
        return resultList;
    }

    /**
     * Sets new resultList.
     *
     * @param resultList New value of resultList.
     */
    public void setResultList(ArrayList<ResultNode> resultList) {
        this.resultList = resultList;
    }

    /**
     * Gets termOccur.
     *
     * @return Value of termOccur.
     */
    public HashMap<String, ArrayList<Integer>> getTermOccur() {
        return termOccur;
    }

    /**
     * Sets new termOccur.
     *
     * @param termOccur New value of termOccur.
     */
    public void setTermOccur(HashMap<String, ArrayList<Integer>> termOccur) {
        this.termOccur = termOccur;
    }
}
