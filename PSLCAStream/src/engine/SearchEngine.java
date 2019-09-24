/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import exception.PSLCAStreamException;
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
    private int height;                                 //the height of the tree
    private Boolean semantic;                           //true for SLCAStream
    private StackNode currentNodeE;                     //ascending serial number
    private StackNode topNode;                          
    private List<StackNode> nodePath;                     //
    private Stack<StackNode> parsingStack;              //uma pilha para manter os nos aberto durante o parser
    private QueryGroupHash queryIndex;                  //relaciona um termo e todas as consultas que o contém
    private HashMap<StackNode, Integer> matchTerms;     //numero de combinacoes de termos que o no sendo processado possui
    private HashMap<String, List<Integer>> invertedG1;  //lista invertida G for ELCA
    private HashMap<String, List<Integer>> invertedG2;  //lista invertida g for ELCA
    private HashMap<String, Integer> simpleG3;          //lista simplificada invertida for SLCA
    private List<Integer> results;                      //todos os nos SLCA encontrados
    
    /**
     * 
     * @param semantic represents the semantic choice between ELCA or SLCA. SLCA as default.
     * @param queryIndex represents a query term and refers to queries in which this term occurs.
     */
    public SearchEngine(Boolean semantic, QueryGroupHash queryIndex) {
        super();
        this.height = -1;
        this.topNode = new StackNode();
        this.currentNodeE = new StackNode();
        this.nodePath = new ArrayList<StackNode>();
        this.results = new ArrayList<>();
        this.parsingStack = new Stack();
        this.matchTerms = new HashMap<StackNode, Integer>();
        this.invertedG1 = new HashMap<String, List<Integer>>();
        this.invertedG2 = new HashMap<String, List<Integer>>();
        this.simpleG3 = new HashMap<String, Integer>();
        this.semantic = semantic;
        this.queryIndex = queryIndex;
        this.nodePath.add(this.currentNodeE);
    }
    
    /**
     * @serial semantic True as default for SLCAStream semantic
     * @param queryIndex represents a query term and refers to queries in which this term occurs.
     */
    public SearchEngine(QueryGroupHash queryIndex) {
        super();
        this.height = -1;
        this.topNode = new StackNode();
        this.currentNodeE = new StackNode();
        this.nodePath = new ArrayList<StackNode>();
        this.results = new ArrayList<>();
        this.parsingStack = new Stack();
        this.matchTerms = new HashMap<StackNode, Integer>();
        this.invertedG1 = new HashMap<String, List<Integer>>();
        this.invertedG2 = new HashMap<String, List<Integer>>();
        this.simpleG3 = new HashMap<String, Integer>();
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
        height++;
        String label = "";
        if ("".equals (uri))
	    label = qName;
	else
	    label = name;
        currentNodeE = new StackNode(label, height, currentNodeE.getNodeId()+1);
        nodePath.add(height, new StackNode(currentNodeE));
        if(queryIndex.getQueryGroupHash().get(label) != null |
           queryIndex.getQueryGroupHash().get(label+"::") != null){
            if(queryIndex.getQueryGroupHash().get(label) != null &&
               !queryIndex.getQueryGroupHash().get(label).isEmpty()){
                currentNodeE.addUsedQueries(queryIndex.getQueryGroupHash().get(label));
                simpleG3.put(label, currentNodeE.getNodeId());
                currentNodeE.setMatchedTerms(currentNodeE.getMatchedTerms()+1);
            }
            if(queryIndex.getQueryGroupHash().get(label+"::") != null &&
               !queryIndex.getQueryGroupHash().get(label+"::").isEmpty()){
                currentNodeE.addUsedQueries(queryIndex.getQueryGroupHash().get(label+"::"));
                simpleG3.put(label+"::", currentNodeE.getNodeId());
                currentNodeE.setMatchedTerms(currentNodeE.getMatchedTerms()+1);
            }
            parsingStack.push(new StackNode(currentNodeE));
        }
        
        //print xml
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
        try{
            if(!parsingStack.isEmpty() && parsingStack.peek().getNodeId() == nodePath.remove(nodePath.size()-1).getNodeId()){
                Boolean complete= false;
                currentNodeE = parsingStack.pop();
                for(Query query: currentNodeE.getUsedQueries()){
                    if(currentNodeE.getMatchedTerms() >= query.getQueryTerms().size()){
                        complete = true;
                        for(String term: query.getQueryTerms()){
                            if(currentNodeE.getNodeId() < simpleG3.get(term)){
                                complete = false;
                                ///????????????
                            }
                        }
                        if(complete && (currentNodeE.getNodeId() > query.getLastResultId())){
                            query.addResult(currentNodeE.getNodeId());
                            query.setLastResultId(currentNodeE.getNodeId());
                            results.add(currentNodeE.getNodeId());
                        }
                    }
                }
                if(!parsingStack.empty())
                    topNode = parsingStack.peek();
                if(!parsingStack.empty() && (currentNodeE.getHeight() - topNode.getHeight()) == 1){
                    topNode.addUsedQueries(topNode.getUsedQueries());
                    topNode.addUsedQueries(currentNodeE.getUsedQueries());
                    topNode.setMatchedTerms(topNode.getMatchedTerms()+currentNodeE.getMatchedTerms());
                    ////????????????????
                }else{
                    parsingStack.push(new StackNode(name, currentNodeE.getHeight()-1, nodePath.get(currentNodeE.getHeight()-1).getNodeId()));
                }
            }
            height--;
        }catch(PSLCAStreamException ex){
            System.out.println("Bad closed xml node:"+ex.getCause());
        }
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
        
        try{
            
        }catch(PSLCAStreamException ex){
            System.out.println("Bad closed xml node:"+ex.getCause());
        }
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
    public List<Integer> getResults(){
        return this.results;
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

    public Stack<StackNode> getParsingStack() {
        return parsingStack;
    }

    public void setParsingStack(Stack<StackNode> parsingStack) {
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

    public StackNode getTopNode() {
        return this.topNode;
    }

    public List<StackNode> getNodePath() {
        return nodePath;
    }

    public HashMap<String, List<Integer>> getInvertedG1() {
        return invertedG1;
    }

    public HashMap<String, List<Integer>> getInvertedG2() {
        return invertedG2;
    }

    public HashMap<String, Integer> getSimpleG3() {
        return simpleG3;
    }
    
    
    
}
