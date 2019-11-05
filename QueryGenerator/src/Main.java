
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vinicius
 */
public class Main  extends DefaultHandler{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){

        String line = "";
        int nTperQuery = 4;
        BufferedWriter queryFile = null;
        File folder = new File("src/xml/");
        File listOfFiles[] = folder.listFiles();
        Set<String> tokens = new HashSet<>();
        String queryFileName = "GEN_query_test_1.txt";
        
        try {
            queryFile = new BufferedWriter(new FileWriter(new File("src/query/"+queryFileName).getAbsolutePath()));
            
            for(File file: listOfFiles){
      
                try {
                    XMLReader xr = XMLReaderFactory.createXMLReader();
                    Tokenizer tokenizer = new Tokenizer();
                    xr.setContentHandler(tokenizer);
                    xr.setErrorHandler(tokenizer);
                    xr.parse(new InputSource(new FileReader(file)));
                    tokens.addAll(tokenizer.getTokens());
                } catch (SAXException ex) {
                    System.out.println(ex.getCause());
                    System.out.println(ex.getMessage());
                }
                
            }
            
            for(String token: tokens){
            }
            
            System.out.println(tokens);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}


//                while((line = streamFile.readLine()) != null){
//                    tokens.addAll(Arrays.asList(line.split("(?<=>)([^<>]+?)(?=<)")));
//                }