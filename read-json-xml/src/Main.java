
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        FileReader xmlFile = new FileReader("D:\\GitHub\\ic-xmlJsonQuery\\read-json-xml\\src\\fileXML.xml");
        System.out.println("wait");
        
        int c = 0;
        while(c != -1){
            c = xmlFile.read();
            System.out.print((char)c);
        }
    }
    
}
