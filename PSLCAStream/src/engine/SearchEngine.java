/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import node.StackNode;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import query.Query;
import query.QueryGroupHash;

/**
 *
 * @author vinicius franca, evandrino barros
 * 
 */
public class SearchEngine extends DefaultHandler{
    private Boolean semantic = true;                //true for SLCAStream
    private StackNode currentNodeE;                 //ascending serial number
    private Stack<QueryGroupHash> parsingStack;     //
    private HashMap<StackNode, Integer> matchTerms; //numero de combinacoes de termos que o no sendo processado possui
    private HashMap<String, List<Integer>> listG1;  //lista invertida G for ELCA
    private HashMap<String, List<Integer>> listG2;  //lista invertida g for ELCA
    private HashMap<String, Integer> listG3;        //lista simplificada invertida for SLCA
    
    @Override
    public void startDocument(){
        System.out.println("Start Document d");
    }
    
    @Override
    public void endDocument(){
        System.out.println("End Document d");
    }
    
    /**
     * Updates the parsing stack, generates each id node, 
     * 
     * @param uri
     * @param name
     * @param qName
     * @param atts 
     */
    @Override
    public void startElement(String uri, String name, String qName, Attributes atts){
        if ("".equals (uri))
	    System.out.println("Start element: " + qName);
	else
	    System.out.println("Start element: {" + uri + "}" + name);
    }
    
    @Override
    public void endElement(String uri, String name, String qName){
        if(semantic){
            
        }else{
            
        }
        Boolean complete;
        int id = this.currentNodeE.getNodeId();
        if ("".equals (uri))
	    System.out.println("End element: " + qName);
	else
	    System.out.println("End element: {" + uri + "}" + name);
    }
    
    public void endELementSLCA(){
        
    }
    
    public void endELementELCA(){
        
    }
    
    @Override
    public void characters (char ch[], int start, int length){
	Boolean newStackEntry = false;
        
        String s = "";
        List<String> elementTerms = new ArrayList<String>();
        System.out.print("Characters:    \"");
        //String tokens[] = nodeContent.split("([.,;:_ /#@!?~`|\"'{})(*&^%$-])+");
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
