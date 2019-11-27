
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
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
public class Generator {
    List<String> tokens_ = new ArrayList<>();
    
    public void run(int nQuery, int nLabels, int nTokens, String queryFileName, String baseName){
        String line = "";
        Random rand = new Random();
        BufferedWriter queryFile = null;
        File folder = new File("src/xml/"+baseName);
        File listOfFiles[] = folder.listFiles();
        Set<String> tokens = new HashSet<>();
        
        
        try {
            queryFile = new BufferedWriter(new FileWriter(new 
                File("src/query_test/"
                    +baseName+"_"+queryFileName+"_"+nLabels+"l"+nTokens+"t_"+nQuery+".txt").getAbsolutePath()));
            
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
            
            tokens_.addAll(tokens);
            int i;
            for(i = 0 ; i < nQuery ; i++){
                String query = "";
                int labels = nLabels;
                for(int j = 0; j < nTokens ; j++){
                    int pos = rand.nextInt(tokens.size());
                    while(query.contains(tokens_.get(pos))){
                        pos = rand.nextInt(tokens.size());
                    }
                    if(labels > 0){
                        query += tokens_.get(rand.nextInt(tokens.size()))+
                                "::"+tokens_.get(pos)+" ";
                        labels--;
                    }else{
                        query += "::"+tokens_.get(pos)+" ";
                    }
                }
                //System.out.println("Query("+(i+1)+") = "+query);
                queryFile.write(query+"\n");
            }
            queryFile.close();
            
            //System.out.println(tokens);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
