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
        
//        String baseName = args[2];
//        int nThreads = args[1];
//        int nQueries = args[0];
        
        int nThreads = 8;
        int nQueries = 10000;
        String baseName = "xmark";
        
        File folder = new File("xml/"+baseName+"/");
        File listOfFiles[] = folder.listFiles();
        String queryFileName = "xmark_test_1_4_"+nQueries+".txt";
        //true = SLCA
        //false = ELCA
        
        QueryProcessor qp = new QueryProcessor(queryFileName, listOfFiles, semantic, 8);
        qp.multipleQueriesStart();
    }    
}
