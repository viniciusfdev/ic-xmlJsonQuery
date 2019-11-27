package main;
import engine.QueryProcessor;
import java.io.File;

/**
 *
 * @author vinicius
 */
public class Main {

    public static void main(String[] args) {
        boolean semantic = true;

//        String baseName = args[0];
//        int nGroups = Integer.parseInt(args[2]);    
//        int nThreads = Integer.parseInt(args[1]);
//        int nQueries = Integer.parseInt(args[0]);

        int nGroups = 1;
        int nThreads = 8;
        int nQueries = 50000;
        //String baseName = "xmark";
        
        
        String []bases = {"xmark", "isfdb", "sigmod"};
        //true = SLCA
        //false = ELCA
        for(String baseName: bases){
            String queryFileName = baseName+"_test_";
            File folder = new File("xml/"+baseName+"/");
            File listOfFiles[] = folder.listFiles();
            for(int q = 0; q < 4 ; q++){
                queryFileName += q+"l5t_50000.txt";
                for(int j = 10000; j <= nQueries ; j = j+10000){
                    for(int i = 1; i <= nThreads; i = i*2){
                        QueryProcessor qp = new QueryProcessor(queryFileName, listOfFiles, semantic, j, i, 1);
                        qp.multipleQueriesStart();
                    }
                }
            }
        }
//        QueryProcessor qp = new QueryProcessor(queryFileName, listOfFiles, semantic, 10000, 1, 1);
//        qp.multipleQueriesStart();

    }    
}
