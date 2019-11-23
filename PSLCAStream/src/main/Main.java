package main;
import engine.QueryProcessor;
import java.io.File;

/**
 *
 * @author vinicius
 */
public class Main {

    public static void main(String[] args) {
        // TODO code application logic here
        boolean semantic = true;
        
//        String baseName = args[3];
//        int nGroups = Integer.parseInt(args[2]);    
//        int nThreads = Integer.parseInt(args[1]);
//        int nQueries = Integer.parseInt(args[0]);

        int nGroups = 1;
        int nThreads = 8;
        int nQueries = 2000;
        String baseName = "xmark";
        
        File folder = new File("src/xml/"+baseName+"/");
        File listOfFiles[] = folder.listFiles();
        String queryFileName = baseName+"_test_1_4.txt";
        //true = SLCA
        //false = ELCA
        
        QueryProcessor qp = new QueryProcessor(queryFileName, listOfFiles, semantic, nQueries, nThreads, nGroups);
        qp.multipleQueriesStart();
    }    
}
