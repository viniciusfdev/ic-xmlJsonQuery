package util;

import exception.MKStreamException;
import query.Query;

import java.util.*;

/**
 * Created by Jônatas on 02/12/2014.
 */
@Deprecated
public final class SearchUtils {
    /**
     * <b>Binary Search</b> <br>
     * This Method implements a Binary Search for the term passed in param
     * @param term
     * @param tagId
     * @param occurrenceList
     * @return
     * @throws MKStreamException
     */
    public static int binarySearch (String term, int tagId, ArrayList<Integer> occurrenceList) throws MKStreamException {
        int min = 0;
        int max;

        if (occurrenceList == null) {
            throw new MKStreamException("#Error: Occurrence List is null");
        }

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
     * <b>Binary Search</b> <br>
     * @param occurrenceList
     * @param firstPos
     * @param tagId
     * @return
     */
    public static int binarySearch(ArrayList<Integer> occurrenceList, int firstPos, Integer tagId) {
        int min = firstPos;
        int max;
        if (occurrenceList.isEmpty()) {
            max = 0;
        } else {
            max = occurrenceList.size() - 1;
        }
        int middle = (min + max) / 2;
        if (occurrenceList.size() != 0) {
            do {
                middle = (min + max) / 2;
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
     * <b>Count Elements Greater Or Equal Than @param term for the @param tagId</b><br>
     * @param term
     * @param tagId
     * @param pos
     * @param list
     * @return
     * @throws MKStreamException
     */
    public static int countElementsGreaterOrEqualThan (String term, Integer tagId, int pos, ArrayList<Integer> list) throws MKStreamException {
        int i = pos;
        int returnValue;

        if (list == null) {
            throw new MKStreamException("#Error: List is null");
        }

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
     * @param tagList
     * @return
     * @throws MKStreamException
     */
    public static int positionForFirstTagIdGreaterThan(String term, Integer tagId, int pos, ArrayList<Integer> tagList) throws MKStreamException {

        if(tagList == null) {
            throw new MKStreamException("#ERROR: TagList is null");
        }

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
}
/*

    @Deprecated
    public static int positionForFirstTagIdGreaterThan(String term, Integer tagId, ArrayList<Integer> tagList) throws MKStreamException{

        if(tagList == null) {
            throw new MKStreamException("#ERROR: TagList is null");
        }

        if (tagList.isEmpty()) {
            return 0;
        }

        // int posAtDocList = binarySearch(term, tagId); //FIXME: Verificar se está funcionando com a classe SearchUtils
        int posAtDocList = 0;

        try {
            posAtDocList = binarySearch(term, tagId, this.termOccur.get(term));
        } catch (MKStreamException e) {
            e.printStackTrace();
        }

        if (tagList.get(posAtDocList)<tagId){
            return posAtDocList + 1;
        }
        else {
            return posAtDocList;
        }
    }

    public static boolean EvaluateIfELCA_And_SetELCAOccurrences(Query query, int tagId, ArrayList<Integer> occurrenceList) throws MKStreamException {
        Set<Map.Entry<String, ArrayList<Integer>>> entries = query.termOccur.entrySet();
        boolean canBeELCA = true;
        HashMap<String,Integer> nextTagIdInDoc = new HashMap<String,Integer> (entries.size());
        HashMap<String,Integer> nextTagIdInQuery = new HashMap<String,Integer> (entries.size());
        for (Map.Entry<String, ArrayList<Integer>> entry : entries) {
            String term = entry.getKey();
            int posatdoc = binarySearch(term, tagId, occurrenceList);
            int occurTermDocum = countElementsGreaterOrEqualThan(term, tagId, posatdoc, occurrenceList);
            nextTagIdInDoc.put(term, positionForFirstTagIdGreaterThan(term, tagId, posatdoc, occurrenceList));
            int posatquery = binarySearch(term, tagId, query.getTermOccur().get(term));
            int occurTermQuery = countElementsGreaterOrEqualThan(term, tagId, posatquery, query.getTermOccur().get(term));
            nextTagIdInQuery.put(term, query.positionForFirstTagIdGreaterThan(term, tagId, posatquery));
            if (canBeELCA) {
                if (occurTermDocum - occurTermQuery == 0) {
                    canBeELCA = false;
                }
            }
        }
        if (canBeELCA) {
            //nova versao
            SetELCAOccurrences(query, tagId,nextTagIdInDoc, nextTagIdInQuery);
        }
        //System.out.println("Hash da consulta depois do set: "+query.quantOfOccur.toString());
        return (canBeELCA);
    }

    public static void SetSLCAOccurrences(Query query, Integer tagId, ArrayList<Integer> completeTagIdList) {
        Set<Map.Entry<String, ArrayList<Integer>>> entries = query.termOccur.entrySet();
        for (Map.Entry<String, ArrayList<Integer>> entry : entries) {
            String term = entry.getKey();
            int posAtDocList = positionForFirstTagIdGreaterThan(term, tagId);
            List<Integer> partialTagIdList = completeTagIdList.subList(posAtDocList,
                    completeTagIdList.size());
            HashSet<Integer> missingElements = new HashSet<Integer>();
            missingElements.addAll(partialTagIdList);
            ArrayList<Integer> allOccurInQuery = entry.getValue();

            int posAtQueryList = query.positionForFirstTagIdGreaterThan(term, tagId);
            List<Integer> partialOccurInQuery = allOccurInQuery.subList(posAtQueryList, allOccurInQuery.size());
            if (!partialOccurInQuery.isEmpty()) {
                missingElements.removeAll(partialOccurInQuery);
                for (Integer newTagId : missingElements) {
                    if (allOccurInQuery.get(0)>newTagId)
                        allOccurInQuery.add(0,newTagId) ;//less than first
                    else if (allOccurInQuery.get(allOccurInQuery.size()-1)<newTagId)
                        allOccurInQuery.add(allOccurInQuery.size(),newTagId);
                    else {
                        int insertPos = Util.binarySearch(allOccurInQuery, posAtQueryList,newTagId);
                        if (newTagId > allOccurInQuery.get(insertPos))
                            allOccurInQuery.add(insertPos + 1, newTagId);
                        else
                            allOccurInQuery.add(insertPos, newTagId);
                    }
                }

            } else {
                for (Integer i : missingElements) {
                    int insertPos;
                    if (allOccurInQuery.isEmpty())
                        insertPos=0;
                    else
                        insertPos= query.positionForFirstTagIdGreaterThan(term,tagId);
                    allOccurInQuery.add(insertPos,i);
                }
            }
        }
    }
}
*/