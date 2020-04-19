package engine;

import node.HashResult;
import node.LCAStackNode;
import node.NodePath;
import node.ResultNode;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import parallelize.ParallelTasks;
import query.Query;
import query.QueryGroup;
import query.QueryGroupHash;
import query.TermOccurrences;
import util.MemoryTracker;

import java.util.*;

/**
 * @author Evandrino Barros, Jônatas Tonholo
 * @version 2.0
 */
public class SearchEngine extends DefaultHandler {
    /** Constants used for JAXP 1.2 */
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    private boolean trackMemory = false;
    private long matchingTime = 0;
    private long startTime = 0;
    private long charTime = 0;
    private long endTime = 0;
    private long processTextTime = 0;
    private long pushingTime = 0;
    private long setGroupStackTime = 0;
    private long addAllTime = 0;
    private long termInstancesTime = 0;

    private int currentHeight = 0;
    private int numberOfLabel;
    private int numberOfQueries;
    private int maxHeight=0;
    private int numberSLCA = 0;
    private int numberNonSLCA = 0;
    private Integer tagId;
    
    private int tempoParalelismo = 0;

    private HashResult hashResult;
    private QueryGroupHash queryGroupHash;
    private NodePath nodePath;
    private MemoryTracker memoryTracker;
    private TermOccurrences termOccurrenceHash;
    private double numberOfNodes =0;
    private double numberOfCheckedNodes = 0;
    private double numberOfQueryChecks  = 0;

    private HashSet<QueryGroup> stackList;
    private StringBuffer textContent = new StringBuffer();

    private boolean ELCASemantics;
    private String pushingType;

    /**
     * Constructor
     * @param ELCASemantics
     * @param queryGroupHash
     * @param numberOfQueries
     * @param trackMemory
     * @param memoryTracker
     * @param pushingType
     * @param termOccurrenceHash
     */
    public SearchEngine (boolean ELCASemantics, QueryGroupHash queryGroupHash, int numberOfQueries, boolean trackMemory, MemoryTracker memoryTracker, String pushingType, TermOccurrences termOccurrenceHash, HashResult hashResult) {

        this.ELCASemantics = ELCASemantics;
        this.queryGroupHash = queryGroupHash;
        this.numberOfQueries = numberOfQueries;
        this.trackMemory = trackMemory;
        this.memoryTracker = memoryTracker;
        this.pushingType = pushingType;
        this.termOccurrenceHash = termOccurrenceHash;
        this.nodePath = new NodePath();
        this.stackList = new HashSet<QueryGroup>();
        //this.hashResult = new HashResult(numberOfQueries);
        this.hashResult = hashResult;
        //System.out.println("I am SearchEngine inside!");
    }

    /**
     * Print the Stacks
     */
    public void printStacks () {
        for (QueryGroup group: stackList) {
            System.out.println("Group: " + group.getGroupTerms());
            Stack<LCAStackNode> temp = new Stack<LCAStackNode>();
            Stack<LCAStackNode> stack = group.getStack();
            if ((stack!=null) && !(stack.empty())){
                System.out.println (" Stack contents ");
                while (!stack.empty()){
                    LCAStackNode node = stack.pop();
                    node.printTermInstances();
                    temp.push(node);
                }
                while (!temp.empty()){
                    LCAStackNode node = temp.pop();
                    stack.push(node);
                }
            }
            else
                System.out.println("Empty Stack");
        }
    }

    /**
     * Clean the Results and Term Occurrences
     */
    public void cleanResultsAndTermOccurrences(){
        Set<Map.Entry<Query,ArrayList<ResultNode>>> querySet = this.hashResult.getHashResult().entrySet();
        while(true) {
            try {
                for (Map.Entry<Query,ArrayList<ResultNode>> entry: querySet){
                    Query query = entry.getKey();
                    query.setResultList(new ArrayList<ResultNode>());
                    //if (ELCASemantics){
                    //    query.setTermOccur(new HashMap<String,ArrayList<Integer>>());
                    //    for (String term: query.getVectorOfQueryTerms()){
                    //        query.getTermOccur().put(term, new ArrayList<Integer>());
                    //    }
                    //}
                }
                this.hashResult = new HashResult(this.numberOfQueries);
                break;
            } catch (Exception e) {}
        }
    }

