/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupwithcommonterms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author vinicius
 */
public class GroupWithCommonTerms {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        BufferedReader queryFile = new BufferedReader(new FileReader(new File("src/xmldoc.txt").getAbsolutePath()));
        List<List<Query>> queryGroup = new ArrayList<>();
        List<Query> queries = new ArrayList<>();
        Query queryAux = null;
        int countQueries = 0;
        int nThreads = 2;
        int index = 0;
        int aux = 0;
        int i = 1;
        
        String line = "";
        while((line = queryFile.readLine()) != null){
            queries.add(new Query(i++, Arrays.asList(line.split("\\s+"))));
            countQueries++;
        }
        int nQueriesPerGroup = countQueries/nThreads;
        
        queryGroup.add(new ArrayList<>());
        queryGroup.get(index).add(queries.get(0).clone());
        queries.remove(0);
        
        while(countOverallSize(queryGroup) != countQueries){
            Query query = queryGroup.get(index).get(queryGroup.get(index).size()-1);
            if(queryGroup.get(index).size() == nQueriesPerGroup && index+1 < nThreads){
                queryGroup.add(new ArrayList<>());
                index++;
            }
            aux = 0;
            for(Query queryAv: queries){
                if(queryGroup.get(index).contains(queryAv)) continue;
                i = 0;
                for(String term: queryAv.getQueryTerms()){
                    if(getAllTerms(queryGroup.get(index)).contains(term)){
                        i++;
                    }
                }
                if(i > aux){
                    aux = i;
                    queryAux = queryAv;
                }                
            }
            if(aux != 0){
                queryGroup.get(index).add(queryAux.clone());
                queries.remove(queryAux);
            }
            else{
                Query randonQuery = null;
                for(Query q: queries){
                    if(!queryGroup.get(index).contains(q)){
                        randonQuery = q;
                        break;
                    }
                }
                queryGroup.get(index).add(randonQuery.clone());
                queries.remove(randonQuery);
                
            }
        }
    }
    
    /**
     * Return a list of all terms in one group.
     * @param group
     * @return 
     */
    public static List<String> getAllTerms(List<Query> group){
        List<String> terms = new ArrayList<>();
        for(Query query: group){
            terms.addAll(query.getQueryTerms());
        }
        return terms;
    }
    
    /**
     * Count overall size of a List of list of query.
     * @param group
     * @return 
     */
    public static int countOverallSize(List<List<Query>> group){
        int size = 0;
        for(List<Query> list: group){
            size += list.size();
        }
        return size;
    }
}
