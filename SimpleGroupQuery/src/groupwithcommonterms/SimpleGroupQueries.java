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
public class SimpleGroupQueries {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        BufferedReader queryFile = new BufferedReader(new FileReader(new File("src/xmldoc.txt").getAbsolutePath()));
        List<List<Query>> queryGroup = new ArrayList<>();
        List<Query> queries = new ArrayList<>();
        List<Query> auxList = new ArrayList<>();
        int countQueries = 0;
        int nThreads = 2;
        int count = 0;
        int aux = -1;
        int i = 1;
        
        String line = "";
        while((line = queryFile.readLine()) != null){
            queries.add(new Query(i++, Arrays.asList(line.split("\\s+"))));
            countQueries++;
        }
        
        int nQueriesPerGroup = countQueries/nThreads;
        
        //initialize groups
        for(int j = 0 ; j < nThreads ; j++){
            queryGroup.add(new ArrayList<Query>());
            queryGroup.get(j).add(queries.get(0).clone());
            queries.remove(0);
        }
        
        //distribute queries into groups
        while(!queries.isEmpty()){
            Query queryAv = queries.get(0);
            aux = -1;
            for(List<Query> gQueries: queryGroup){
                if(gQueries.size() < nQueriesPerGroup){
                    count = 0;
                    for(Query query: gQueries){
                        if(query.getQueryTerms().contains(queryAv)){
                            count++;
                        }
                    }
                    if(count > aux){
                        aux = count;
                        auxList = gQueries;
                        System.out.println(auxList);
                    }
                }
            }
            auxList.add(queryAv.clone());
            queries.remove(queryAv);
        }
    }
   
}
