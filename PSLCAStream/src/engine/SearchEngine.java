/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import exception.PSLCAStreamException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
    private int id;
    private int height;                                 //the height of the tree
    private Boolean semantic;                           //true for SLCAStream
    private List<Integer> results;                      //todos os nos SLCA encontrados
    private StackNode currentNodeE;                     
    private List<StackNode> nodePath;                   //
    private QueryGroupHash queryIndex;                  //relaciona um termo e todas as consultas que o contém
    private Stack<StackNode> parsingStack;              //uma pilha para manter os nos aberto durante o parser
    private HashMap<String, Integer> simpleG3;          //lista simplificada invertida for SLCA
    private HashMap<String, List<Integer>> invertedg2;  //lista invertida g for ELCA
    private HashMap<String, List<Integer>> invertedG1;  //lista invertida G for ELCA
    
    /**
     * 
     * @param semantic represents the semantic choice between ELCA or SLCA. SLCA as default.
     * @param queryIndex represents a query term and refers to queries in which this term occurs.
     */
    public SearchEngine(QueryGroupHash queryIndex, Boolean semantic) {
        super();
        this.id = 0;
        this.height = -1;
        this.semantic = semantic;
        this.queryIndex = queryIndex;
        this.results = new ArrayList<>();
        this.parsingStack = new Stack<>();
        this.currentNodeE = new StackNode();
        this.nodePath = new ArrayList<StackNode>();
        this.simpleG3 = new HashMap<String, Integer>();
        this.invertedg2 = new HashMap<String, List<Integer>>();
        this.invertedG1 = new HashMap<String, List<Integer>>();
    }
    
    /**
     * Call back to indicate when the document traversal end
     */
    @Override
    public void startDocument(){
            //System.out.println("####Start Document:"+this);
    }
    
    /**
     * Call back to indicate when the document traversal start
     */
    @Override
    public void endDocument(){
        //System.out.println("####End Document:"+this);
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
        try{
            height++;
            String label = "";
            if ("".equals (uri))
                label = qName;
            else
                label = name;
            
            currentNodeE = new StackNode(label, height, ++id);
            nodePath.add(height, new StackNode(currentNodeE));
           
            if(queryIndex.getQueryGroupHash().get(label) != null |
               queryIndex.getQueryGroupHash().get(label+"::") != null){
                if(queryIndex.getQueryGroupHash().get(label) != null &&
                   !queryIndex.getQueryGroupHash().get(label).isEmpty()){
                    currentNodeE.addUsedQueries(queryIndex.getQueries(label));
                    for(Query query: currentNodeE.getUsedQueries()){
                        if(query.getQueryTerms().contains(label))
                            query.increaseMachedTermsById(currentNodeE.getNodeId(), label);
                    }
                    simpleG3.put(label, currentNodeE.getNodeId());
                    //ELCA SEMANTIC
                    if(!semantic){
                        List<Integer> nodes = new ArrayList<>();
                        if((nodes = invertedG1.get(label)) != null){
                            nodes.add(currentNodeE.getNodeId());
                        }else{
                            invertedG1.put(label, new ArrayList<>());
                            invertedG1.get(label).add(currentNodeE.getNodeId());
                        }
                    }
                    //
                }
                if(queryIndex.getQueryGroupHash().get(label+"::") != null &&
                   !queryIndex.getQueryGroupHash().get(label+"::").isEmpty()){
                    currentNodeE.addUsedQueries(queryIndex.getQueries(label+"::"));
                    for(Query query: currentNodeE.getUsedQueries()){
                        if(query.getQueryTerms().contains(label+"::"))
                            query.increaseMachedTermsById(currentNodeE.getNodeId(), label+"::");
                    }
                    simpleG3.put(label+"::", currentNodeE.getNodeId());

                    //ELCA SEMANTIC
                    if(!semantic){
                        List<Integer> nodes = new ArrayList<>();
                        if((nodes = invertedG1.get(label+"::")) != null){
                            nodes.add(currentNodeE.getNodeId());
                        }else{
                            invertedG1.put(label+"::", new ArrayList<>());
                            invertedG1.get(label+"::").add(currentNodeE.getNodeId());
                        }
                    }
                    //
                }
                parsingStack.push(new StackNode(currentNodeE));
            }
        }catch(PSLCAStreamException ex){
            throw new PSLCAStreamException("Bad open xml node");
        }
        //print xml
//        if ("".equals (uri))
//	    System.out.println("Start element: " + qName);
//	else
//	    System.out.println("Start element: {" + uri + "}" + name);
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
//        
//        if ("".equals (uri))
//	    System.out.println("End element: " + qName);
//	else
//	    System.out.println("End element: {" + uri + "}" + name);
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
            if(!parsingStack.isEmpty() && parsingStack.peek().getNodeId() == nodePath.get(nodePath.size()-1).getNodeId()){
                Boolean complete = false;
                StackNode topNode = new StackNode();
                currentNodeE = parsingStack.pop();
                
                for(Query query: currentNodeE.getUsedQueries()){
                    if(query.nMatches(currentNodeE.getNodeId()) >= query.getQueryTerms().size()){
                        complete = true;
                        for(String term: query.getQueryTerms()){
                            if((simpleG3.get(term) != null) && (currentNodeE.getNodeId() > simpleG3.get(term))){
                                complete = false;
                            }
                        }
                        if(complete && (currentNodeE.getNodeId() > query.getLastResultId())){
                            query.addResult(currentNodeE.getNodeId());
                            query.setLastResultId(currentNodeE.getNodeId());
                        }
                    }
                }
                if(!parsingStack.empty())
                    topNode = parsingStack.peek();
                if(!parsingStack.empty() && (currentNodeE.getHeight() - topNode.getHeight()) == 1){
                    topNode.addUsedQueries(currentNodeE.getUsedQueries());
                    topNode.inheritMachedTerms(topNode.getNodeId(), currentNodeE.getNodeId());
                }else{
                    if(!nodePath.isEmpty() && currentNodeE.getHeight() != 0){
                        parsingStack.push(new StackNode(name, currentNodeE.getHeight()-1, nodePath.get(currentNodeE.getHeight()-1).getNodeId()));
                        parsingStack.lastElement().addUsedQueries(currentNodeE.getUsedQueries());
                        parsingStack.lastElement().inheritMachedTerms(parsingStack.lastElement().getNodeId(), currentNodeE.getNodeId());
                    }
                }
            }
            nodePath.remove(nodePath.size()-1);
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
        try{
            if(!parsingStack.isEmpty() && parsingStack.peek().getNodeId() == nodePath.get(nodePath.size()-1).getNodeId()){
                Boolean complete = false;
                StackNode topNode = new StackNode();
                currentNodeE = parsingStack.pop();
                if(currentNodeE.getNodeId() == 4){
                    System.out.println("");
                }
                for(Query query: currentNodeE.getUsedQueries()){
                    if(query.nMatches(currentNodeE.getNodeId()) >= query.getQueryTerms().size()){
                        complete = true;
                        for(String term: query.getQueryTerms()){
                            if((simpleG3.get(term) != null) && (currentNodeE.getNodeId() > simpleG3.get(term))){
                                complete = false;
                            }
                        }
                        if(complete && (currentNodeE.getNodeId() > query.getLastResultId())){
                            query.addResult(currentNodeE.getNodeId());
                            query.setLastResultId(currentNodeE.getNodeId());
                        //ELCA SEMANTIC
                            for(String term: query.getQueryTerms()){
                                List<Integer> nodes = new ArrayList<>();
                                if(invertedG1.get(term) != null)
                                    for(Integer idi: invertedG1.get(term)){
                                        if(idi >= currentNodeE.getNodeId())
                                            nodes.add(idi);
                                    }
                                invertedg2.put(term, nodes);
                            }
                        }else if(complete){
                            Boolean canBeElca = true;
                            List<Integer> idList = new ArrayList<>();
                            for(String term: query.getQueryTerms()){
                                if(invertedG1.get(term) != null)
                                    for(Integer idi: invertedG1.get(term)){
                                        if(invertedg2.get(term) != null && idi != null &&
                                           idi >= currentNodeE.getNodeId() && !invertedg2.get(term).contains(idi))
                                            idList.add(idi);
                                    }
                                if(idList.isEmpty()){
                                    canBeElca = false;
                                }else{
                                    invertedg2.get(term).addAll(idList);
                                }
                            }
                            if(canBeElca){
                                query.addResult(currentNodeE.getNodeId());
                            }
                        }
                        //
                    }
                }
                if(!parsingStack.empty())
                    topNode = parsingStack.peek();
                if(!parsingStack.empty() && (currentNodeE.getHeight() - topNode.getHeight()) == 1){
                    topNode.addUsedQueries(currentNodeE.getUsedQueries());
                    topNode.inheritMachedTerms(topNode.getNodeId(), currentNodeE.getNodeId());
                }else{
                    if(!nodePath.isEmpty() && currentNodeE.getHeight() != 0){
                        parsingStack.push(new StackNode(name, currentNodeE.getHeight()-1, nodePath.get(currentNodeE.getHeight()-1).getNodeId()));
                        parsingStack.lastElement().addUsedQueries(currentNodeE.getUsedQueries());
                        parsingStack.lastElement().inheritMachedTerms(parsingStack.lastElement().getNodeId(), currentNodeE.getNodeId());
                    }
                }
            }
            nodePath.remove(nodePath.size()-1);
            height--;
            
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
        String nodeContent = "";
        List<String> nodeTokens = new ArrayList<>();
	for (int i = start; i < start + length; i++) {
	    if(!(ch[i] == '\\' || ch[i] == '"' || ch[i] == '\r' || ch[i] == '\t' || ch[i] == '\n')){
                //System.out.print(""+ch[i]);
                nodeContent = (nodeContent+ch[i]);
            }
	}
        //System.out.println("");
        nodeTokens = new ArrayList<>(Arrays.asList(nodeContent.split("([.,;:_ /#@!?~`|\"'{})(*&^%$-])+")));
        
        nodeTokens.remove("");
        
        try{
            if(!nodeTokens.isEmpty()){
                for(String term: new ArrayList<String>(nodeTokens)){
                    nodeTokens.add("::"+term);
                    nodeTokens.add(currentNodeE.getLabel()+"::"+term);
                }
                for(String term: nodeTokens){
                    if(queryIndex.getQueryGroupHash().get(term) != null
                       && !queryIndex.getQueryGroupHash().get(term).isEmpty()){
                        simpleG3.put(term, currentNodeE.getNodeId());
                        currentNodeE.addUsedQueries(queryIndex.getQueryGroupHash().get(term));
                        //increment all query matches
                        for(Query query: currentNodeE.getUsedQueries()){
                            if(query.getQueryTerms().contains(term))
                                query.increaseMachedTermsById(currentNodeE.getNodeId(), term);
                        }
                        //currentNodeE.setMatchedTerms(currentNodeE.getMatchedTerms()+1);
                        String contains[] = containsNode(currentNodeE.getNodeId());
                        if(contains[1].equalsIgnoreCase("true")){
                            parsingStack.set(Integer.parseInt(contains[0]), new StackNode(currentNodeE));
                        }else{
                            parsingStack.push(new StackNode(currentNodeE));
                        }
                    }
                }
            }
        }catch(PSLCAStreamException ex){
           throw new PSLCAStreamException("Error during characters parser");
        }
    }
    
    /**
     * Return true and the position if a node id exist in stack
     * Otherwise return false
     * @param nodeId
     * @return String
     */
    public String[] containsNode(int nodeId){
        if(!parsingStack.isEmpty()){
            int snPosition = 0;
            Iterator<StackNode> stackElements = parsingStack.iterator();
            for(snPosition = 0; stackElements.hasNext() ; snPosition++){
                if(stackElements.next().getNodeId() == nodeId){
                    String tupla[] = {""+snPosition, "true"};
                    return tupla;
                }
            }
        }
        String tupla[] = {"-1", "false"};
        return tupla;
    }
    
    public Set<Query> getResultsByQuery(){
        List<Query> queries = new ArrayList<Query>();
        for (List<Query> value : queryIndex.getQueryGroupHash().values()) {
            queries.addAll(value);
        }
        Set<Query> queries_ = new HashSet<>();
        for(Query query: queries){
            queries_.add(query);
        }
        return queries_;
    }
    
    public void printResultsByQuery(){
        for(Query query: getResultsByQuery()){
            if(query.getResults().isEmpty()) continue;
            System.out.print("Query("+query.getQueryID()+")="+query.getQueryTerms().toString()+"=");
            System.out.println(query.getResults().toString());
            System.out.println("\n");
        }
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

    public List<StackNode> getNodePath() {
        return nodePath;
    }

    public HashMap<String, Integer> getSimpleG3() {
        return simpleG3;
    }
    
}
