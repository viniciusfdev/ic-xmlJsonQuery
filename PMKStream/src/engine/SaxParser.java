package engine;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;

/**
 * @author Evandrino Barros, Jônatas Tonholo
 * @version 2.0
 */
public class SaxParser {
    private SAXParserFactory spf = SAXParserFactory.newInstance();

    /**
     * Parse the filename
     * @param filename
     * @param SE
     */
    public void parse (String filename, SearchEngine SE) {
        spf = SAXParserFactory.newInstance();
        this.spf = SAXParserFactory.newInstance();

        try {
            spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,false);
        } catch (SAXNotRecognizedException e) {
            e.printStackTrace();
        } catch (SAXNotSupportedException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();
            //parse the file and also register this class for call backs
            //parser_timer.start("parse");
            sp.parse(convertToFileURL(filename), SE);
            //parser_timer.end("parse");
            sp.reset();
            //return parser_timer.getElapsedTime("parse", TimeUnit.SECONDS);

        }catch(SAXException se) {
            se.printStackTrace();
            System.out.println(se.toString());
        }catch(ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Convert to File URL
     * @param filename
     * @return
     */
    private static String convertToFileURL(String filename) {
        // On JDK 1.2 and later, simplify this to:
        // "path = file.toURL().toString()". //FIXME
        String path = new File(filename).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
    }
}
