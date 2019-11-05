package main;

import engine.QueryProcessor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vinicius
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String xmlFilePath[] = {"xml_file_1.xml"};
        String queryFileName = "query_test_1.txt";
        boolean semantic = true;
        //true = SLCA
        //false = ELCA
        QueryProcessor qp = new QueryProcessor(queryFileName, xmlFilePath, semantic, 4);
        qp.multipleQueriesStart();
    }
    
}
