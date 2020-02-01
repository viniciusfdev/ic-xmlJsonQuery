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
        //semantic true = SLCA
        //semantic false = ELCA
        //fileType true = xml
        //fileType false = fileType

        int expr = 4; 
        int nTouL = 2;
        int nGroups = 1;
        int nThreads = 1;
        int nQueries = 100;
        String baseName = "";
        String absPath = "";
        boolean semantic = false;
        boolean fileType = false;

        if(args.length > 0){
            expr = Integer.parseInt(args[0]);
            if(args.length > 1)
                baseName = args[1];
            if(args.length > 2){
                nTouL = Integer.parseInt(args[2]);
                if(args.length > 3){
                    absPath = args[3]+"/";
                }
            }
        }
        if(expr == 0){
            experimento0(absPath, semantic, nQueries, nThreads, nGroups, fileType);
        }else if(expr == 1){
            experimento1(baseName, absPath, semantic, nQueries, nThreads, nGroups, fileType);
        }else if(expr == 2){
            experimento2(baseName, absPath, semantic, nTouL, nQueries, nThreads, nGroups, fileType);
        }else if(expr == 3){
            experimento3(baseName, absPath, semantic, nTouL, nQueries, nThreads, nGroups, fileType);
        }else if(expr == 4){
            experimento4(absPath, semantic, nTouL, nQueries, nThreads, nGroups, fileType);
        }
        //finish
    }    
    
    /**
     * Experimento com arquivos JSON
     */
    public static void experimento4(String absPath, boolean semantic, int nTokens, int nQueries, int nThreads, int nGroups, boolean fileType){
        File folder = new File(absPath+"json/");
        String queryFileName = "json_test_";
        queryFileName += "0l_"+nTokens+"t.txt";
        File listOfFiles[] = folder.listFiles();
        
        for(int j = 0; j < 1 ; j++){
            registerState(queryFileName,"Initiate", absPath);
            for(int i = 1; i <= nThreads ; i = i*2){
                QueryProcessor qp = new QueryProcessor(queryFileName, listOfFiles, semantic, nQueries, i, nGroups, fileType, absPath);
                qp.multipleQueriesStart();
            }
            registerState(queryFileName,"Terminate", absPath);
        }
    }
    
    /**
     * Experimento: 0 labels para 2 e 6 token values
     */
    public static void experimento3(String baseName, String absPath, boolean semantic, int nTokens, int nQueries, int nThreads, int nGroups, boolean fileType){
        File folder = new File(absPath+"xml/"+baseName+"/");
        String queryFileName = baseName+"_test_";
        queryFileName += "0l"+nTokens+"t_50000.txt";
        File listOfFiles[] = folder.listFiles();
        
        for(int j = 0; j < 3 ; j++){
            registerState(queryFileName,"Initiate", absPath);
            for(int i = 1; i <= nThreads ; i = i*2){
                QueryProcessor qp = new QueryProcessor(queryFileName, listOfFiles, semantic, nQueries, i, nGroups, fileType, absPath);
                qp.multipleQueriesStart();
            }
            registerState(queryFileName,"Terminate", absPath);
        }
    }
    
    /**
     * Experimento 2: var labels e 5 token values
     */
    public static void experimento2(String baseName, String absPath, boolean semantic, int nLabels, int nQueries, int nThreads, int nGroups, boolean fileType){
        File folder = new File(absPath+"xml/"+baseName+"/");
        String queryFileName = baseName+"_test_";
        queryFileName += nLabels+"l5t_50000.txt";
        File listOfFiles[] = folder.listFiles();
        
        for(int j = 0; j < 10 ; j++){
            registerState(queryFileName,"Initiate", absPath);
            for(int i = 1; i <= nThreads ; i = i*2){
                QueryProcessor qp = new QueryProcessor(queryFileName, listOfFiles, semantic, nQueries, i, nGroups, fileType, absPath);
                qp.multipleQueriesStart();
            }
            registerState(queryFileName,"Terminate", absPath);
        }
        
    }
    
    /**
     * Experimento 1: 0 labels e 4 token values variando n queries
     */
    public static void experimento1(String baseName, String absPath, boolean semantic, int nQueries, int nThreads, int nGroups, boolean fileType){
        File folder = new File(absPath+"xml/"+baseName+"/");
        String queryFileName = baseName+"_test_";
        queryFileName += "0l4t_50000.txt";
        File listOfFiles[] = folder.listFiles();
        
        for(int j = 0; j < 10 ; j++){
            registerState(queryFileName,"Initiate", absPath);
            for(int q = 10000; q <= nQueries ; q += 10000){
                for(int i = 1; i <= nThreads ; i = i*2){
                    QueryProcessor qp = new QueryProcessor(queryFileName, listOfFiles, semantic, q, i, nGroups, fileType, absPath);
                    qp.multipleQueriesStart();
                }
            }
            registerState(queryFileName,"Terminate", absPath);
        }
    }
    
    public static void experimento0(String absPath, boolean semantic, int nQueries, int nThreads, int nGroups, boolean fileType){
        File folder = new File(absPath+"xml/simple/");
        String queryFileName = "simple_test.txt";
        File listOfFiles[] = folder.listFiles();
        
        registerState(queryFileName,"Initiate", absPath);
        for(int i = 1; i <= nThreads ; i = i*2){
            QueryProcessor qp = new QueryProcessor(queryFileName, listOfFiles, semantic, 100, i, nGroups, fileType, absPath);
            qp.multipleQueriesStart();
        }
        registerState(queryFileName,"Terminate", absPath);
    }
    
    public static void registerState(String queryFileName, String msg, String absPath){
        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter(absPath+"results/time_"+queryFileName, true));
            wr.write(msg, 0, msg.length());
            wr.newLine();
            wr.close();
        } catch (IOException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
