
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
 * @author aluno
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SAXException, FileNotFoundException, IOException {
        // TODO code application logic here
	XMLReader xr = XMLReaderFactory.createXMLReader();
	AppSax handler = new AppSax();
	xr.setContentHandler(handler);
	xr.setErrorHandler(handler);
        FileReader fr = new FileReader(new File("xmlfile.xml").getAbsolutePath());
        xr.parse(new InputSource(fr));
    }
    
}
