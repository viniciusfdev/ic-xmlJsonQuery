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
    private Boolean semantic;                       //true for SLCAStream
    private StackNode currentNodeE;                 //ascending serial number
    private StackNode tn;                           //um conjunto de termos de consulta
    private List<Integer> nodePath;                 //
    private Stack<QueryGroupHash> parsingStack;     //uma pilha para manter os nos aberto durante o parser
    private QueryGroupHash queryIndex;              //relaciona um termo e todas as consultas que o contém
    private HashMap<StackNode, Integer> matchTerms; //numero de combinacoes de termos que o no sendo processado possui
    private HashMap<String, List<Integer>> listG1;  //lista invertida G for ELCA
    private HashMap<String, List<Integer>> listG2;  //lista invertida g for ELCA
    private HashMap<String, Integer> listG3;        //lista simplificada invertida for SLCA
    private HashMap<Query, List<Integer>> results;  //resultados referentes a cada consulta
    
    /**
     * 
     * @param semantic represents the semantic choice between ELCA or SLCA. SLCA as default.
     * @param queryIndex represents a query term and refers to queries in which this term occurs.
     */
    public SearchEngine(Boolean semantic, QueryGroupHash queryIndex) {
        super();
        this.nodePath = new ArrayList<Integer>();
        this.parsingStack = new Stack();
        this.listG1 = new HashMap<>();
        this.listG2 = new HashMap<>();
        this.listG3 = new HashMap<>();
        this.results = new HashMap<>();
        this.semantic = semantic;
        this.queryIndex = queryIndex;
        
    }
    
    /**
     * @serial semantic True as default for SLCAStream semantic
     * @param queryIndex represents a query term and refers to queries in which this term occurs.
     */
    public SearchEngine(QueryGroupHash queryIndex) {
        super();
        this.nodePath = new ArrayList<Integer>();
        this.parsingStack = new Stack();
        this.listG1 = new HashMap<>();
        this.listG2 = new HashMap<>();
        this.listG3 = new HashMap<>();
        this.results = new HashMap<>();
        this.semantic = true;
        this.queryIndex = queryIndex;
    }
    
    /**
     * Call back to indicate when the document traversal end
     */
    @Override
    public void startDocument(){
        System.out.println("Start Document d");
    }
    
    /**
     * Call back to indicate when the document traversal start
     */
    @Override
    public void endDocument(){
        System.out.println("End Document d");
    }
    
    /**
     * 
     * Updates the parsing stack and generates each id node.
     * 
     * @param uri
     * @param name the label value upon the traversal of a document.
     * @param qName 
     * @param atts the attributes is not used in this implementation.
     */
    @Override
    public void startElement(String uri, String name, String qName, Attributes atts){
        if ("".equals (uri))
	    System.out.println("Start element: " + qName);
	else
	    System.out.println("Start element: {" + uri + "}" + name);
    }
    
    /**
     * Checks witch nodes or their descendants match the stored queries according
     * to the semantic choice and evaluates if the current node or any of its 
     * descendants match all current current query terms. Also checks if the node 
     * is an LCA node.
     * 
     * @param uri
     * @param name the closed label value upon the traversal of a document.
     * @param qName 
     */
    @Override
    public void endElement(String uri, String name, String qName){
        if(semantic){
            endELementSLCA(uri, name, qName);
        }else{
            endELementELCA(uri, name, qName);
        }
        
        int id = this.currentNodeE.getNodeId();
        if ("".equals (uri))
	    System.out.println("End element: " + qName);
	else
	    System.out.println("End element: {" + uri + "}" + name);
    }
    
    /**
     * Specialized endElement method for SLCA semantic.
     * 
     * @see #endElement(java.lang.String, java.lang.String, java.lang.String) 
     * 
     * @param uri
     * @param name
     * @param qName 
     */
    public void endELementSLCA(String uri, String name, String qName){
        Boolean complete;
    }
    
    /**
     * Specialized endElement method for ELCA semantic.
     * 
     * @see #endElement(java.lang.String, java.lang.String, java.lang.String) 
     * 
     * @param uri
     * @param name
     * @param qName 
     */
    public void endELementELCA(String uri, String name, String qName){
        Boolean complete;
    }
    
    /**
     * Updates the parsing stack if the queries include text tokens as a term.
     * 
     * @param ch
     * @param start
     * @param length 
     */
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
    
    /**
     * Return the result, a structure composed of a key search and a set of nodes.
     * 
     * @return 
     */
    public HashMap<Query, List<Integer>> getResults(){
        return null;
    }

    public Boolean getSemantic() {
        return semantic;
    }

    public void setSemantic(Boolean semantic) {
        this.semantic = semantic;
    }

    public StackNode getCurrentNodeE() {
        return currentNodeE;
    }

    public void setCurrentNodeE(StackNode currentNodeE) {
        this.currentNodeE = currentNodeE;
    }

    public Stack<QueryGroupHash> getParsingStack() {
        return parsingStack;
    }

    public void setParsingStack(Stack<QueryGroupHash> parsingStack) {
        this.parsingStack = parsingStack;
    }

    public QueryGroupHash getQueryIndex() {
        return queryIndex;
    }

    public void setQueryIndex(QueryGroupHash queryIndex) {
        this.queryIndex = queryIndex;
    }

    public HashMap<StackNode, Integer> getMatchTerms() {
        return matchTerms;
    }

    public void setMatchTerms(HashMap<StackNode, Integer> matchTerms) {
        this.matchTerms = matchTerms;
    }

    public HashMap<String, List<Integer>> getListG1() {
        return listG1;
    }

    public void setListG1(HashMap<String, List<Integer>> listG1) {
        this.listG1 = listG1;
    }

    public HashMap<String, List<Integer>> getListG2() {
        return listG2;
    }

    public void setListG2(HashMap<String, List<Integer>> listG2) {
        this.listG2 = listG2;
    }

    public HashMap<String, Integer> getListG3() {
        return listG3;
    }

    public void setListG3(HashMap<String, Integer> listG3) {
        this.listG3 = listG3;
    }
    
    
}
