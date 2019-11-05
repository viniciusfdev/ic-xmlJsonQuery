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
        int nQueriesPerGroup = 4;
        List<List<Query>> queryGroup = new ArrayList<>();
        List<List<String>> termGroup = new ArrayList<>();
        List<Query> queries = new ArrayList<>();
        String line = "";
        int queryID = 0;
        int aux = 0;
        int i = 1;
        int index = 0;
        int nThreads = 4;
        
        while((line = queryFile.readLine()) != null){
            queries.add(new Query(i++, Arrays.asList(line.split("\\s+"))));
        }
        
        queryGroup.add(new ArrayList<>());
        queryGroup.get(index).add(queries.get(0));
        List<String> termOcurrences = new ArrayList<>();
        termOcurrences.addAll(queries.get(0).getQueryTerms());
        
        do{
            Query query = queryGroup.get(index).get(queryGroup.get(index).size()-1);
            if(query.getQueryID() == nQueriesPerGroup*(index+1) && nQueriesPerGroup > 0 && index+1 < nThreads){
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
                    queryID = queryAv.getQueryID();
                }                
            }
            if(aux != 0)
                queryGroup.get(index).add(queries.get(queryID-1));
            else
                for(Query q: queries)
                    if(!queryGroup.get(index).contains(q)){
                        queryGroup.get(index).add(q);
                        termOcurrences.addAll(new ArrayList<>(queries.get(0).getQueryTerms()));
                    }
        }while(index != 4);
        
        System.out.println("");
    }
    
    public static List<String> getAllTerms(List<Query> group){
        List<String> terms = new ArrayList<>();
        for(Query query: group){
            terms.addAll(query.getQueryTerms());
        }
        return terms;
    }
    
    public static boolean isOver(){
        return false;
    }
}
