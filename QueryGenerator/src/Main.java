
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
        Generator gen = new Generator();
        //1 - n queries
        //2 - n tokens
        //3 - query file name
        //4 - base name(directory name)
        for(int i = 0; i < 4 ; i++)
        gen.run(50000, i, 5, "test", "icde");
    }
    
    
    
}


//                while((line = streamFile.readLine()) != null){
//                    tokens.addAll(Arrays.asList(line.split("(?<=>)([^<>]+?)(?=<)")));
//                }