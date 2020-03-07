package main;

import java.io.FileNotFoundException;
import java.util.Scanner;

import engine.QueryProcessor;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jônatas on 04/12/2014.
 */
public class RunMKStream {
    //private static String defaultDir = System.getProperty("user.dir") + "/src/xml";
//    private static String defaultDir = "src/";
//    private static String defaultDir = "D:\\data_xml/queries";
    private static String[] lArgs = new String[10];
    //private static String dir = System.getProperty("user.dir") + "/src/xml/";
    //private static String queryFile = System.getProperty("user.dir") + "/src/xml/consulta.txt"; //FIXME: Aqui vai o arquivo .txt de consulta. O Algoritmo consulta todos os XMLs em dir.
    private static String processor = ""; // Nome do algoritmo
    private static String nodeType; //SLCA ou ELCA
    private static String numberOfQueries;
    private static String memoryTime = "";
    private static String numberOfStacks;
//    private static String pushingType = "allnodes_allstacks";
//    private static String pushingType = "minnodes_allstacks";
    private static String pushingType = "minnodes_groupstacks";



    public static void main(String ... args) {
        String defaultDir = System.getProperty("user.dir")+"/src/";
        
        System.out.println(defaultDir);
        
        //netbeans
        args = new String[5];
        args[0] = "isfdb";
        args[1] = "100";
        args[2] = "1";
        args[3] = "2";
        args[4] = "1";
        
        for(int i=0; i < 4 ; i++) {
            lArgs[0] = defaultDir+"xml/datasets/"+args[0].toLowerCase()+"/";
            if(args[3].equals("2"))
                    lArgs[1] = args[0].toLowerCase()+"_test_0l5t_50000.txt";
            else if(args[3].equals("3"))
                    lArgs[1] = args[0].toLowerCase()+"_test_1l5t_50000.txt";
            else if(args[3].equals("4"))
                    lArgs[1] = args[0].toLowerCase()+"_test_2l5t_50000.txt";
            else if(args[3].equals("5"))
                    lArgs[1] = args[0].toLowerCase()+"_test_3l5t_50000.txt";
            else if(args[3].equals("6"))
                    lArgs[1] = args[0].toLowerCase()+"_test_0l2t_50000.txt";
            else if(args[3].equals("7"))
                    lArgs[1] = args[0].toLowerCase()+"_test_0l4t_50000.txt";
            else if(args[3].equals("8"))
                    lArgs[1] = args[0].toLowerCase()+"_test_0l6t_50000.txt";
            else if(args[3].equals("1"))
                    lArgs[1] = args[0].toLowerCase()+"_test_0l4t_50000.txt";

            for(int j=1; j <= 8 ; j=j*2){
                lArgs[2] = processor;
                lArgs[3] = args[0];
                lArgs[4] = args[1];
                lArgs[5] = memoryTime;
                lArgs[6] = args[2];
                lArgs[7] = pushingType;
                lArgs[8] = ""+j;
                lArgs[9] = defaultDir + "/xml/queries/";

                //System.out.println("base;#xml/queries;#stacks;#experiment;#threads;");
                //System.out.print(args[0]+";"+args[1]+";"+args[2]+";"+args[3]+";"+args[4]+";");

                try {
                    QueryProcessor.run((lArgs));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void registerState(String msg, String queryFileName){
        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter("/xml/queries/results/time_"+queryFileName, true));
            wr.write(msg, 0, msg.length());
            wr.newLine();
            wr.close();
        } catch (IOException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}