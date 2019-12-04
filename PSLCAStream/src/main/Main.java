package main;
import engine.QueryProcessor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vinicius
 */
public class Main {

    public static void main(String[] args) {
        //String []bases = {"xmark", "isfdb", "sigmod"};
        //true = SLCA
        //false = ELCA

        int nGroups = 1;
        int nThreads = 8;
        int nQueries = 50000;
        boolean semantic = true;
        String baseName = args[1];
        int expr = Integer.parseInt(args[0]);
        int nTouL = Integer.parseInt(args[2]);
        
        if(expr == 1){
            experimento1(baseName, semantic, nQueries, nThreads, nGroups);
        }else if(expr == 2){
            experimento2(baseName, semantic, nTouL, nQueries, nThreads, nGroups);
        }else if(expr == 3){
            experimento3(baseName, semantic, nTouL, nQueries, nThreads, nGroups);
        }
        System.out.println("acabou");
    }    
    
    /**
     * Experimento: 0 labels para 2 e 6 token values
     */
    public static void experimento3(String baseName, boolean semantic, int nTokens, int nQueries, int nThreads, int nGroups){
        File folder = new File("xml/"+baseName+"/");
        String queryFileName = baseName+"_test_";
        queryFileName += "0l"+nTokens+"t_50000.txt";
        File listOfFiles[] = folder.listFiles();
        
        for(int j = 0; j < 10 ; j++){
            registerState(queryFileName,"Initiate");
            for(int i = 1; i <= nThreads ; i = i*2){
                QueryProcessor qp = new QueryProcessor(queryFileName, listOfFiles, semantic, nQueries, i, nGroups);
                qp.multipleQueriesStart();
            }
            registerState(queryFileName,"Terminate");
        }
    }
    
    /**
     * Experimento 2: var labels e 5 token values
     */
    public static void experimento2(String baseName, boolean semantic, int nLabels, int nQueries, int nThreads, int nGroups){
        File folder = new File("xml/"+baseName+"/");
        String queryFileName = baseName+"_test_";
        queryFileName += nLabels+"l5t_50000.txt";
        File listOfFiles[] = folder.listFiles();
        
        for(int j = 0; j < 10 ; j++){
            registerState(queryFileName,"Initiate");
            for(int i = 1; i <= nThreads ; i = i*2){
                QueryProcessor qp = new QueryProcessor(queryFileName, listOfFiles, semantic, nQueries, i, nGroups);
                qp.multipleQueriesStart();
            }
            registerState(queryFileName,"Terminate");
        }
        
    }
    
    /**
     * Experimento 1: 0 labels e 4 token values variando n queries
     */
    public static void experimento1(String baseName, boolean semantic, int nQueries, int nThreads, int nGroups){
        File folder = new File("xml/"+baseName+"/");
        String queryFileName = baseName+"_test_";
        queryFileName += "0l4t_50000.txt";
        File listOfFiles[] = folder.listFiles();
        
        for(int j = 0; j < 10 ; j++){
            registerState(queryFileName,"Initiate");
            for(int q = 10000; j <= nQueries ; j += 10000)
            for(int i = 1; i <= nThreads ; i = i*2){
                QueryProcessor qp = new QueryProcessor(queryFileName, listOfFiles, semantic, nQueries, q, nGroups);
                qp.multipleQueriesStart();
            }
            registerState(queryFileName,"Terminate");
        }
    }
    
    
    public static void registerState(String queryFileName, String msg){
        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter("results/time_"+queryFileName, true));
            wr.write(msg, 0, msg.length());
            wr.newLine();
            wr.close();
        } catch (IOException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
