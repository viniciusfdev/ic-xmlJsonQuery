package main;
import engine.QueryProcessor;

/**
 *
 * @author vinicius
 */
public class Main {

    public static void main(String[] args) {
        // TODO code application logic here
        boolean semantic = true;
        String xmlFilePath[] = {"/xmark/xmark(1).xml"};
        String queryFileName = "xmark_test_1_4_50000.txt";
        //true = SLCA
        //false = ELCA
        QueryProcessor qp = new QueryProcessor(queryFileName, xmlFilePath, semantic, 1);
        qp.multipleQueriesStart();
    }    
}
