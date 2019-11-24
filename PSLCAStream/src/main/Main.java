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
        String baseName = args[0];
        
//        int nGroups = Integer.parseInt(args[2]);    
//        int nThreads = Integer.parseInt(args[1]);
//        int nQueries = Integer.parseInt(args[0]);

        int nGroups = 1;
        int nThreads = 8;
        int nQueries = 50000;
        //String baseName = "xmark";
        
        File folder = new File("xml/"+baseName+"/");
        String queryFileName = baseName+"_test_1_4.txt";
        File listOfFiles[] = folder.listFiles();
        //true = SLCA
        //false = ELCA
        for(int j = 10000; j <= nQueries ; j = j+10000){
            for(int i = 1; i <= nThreads; i = i*2){
                QueryProcessor qp = new QueryProcessor(queryFileName, listOfFiles, semantic, j, i, 1);
                qp.multipleQueriesStart();
            }
        }
//        QueryProcessor qp = new QueryProcessor(queryFileName, listOfFiles, semantic, 10000, 1, 1);
//        qp.multipleQueriesStart();

    }    
}
