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
        String xmlFilePath[] = {"/xmark/xmark(1).xml", "/xmark/xmark(2).xml"};
        String baseName = "xmark";
        File folder = new File("src/xml/");
        File listOfFiles[] = folder.listFiles();
        for(File f: listOfFiles){
            f.getName();
        }
        String queryFileName = "xmark_test_1_4_10000.txt";
        //true = SLCA
        //false = ELCA
        QueryProcessor qp = new QueryProcessor(queryFileName, xmlFilePath, semantic, 4);
        qp.multipleQueriesStart();
    }    
}