    /**
     * <b>MKStream Start Document</b><br>
     * Parser calls this once at the beginning of a document
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        this.numberOfLabel = 0;
        this.tagId = 0;
        this.currentHeight = 0;
        this.maxHeight = 0;
    }

    @Override
    public void endDocument() throws SAXException{
        //System.out.println("-----------------------------------------------------");
        //System.out.println("Number of matching nodes: "+this.numberOfMatchingNodes);
     
        //System.out.println("Total number of node: "+this.totalNumberOfNodes);
        //double percentage =this.numberOfMatchingNodes/this.totalNumberOfNodes;
        //System.out.println("Percentage (mathing/total): "+Math.round(percentage*100));
        //this.numberOfMatchingNodes=0;
    }
    
    /**
     * <b>MKStream Start</b>
     * @param namespaceURI
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
        long time = System.currentTimeMillis();
        if (this.pushingType.equals("minnodes_groupstacks")){
        	//System.out.println("StartElement: "+qName);
            processStartElementWithMinimalNodesGroupStacks(namespaceURI, localName, qName.toLowerCase(), attributes);
        }
        time = System.currentTimeMillis() - time;
        this.startTime += time;
    }

    /**
     * <b>MKStream End</b>
     * @param namespaceURI
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        long time = System.currentTimeMillis();
        Integer endElementTagId = this.nodePath.getTagId();
        this.numberOfNodes++;
        boolean checkedNodeFlag = false;
        this.nodePath.removeNode();
        for (QueryGroup querygroup : this.stackList) {
            if (querygroup.getStack().size() == this.currentHeight) {
                if (querygroup.getStack().empty()) {
                    continue;
                }
                LCAStackNode current = querygroup.getStack().pop();
                LCAStackNode top;
                if (!querygroup.getStack().empty()) {
                    top = querygroup.getStack().peek();
                    top.getTermInstances().or(current.getTermInstances());
                    top.getQueryList().addAll(current.getQueryList());
                    top.getNonSLCAbitmap().or(current.getNonSLCAbitmap());
                    top.incrementMatchingTerms(current.getMatchingTerms()); //top.matchingTerms+=current.matchingTerms;
                    //this.;
                    //System.out.println("Updating numberOfMatchingNodes: "+this.numberOfMatchingNodes);
                }
                for (Query query : current.getQueryList()) {
                    if (!(current.canBeSLCA(query)) && !(ELCASemantics)) {
                        //System.out.println("Consulta saltada "+query.queryNumber); //FIXME: Necessário?
                        continue;
                    }
                    checkedNodeFlag = true;
                    this.numberOfQueryChecks++;
                    boolean resultNode = current.matchQuery(query);
                    query.addOneToNumberOfComparisons();
                    //System.out.println("Testando a consulta: "+query.getQueryNumber());
                    if (resultNode) { // Found any result
                        //sSystem.out.println("Consulta com match: "+query.getQueryNumber());
                        ResultNode result = null;
                        if (current.canBeSLCA(query)) {
                            result = new ResultNode(current.getLabel(), current.getTagId(), "onlySLCA", query.getQueryNumber());
                            this.numberSLCA++;
                            if (ELCASemantics) { // Found a SLCA
                                this.termOccurrenceHash.SetSLCAOccurrences(query, endElementTagId);
                            }
                        } else if (ELCASemantics) {
                            if (this.termOccurrenceHash.EvaluateIfELCA_And_SetELCAOccurrences(query, endElementTagId)) {
                                result = new ResultNode(current.getLabel(), current.getTagId(), "nonSLCA", query.getQueryNumber());
                                this.numberNonSLCA++;
                            }
                    }

                    if (result != null) {
                        if (this.trackMemory) {
                                query.getResultList().add(result);
                                this.hashResult.putResultNode(query, result);
                        }
                        else {
                                    this.hashResult.putResultNode(query, result);
                        }
                        if (!querygroup.getStack().empty()) {
                            top = querygroup.getStack().peek();
                            top.putQueryInCantBeSLCABitmap(query);
                            if (!ELCASemantics) {
                                top.getQueryList().remove(query);
                            }
                        }
                    }
                }
            }
        }
    }
        if (checkedNodeFlag) { this.numberOfCheckedNodes++; checkedNodeFlag = false; }
        this.currentHeight--;
        time = System.currentTimeMillis() - time;
        this.endTime += time;
        
    }

    /**
     * <b>MKStream Text</b>
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        long time = System.currentTimeMillis();
        this.textContent.setLength(0);
        this.textContent.append(String.copyValueOf(ch, start, length));
        if (this.pushingType.equals("minnodes_groupstacks"))
            processTextForMinimalNodesGroupStacks(this.textContent.toString());
        this.charTime += (System.currentTimeMillis()-time);
        //System.out.println("charTime: "+this.charTime);
    }

    /**
     * Sets the Group Stack
     * @param groupList
     * @param label
     * @param term
     * @param tagId
     * @param currentHeight
     */
    public void setGroupStack (ArrayList<QueryGroup> groupList,
                               String label,
                               String term,
                               Integer tagId,
                               int currentHeight ) {
        //int stackHeight;
        
        //System.out.println();
    	//System.out.println("Entrei em setGroupStack for label "+label);
        ArrayList<Thread> threads = new ArrayList<Thread>();
        
        long time = System.currentTimeMillis();
        
        for (QueryGroup group: groupList){
        	ParallelTasks pt = new ParallelTasks(group, currentHeight,
        			nodePath, label, term, tagId, numberOfQueries);
        	//Thread tpt = new Thread(pt);
        	//tpt.start();
        	//threads.add(tpt);
        	pt.run();
        }
        
        tempoParalelismo += (System.currentTimeMillis()- time);
        
        for (QueryGroup group: groupList){
        	for(Thread t : threads){
        		try {
					t.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
        
        for (QueryGroup group: groupList){
            

            if (this.trackMemory){
                if (group.getStack().size() > this.maxHeight){
                    
                    long instantFreeMemory=0; long instantTotalMemory=0; long instantUsedMemory=0;
                    instantTotalMemory = Runtime.getRuntime().totalMemory();
                    instantFreeMemory = Runtime.getRuntime().freeMemory();
                    instantUsedMemory = instantTotalMemory-instantFreeMemory;
                    if (memoryTracker.getMemoryUsage("maxtotalmemory") < instantTotalMemory)
                        memoryTracker.setUsedMemoryInBytes("maxtotalmemory",instantTotalMemory);
                    if (memoryTracker.getMemoryUsage("maxfreememory") < instantFreeMemory)
                        memoryTracker.setUsedMemoryInBytes("maxfreememory",instantFreeMemory);
                    if (memoryTracker.getMemoryUsage("maxusedmemory") < instantUsedMemory)
                        memoryTracker.setUsedMemoryInBytes("maxusedmemory",instantUsedMemory);
                    memoryTracker.accumulateMemoryUsage("sumtotalmemory",instantTotalMemory);
                    memoryTracker.accumulateMemoryUsage("sumfreememory",instantFreeMemory);
                    memoryTracker.accumulateMemoryUsage("sumusedmemory",instantUsedMemory);
                    memoryTracker.accumulateMemoryUsage("numberofmemorychecks",1);
                    
                    //System.out.println(instantTotalMemory/1024);
                    
                    //System.out.println("---------------------------------------------------------------");
                    //System.out.println("Numero da checagem de memoria: "+memoryTracker.getMemoryUsage("numberofmemorychecks"));
                    //System.out.println("Total de memoria dentro de SearchEngine: "+ Math.round(Runtime.getRuntime().totalMemory()/1024)+ " kB");
                    
                    //System.out.println("Total de memoria liberada dentro de SearchEngine: "+ Math.round(Runtime.getRuntime().freeMemory()/1024)+ " kB");
                    //System.out.println("Diferença: " + (Math.round(Runtime.getRuntime().totalMemory()/1024)- Math.round(Runtime.getRuntime().freeMemory()/1024)) +" kB");
                    //System.out.println("---------------------------------------------------------------");
                    
                    /*this.memoryTracker.checkMemoryWithGC("parse");
                    long initialTotalFreeMemory = this.memoryTracker.getMemoryUsage("initial");
                    long currentTotalFreeMemory = this.memoryTracker.getMemoryUsage("parse") - initialTotalFreeMemory;
                    this.memoryTracker.setUsedMemoryInBytes("totalfreememory", currentTotalFreeMemory);
                    long maxTotalFreeMemory = this.memoryTracker.getMemoryUsage("maxtotalfreememory");
                    if (currentTotalFreeMemory > maxTotalFreeMemory) {
                        memoryTracker.setUsedMemoryInBytes("maxtotalfreememory", currentTotalFreeMemory);
                    }

                    this.memoryTracker.setUsedMemoryInBytes("deepmemory",0);
                    long currentdeepmemory = this.memoryTracker.getMemoryUsage("deepmemory");
                    long maxdeepmemory = this.memoryTracker.getMemoryUsage("maxdeepmemory");
                    if (currentdeepmemory > maxdeepmemory) {
                        memoryTracker.setUsedMemoryInBytes("maxdeepmemory", currentdeepmemory);
                    }

                    this.memoryTracker.setUsedMemoryInBytes("maxdeepmemoryallstacks", 0);
                    this.memoryTracker.setUsedMemoryInBytes("accumulateddeepmemoryallstacks", 0);
                    this.memoryTracker.setUsedMemoryInBytes("tempdeepmemory", 0);
                    long total_deepmemory_allstacks = 0;
                    this.memoryTracker.setUsedMemoryInBytes("deepmemory_allstacks", total_deepmemory_allstacks);
                    long max_deepmemory_allstacks = this.memoryTracker.getMemoryUsage("maxdeepmemoryallstacks");
                    if (total_deepmemory_allstacks > max_deepmemory_allstacks) {
                        this.memoryTracker.setUsedMemoryInBytes("maxdeepmemoryallstacks",total_deepmemory_allstacks);
                    }
                    */

                    this.maxHeight = group.getStack().size();
                }

            }
            this.stackList.add(group);
        }
        this.addAllTime += (System.currentTimeMillis()- time);
        this.setGroupStackTime += (System.currentTimeMillis()- time);
    }

    /**
     * <b>Process Star Element With Minimal Nodes Group Stacks</b><br>
     * That's an implementation of the MKStream Start
     * @param namespaceURI
     * @param localName
     * @param qName
     * @param attributes
     */
    public void processStartElementWithMinimalNodesGroupStacks (String namespaceURI, String localName, String qName, Attributes attributes) {
        this.currentHeight++;
        this.numberOfLabel++;
        if (attributes.getValue("tagId") != null) {
            this.tagId =  Integer.parseInt(attributes.getValue("tagId"));
        } else {
            this.tagId = this.numberOfLabel;
        }
        //System.out.println("ELCASemantics: "+ELCASemantics);
        this.nodePath.addNode(qName, tagId);
        String XMLTag = qName;
        //System.out.println("Inside processStartElement: "+XMLTag);
        ArrayList<QueryGroup> groupList = this.queryGroupHash.getGroupHash().get(XMLTag);
        //System.out.println("groupList: "+XMLTag+" - "+groupList);
        if (groupList != null) {
        	//System.out.println("1 Achei um grupo para a consulta. XML tag: "+XMLTag);//+groupList.getGroupNumber());
            setGroupStack(groupList,XMLTag,XMLTag,tagId,currentHeight);
            //System.out.println("ELCASemantics: "+ELCASemantics);
            //if (ELCASemantics) {
            //    this.termOccurrenceHash.registerOccurence(XMLTag, tagId);
            //}
        }
        groupList = this.queryGroupHash.getGroupHash().get(XMLTag.concat("::"));
        //System.out.println("groupList: "+XMLTag.concat("::")+" - "+groupList);
        if (groupList != null){
        	//System.out.println("2 Achei um grupo para a consulta. XML tag: "+XMLTag.concat("::"));//+groupList.getGroupNumber());
            setGroupStack(groupList, XMLTag, XMLTag.concat("::"), this.tagId, this.currentHeight);
            //System.out.println("ELCASemantics: "+ELCASemantics);
            //if (ELCASemantics){
            //    this.termOccurrenceHash.registerOccurence(XMLTag.concat("::"), tagId);
            //}
        }

        //processing attributes
        for (int i=0; i < attributes.getLength(); i++) {
            //attribute
            String attributeTerm = attributes.getQName(i);
            groupList = this.queryGroupHash.getGroupHash().get(attributeTerm);
            if (groupList != null) {
                setGroupStack(groupList, XMLTag, attributeTerm, this.tagId, this.currentHeight);
                //if (ELCASemantics) {
                //    this.termOccurrenceHash.registerOccurence(attributeTerm, this.tagId);
                //}
            }
            //attribute::
            attributeTerm = attributes.getQName(i).concat("::");
            groupList = this.queryGroupHash.getGroupHash().get(attributeTerm);
            if (groupList != null) {
                setGroupStack(groupList, XMLTag, attributeTerm, this.tagId, this.currentHeight);
                //if (ELCASemantics) {
                //    this.termOccurrenceHash.registerOccurence(attributeTerm, this.tagId);
                //}
            }
            //attribute::value
            attributeTerm = attributes.getQName(i).concat("::".concat(attributes.getValue(i)));
            groupList = this.queryGroupHash.getGroupHash().get(attributeTerm);
            if (groupList != null) {
                setGroupStack(groupList,XMLTag,attributeTerm,tagId,currentHeight);
                //if (ELCASemantics) {
                //    this.termOccurrenceHash.registerOccurence(attributeTerm, tagId);
                //}
            }
            //::value
            attributeTerm = "::".concat(attributes.getValue(i));
            groupList = this.queryGroupHash.getGroupHash().get(attributeTerm);
            if (groupList != null) {
                setGroupStack(groupList, XMLTag, attributeTerm, this.tagId, this.currentHeight);
                //if (ELCASemantics) {
                //    this.termOccurrenceHash.registerOccurence(attributeTerm, tagId);
                //}
            }
            //value
            attributeTerm = attributes.getValue(i);
            groupList = this.queryGroupHash.getGroupHash().get(attributeTerm);
            if (groupList != null) {
                setGroupStack(groupList, XMLTag, attributeTerm, this.tagId, this.currentHeight);
                //if (ELCASemantics) {
                //    this.termOccurrenceHash.registerOccurence(attributeTerm, tagId);
                //}
            }
        }
    }

    /**
     * <b>Process Text For Minimal Nodes Group Stacks</b> <br>
     * That's an implementation of the MKStream Text
     * @param text
     */
    public void processTextForMinimalNodesGroupStacks (String text) {
        text = text.toLowerCase().trim();

        if (text.equals("")) {
            return;
        }

        long time = System.currentTimeMillis();
        String XMLTag = this.nodePath.getLabel();
        int localTagId = this.nodePath.getTagId();
        processTextTime += (System.currentTimeMillis()-time);
        String nodeContent = text;
        nodeContent = nodeContent.toLowerCase().trim(); //remove blank spaces
        ArrayList<QueryGroup> groupList;
        if (nodeContent.length() > 0) {
            String tokens[] = nodeContent.split("([.,;:_ /#@!?~`|\"'{})(*&^%$-])+");
            for (String token: tokens){
                //System.out.println("Token: "+token);
                String term = XMLTag.concat("::".concat(token));
                //System.out.println(" 1 term: "+term);
                groupList = this.queryGroupHash.getGroupHash().get(term);
                if (groupList != null){ // Found a term in the index
                    setGroupStack(groupList,XMLTag,term,localTagId,currentHeight);
                    //if (ELCASemantics) {
                    //    this.termOccurrenceHash.registerOccurence(term, localTagId);
                    //}
                }
                //::keyword
                //System.out.println(" 2 term: "+term);
                term = "::".concat(token);
                groupList = this.queryGroupHash.getGroupHash().get(term);
                if (groupList != null) { // Found a term in the index
                    setGroupStack(groupList,XMLTag,term,localTagId,currentHeight);
                    //if (ELCASemantics) {
                    //    this.termOccurrenceHash.registerOccurence(term, localTagId);
                    //}
                }
                //keyword
                term = token;
                //System.out.println(" 3 term: "+term);
                groupList = this.queryGroupHash.getGroupHash().get(term);
                if (groupList != null) {
                    setGroupStack(groupList,XMLTag,term,localTagId,currentHeight);
                    //if (ELCASemantics) {
                    //    this.termOccurrenceHash.registerOccurence(term, localTagId);
                    //}
                }
            }
        }
    }

    /**
     * Gets hashResult.
     *
     * @return Value of hashResult.
     */
    public HashResult getHashResult() {
        return hashResult;
    }

    /**
     * Gets maxHeight.
     *
     * @return Value of maxHeight.
     */
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Gets endTime.
     *
     * @return Value of endTime.
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Gets startTime.
     *
     * @return Value of startTime.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Gets matchingTime.
     *
     * @return Value of matchingTime.
     */
    public long getMatchingTime() {
        return matchingTime;
    }

    
    public int getTempoParalelismo() {
		return tempoParalelismo;
	}

	public void setTempoParalelismo(int tempoParalelismo) {
		this.tempoParalelismo = tempoParalelismo;
	}
    
    public double getNumberOfNodes(){
        return this.numberOfNodes;
    }


    public double getNumberOfCheckedNodes(){

        return this.numberOfCheckedNodes;
    }

    
    public double getNumberOfQueryChecks(){
        return this.numberOfQueryChecks;
    }
 	/**
     * Gets charTime.
     *
     * @return Value of charTime.
     */
    public long getCharTime() {
        return charTime;
    }
}
