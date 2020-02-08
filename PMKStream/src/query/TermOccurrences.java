package query;

import util.Util;

import java.util.*;

/**
 * @author Evandrino Barros, Jônatas Tonholo
 * @version 2.0
 */
public class TermOccurrences {
    private HashMap<String, ArrayList<Integer>> occurrenceHash;

    /**
     * Default Constructor
     */
    public TermOccurrences() {
        occurrenceHash = new HashMap<String, ArrayList<Integer>>();
    }

    /**
     * Constructor
     *
     * @param qgh
     * @param numberOfQueries
     */
    public TermOccurrences(QueryGroupHash qgh, int numberOfQueries) {
        this.occurrenceHash = new HashMap<String, ArrayList<Integer>>(numberOfQueries * 2, 1.0f);
        for (QueryGroup group : qgh.getGroupList()) {
            for (String term : group.getGroupTerms().keySet()) {
                if (!this.occurrenceHash.containsKey(term)) {
                    this.occurrenceHash.put(term, new ArrayList<Integer>());
                }
            }
        }
    }

    /**
     * Register the Occurrences
     *
     * @param term
     * @param tagId
     */
    public void registerOccurence(String term, Integer tagId) {
        ArrayList<Integer> occurrenceList = this.occurrenceHash.get(term);
        if (!occurrenceList.isEmpty()) {
            int pos = occurrenceList.size() - 1;
            if (occurrenceList.get(pos) < tagId)
                occurrenceList.add(tagId);
        } else {
            occurrenceList.add(tagId);
        }
    }

    /**
     * Returns the size of the Occurrence Hash
     *
     * @param term
     * @return
     */
    public int getListSize(String term) {
        return (this.occurrenceHash.get(term).size());
    }

    /**
     * Clean the Occurrences in the Term Occurrences Hash
     */
    public void cleanOccurrences() {
        Set<Map.Entry<String, ArrayList<Integer>>> termSet = this.occurrenceHash.entrySet();
        for (Map.Entry<String, ArrayList<Integer>> entry : termSet) {
            occurrenceHash.put(entry.getKey(), new ArrayList<Integer>());
        }
    }

    /**
     * Print the Term Occurrences Hash
     */
    public void print() {
        Set<Map.Entry<String, ArrayList<Integer>>> termSet = this.occurrenceHash.entrySet();
        for (Map.Entry<String, ArrayList<Integer>> entry : termSet) {
            System.out.print("Term: " + entry.getKey() + " - #Ocorrencias: " + entry.getValue().size() + " - {");
            for (Integer i : entry.getValue()) {
                System.out.print(i + ", ");
            }
            System.out.println("}");
        }
    }

    /**
     * <b>Binary Search</b> <br>
     * This Method implements a Binary Search for the term passed in param
     * FIXME: Refatorar para uma classe util. Existem outras classes com Binary Search
     *
     * @param term
     * @param tagId
     * @return
     */
    public int binarySearch(String term, Integer tagId) {
        int min = 0;
        ArrayList<Integer> occurrenceList = this.occurrenceHash.get(term);
        int max = occurrenceList.size() == 0 ? 0 : occurrenceList.size() - 1;
        int middle = (min + max) / 2;
        if (occurrenceList.size() != 0) {
            do {
                middle = (min + max) / 2;
                //System.out.println("Min: "+min+" - Max: "+max+" - Mid: "+mid);
                if (tagId > occurrenceList.get(middle)) {
                    min = middle + 1;
                } else {
                    max = middle - 1;
                }

            } while ((tagId != occurrenceList.get(middle)) && (min <= max));
        }
        //System.out.println("Returned value="+mid);
        return middle;
    }

    /**
     * Count Elements Greater or Equal Than Term
     * FIXME: Refatorada para SearchUtils
     *
     * @param term
     * @param tagId
     * @param pos
     * @return
     */
    public int countElementsGreaterOrEqualThan(String term, Integer tagId, int pos) {
        ArrayList<Integer> list = this.occurrenceHash.get(term);
        int i = pos;
        int returnValue;
        if (list.get(i) == tagId) {
            returnValue = list.size() - i;
        } else if ((i + 1) == list.size()) {
            if (tagId < list.get(list.size() - 1)) //tagId less than last element
            {
                returnValue = 1;
            } else {
                returnValue = 0;   //tagId greater than last element
            }
        } else if (i == 0) {         //tagId less than 1st element
            if (tagId > list.get(i)) {
                returnValue = (list.size() - 1);
            } else {
                returnValue = (list.size());
            }
        } else {
            if (list.get(i) < tagId)
                i = i + 1;
            returnValue = (list.size() - i);
        }
        return returnValue;
    }

    /**
     * Get the position for the first TagId Greater than the term
     * FIXME: refatorar para uma classe util
     *
     * @param term
     * @param tagId
     * @param pos
     * @return
     */
    public int positionForFirstTagIdGreaterThan(String term, Integer tagId, int pos) {
        int posAtDocList = pos;
        ArrayList<Integer> tagList = this.occurrenceHash.get(term);
        if (tagList.get(posAtDocList) < tagId) {
            return posAtDocList + 1;
        } else {
            return posAtDocList;
        }
    }

    public int positionForFirstTagIdGreaterThan(String term, Integer tagId) {
        int posAtDocList = this.binarySearch(term, tagId);
        ArrayList<Integer> tagList = this.occurrenceHash.get(term);
        if (tagList.get(posAtDocList) < tagId) {
            return posAtDocList + 1;
        } else {
            return posAtDocList;
        }
    }

