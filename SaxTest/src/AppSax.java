
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aluno
 */
public class AppSax extends DefaultHandler{ 
   
    public AppSax(){
        super();
    }
    
    @Override
    public void startDocument(){
        System.out.println("Start Document d");
    }
    
    @Override
    public void endDocument(){
        System.out.println("End Document d");
    }
    
    @Override
    public void startElement(String uri, String name, String qName, Attributes atts){
        if ("".equals (uri))
	    System.out.println("Start element: " + qName);
	else
	    System.out.println("Start element: {" + uri + "}" + name);
    }
    
    @Override
    public void endElement(String uri, String name, String qName){
       if ("".equals (uri))
	    System.out.println("End element: " + qName);
	else
	    System.out.println("End element: {" + uri + "}" + name);
    }
    
    public void characters (char ch[], int start, int length){
	
        String s = "";
        List<String> elementTerms = new ArrayList<String>();
        System.out.print("Characters:    \"");
	for (int i = start; i < start + length; i++) {
	    if((ch[i] == '\\') || (ch[i] == '"') || (ch[i] == '\n') || (ch[i] == '\r') 
            || (ch[i] == '\t') || (ch[i] == ' ') || (ch[i] == ',') || (ch[i] == '.')){
                if(Character.isLetterOrDigit(ch[i+1])){
                    elementTerms.add(s);
                    s = "";
                }
            }else{
                s += ch[i];
	    }
	}
        for(String a: elementTerms){
            System.out.print(a+" ");
        }
        System.out.print("\"\n");
        
    }
    
    
}