    /**
     * Evaluate If ELCA And Set ELCA Occurrences
     * FIXME: Refatorada para ELCA Utils
     * TODO: Debug
     *
     * @param query
     * @param tagId
     * @return
     */
    public boolean EvaluateIfELCA_And_SetELCAOccurrences(Query query, int tagId) {
        Set<Map.Entry<String, ArrayList<Integer>>> entries = query.getTermOccur().entrySet();
        boolean canBeELCA = true;
        HashMap<String, Integer> nextTagIdInDoc = new HashMap<String, Integer>(entries.size());
        HashMap<String, Integer> nextTagIdInQuery = new HashMap<String, Integer>(entries.size());
        for (Map.Entry<String, ArrayList<Integer>> entry : entries) {
            String term = entry.getKey();
            int posAtDoc = this.binarySearch(term, tagId);
            int occurTermDocum = this.countElementsGreaterOrEqualThan(term, tagId, posAtDoc);
            nextTagIdInDoc.put(term, this.positionForFirstTagIdGreaterThan(term, tagId, posAtDoc));
            int posAtQuery = query.binarySearch(term, tagId);
            int occurTermQuery = query.countElementsGreaterOrEqualThan(term, tagId, posAtQuery);
            nextTagIdInQuery.put(term, query.positionForFirstTagIdGreaterThan(term, tagId, posAtQuery));
            if (canBeELCA) {
                if (occurTermDocum - occurTermQuery == 0) {
                    canBeELCA = false;
                }
            }
        }
        if (canBeELCA) {
            //nova versao
            SetELCAOccurrences(query, nextTagIdInDoc, nextTagIdInQuery);
        }
        return (canBeELCA);
    }

    /**
     * Set SLCA Occurrences
     * TODO: Debug
     *
     * @param query
     * @param tagId
     */
    public void SetSLCAOccurrences(Query query, Integer tagId) {
        Set<Map.Entry<String, ArrayList<Integer>>> entries = query.getTermOccur().entrySet();
        for (Map.Entry<String, ArrayList<Integer>> entry : entries) {
            String term = entry.getKey();
            ArrayList<Integer> completeTagIdList = this.occurrenceHash.get(term);
            int posAtDocList = this.positionForFirstTagIdGreaterThan(term, tagId);
            List<Integer> partialTagIdList = completeTagIdList.subList(posAtDocList, completeTagIdList.size());
            HashSet<Integer> missingElements = new HashSet<Integer>();
            missingElements.addAll(partialTagIdList);
            ArrayList<Integer> allOccurInQuery = entry.getValue();

            int posAtQueryList = query.positionForFirstTagIdGreaterThan(term, tagId);
            List<Integer> partialOccurInQuery = allOccurInQuery.subList(posAtQueryList, allOccurInQuery.size());
            if (!partialOccurInQuery.isEmpty()) {
                missingElements.removeAll(partialOccurInQuery);
                for (Integer newTagId : missingElements) {
                    if (allOccurInQuery.get(0) > newTagId) {
                        allOccurInQuery.add(0, newTagId);//less than first
                    } else if (allOccurInQuery.get(allOccurInQuery.size() - 1) < newTagId) {
                        allOccurInQuery.add(allOccurInQuery.size(), newTagId);
                    } else {
                        int insertPos = Util.binarySearch(allOccurInQuery, posAtQueryList, newTagId);
                        if (newTagId > allOccurInQuery.get(insertPos)) {
                            allOccurInQuery.add(insertPos + 1, newTagId);
                        } else {
                            allOccurInQuery.add(insertPos, newTagId);
                        }
                    }
                }
            } else {
                for (Integer i : missingElements) {
                    int insertPos;
                    if (allOccurInQuery.isEmpty())
                        insertPos = 0;
                    else
                        insertPos = query.positionForFirstTagIdGreaterThan(term, tagId);
                    allOccurInQuery.add(insertPos, i);
                }
            }
        }
    }

    /**
     * Set SLCA Occurrences
     * TODO: Debug
     *
     * @param query
     * @param nextTagIdInDoc
     * @param nextTagIdInQuery
     */
    public void SetELCAOccurrences(Query query,
                                   HashMap<String, Integer> nextTagIdInDoc,
                                   HashMap<String, Integer> nextTagIdInQuery) {

        Set<String> terms = nextTagIdInDoc.keySet();
        for (String term : terms) {
            ArrayList<Integer> completeTagIdList = this.occurrenceHash.get(term);
            int posAtDocList = nextTagIdInDoc.get(term);
            List<Integer> partialDocTagIdList = completeTagIdList.subList(posAtDocList, completeTagIdList.size());
            HashSet<Integer> missingTagIds = new HashSet<Integer>();
            missingTagIds.addAll(partialDocTagIdList);
            ArrayList<Integer> allOccurInQuery = query.getTermOccur().get(term);
            int posAtQueryList = nextTagIdInQuery.get(term);
            List<Integer> partialQueryTagIdList = allOccurInQuery.subList(posAtQueryList, allOccurInQuery.size());
            missingTagIds.removeAll(partialQueryTagIdList);
            for (Integer newTagId : missingTagIds) {
                if (newTagId < allOccurInQuery.get(0)) //less than first
                    allOccurInQuery.add(0, newTagId);
                else if (newTagId > allOccurInQuery.get(allOccurInQuery.size() - 1)) //greater than last
                    allOccurInQuery.add(allOccurInQuery.size(), newTagId);
                else {
                    int newPos = Util.binarySearch(allOccurInQuery, posAtQueryList, newTagId);
                    if (newTagId > allOccurInQuery.get(newPos))
                        allOccurInQuery.add(newPos + 1, newTagId);
                    else
                        allOccurInQuery.add(newPos, newTagId);
                }
            }
        }
    }

}
