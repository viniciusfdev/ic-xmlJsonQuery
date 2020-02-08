package engine;

import database.Database;
import exception.MKStreamException;
import parallelize.Parallel;
import parallelize.Sequential;
import query.QueryGroup;
import query.QueryGroupHash;
import query.TermOccurrences;
import util.DateUtils;
import util.OnlyExtension;
import util.TimeTracker;
import node.HashResult;

import java.io.*;
import java.util.*;

/**
 * @author Evandrino Barros, Jônatas Tonholo
 * @version 2.0
 */
public class QueryProcessor {
	private String queriesFileName;
	private String[] XMLFileList;
	private String XMLFilePath;
	private ArrayList<String> arrayOfQueries;
	private Hashtable resultsSummary;
	private int numberOfQueries;
	public static int cont = 0;
    public static double numberOfQueryChecks = 0;
    public static double numberOfCheckedNodes = 0;
    public static double numberOfNodes = 0;
    private static HashResult hashResult;

	/**
	 * Constructor
	 * 
	 * @param XMLFilePath
	 * @param queriesFileName
	 */
	public QueryProcessor(String XMLFilePath, String queriesFileName) {
		this.XMLFilePath = XMLFilePath;
		this.queriesFileName = queriesFileName;
		this.numberOfQueries = 0;
		this.arrayOfQueries = new ArrayList<String>();
		this.resultsSummary = new Hashtable();
  
	}

	/**
	 * Get All Query Terms as String
	 * 
	 * @return
	 */
	public String getAllQueryTerms() {
		return this.arrayOfQueries.toString();
	}

	/**
	 * Set the XML File List
	 */
	public void setXMLFileList() {
		OnlyExtension xmlExt = new OnlyExtension("xml");
		//System.out.println(this.XMLFilePath);
		this.XMLFileList = new java.io.File(this.XMLFilePath).list(xmlExt); // Get
																			// list
																			// of
																			// names
		java.util.Arrays.sort(this.XMLFileList); // Sort it (Data Structuring
													// chapter))
	}

	/**
	 * Print the XML File List
	 */
	public void printXMLFileList() {
		for (int i = 0; i < this.XMLFileList.length; i++)
			System.out.println(this.XMLFilePath + "/" + this.XMLFileList[i]);
	}

	/**
	 * Set the Array of Queries
	 * 
	 * @param maxQuant
	 */
	public void setArrayOfQueries(int maxQuant) {
		try {
			// File f = new File(this.XMLFilePath + this.queriesFileName);
			// //FIXME
			File f = new File(this.queriesFileName);
			BufferedReader in = new BufferedReader(new FileReader(f));
			String line;
			while ((line = in.readLine()) != null) {
				if (this.numberOfQueries == maxQuant) { // FIXME: o que acontece
														// se eu ler o arquivo
														// todo?
					break;
				}
				//lowercase
				this.arrayOfQueries.add(line.toLowerCase().trim());
				//this.arrayOfQueries.add(line.trim());
				this.numberOfQueries++;
			}
			//System.out.println("n " + numberOfQueries);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Print Array Of Query
	 */
	public void printArrayOfQuery() {
		for (String auxQueryTerm : this.arrayOfQueries) {
			System.out.println("Query: " + auxQueryTerm);
		}
	}

	/**
	 * Gets the Result Summary String
	 * 
	 * @return
	 */
	public String getResultSummaryString() {
		String summaryString = new String();
		Enumeration e = this.resultsSummary.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			summaryString += key + " " + this.resultsSummary.get(key) + "\n";
		}
		return summaryString;
	}

	/**
	 * Record the Results in Data Base FIXME: Criar um controller
	 * 
	 * @param SE
	 * @param aArgs
	 * @param XMLFile
	 * @param fileCounter
	 * @param dbMysql
	 * @param pushing_type
	 */
	public static void recordResultsInDB(SearchEngine SE, String[] aArgs,
			String XMLFile, int fileCounter, Database dbMysql,
			String pushing_type) {
		/*
		 * HashResult resulthash = SE.getHashResult(); HashMap resultmap =
		 * resulthash.getHashResult(); Set keyset = resultmap.keySet(); Iterator
		 * i = keyset.iterator(); while (i.hasNext()){ Query key = (Query)
		 * i.next(); ArrayList<ResultNode> resultNodeList =
		 * (ArrayList<ResultNode>) resultmap.get(key); for (ResultNode
		 * result:resultNodeList){ try{
		 * dbMysql.executeSQLStatement("insert into resultado values ("
		 * +result.getQueryNumber() +",\""+result.getQueryTerms()+"\","
		 * +fileCounter +",\""+XMLFile+"\"," +"\""+result.getLabel()+"\","
		 * +"\""+pushing_type+"\"," +result.getTagId()+"," +0+",0,"
		 * +"\""+result.getNodeType()+"\"," +0+")"); } catch(Exception e){
		 * String error = e.getMessage(); } }
		 * dbMysql.executeSQLStatement("commit"); }
		 */
	}

	// FIXME: Main
	// public static void main(String args[]) throws FileNotFoundException {
	public static void run(String args[]) throws FileNotFoundException {
		//System.out.println("MKEStream 2.0");
		//String initialTime = DateUtils.now();
		if (args.length != 9) {
			System.out.println("Invalid number of parameters.");
			System.out.println("Parameters: <0 - dir of XML docs>");
			System.out.println("            <1 - query file>");
			System.out.println("            <2 - processor>");
			System.out.println("            <3 - node type");
			System.out.println("            <4 - number of queries>");
			System.out.println("            <5 - memory | time>");
			System.out.println("            <6 - number of stacks>");
			System.out
					.println("            <7 - allnodes_allstacks | minnodes_allstacks | minnodes_groupstacks>");
			return;
		}

		String pushingType = args[7].trim().toLowerCase();
		if (!pushingType.equals("minnodes_allstacks")
				&& !pushingType.equals("minnodes_groupstacks")
				&& !pushingType.equals("allnodes_allstacks")) {
			System.out.println("pushing_type value is unexpected.");
			return;
		}
		//System.out.println("Initial runtime: " + initialTime);

		// TODO: implementar database
		// Database db = new Database("jis2011","jis2011","jis2011");
		// db.executeSQLStatement("delete from resultado where processador='"+pushingType+"'");

		int fileCounter = 0;
		long totalProcessingTimePerFile = 0;

		QueryProcessor QP = new QueryProcessor(args[0], args[1]);
		// String base_dir = QP.XMLFilePath; //aArgs[0]
		String query_file = QP.queriesFileName; // aArg2[1]
		String algorithm = args[2].trim(); // aArgs[2]
		String semantics = new String(args[3].trim());
		//System.out.println("Semantics: "+semantics);
		int maxQueryQuant = args[4].equalsIgnoreCase("") ? 0 : new Integer(
				args[4].trim());
		String resultType = new String(args[5].trim());
		int numberOfStacks = args[6].equalsIgnoreCase("") ? 0 : new Integer(
				args[6].trim());
		int numberOfThreads = args[8].equalsIgnoreCase("") ? 0 : new Integer(
				args[8].trim());
		boolean ELCASemantics = false;
		// boolean allNodes = aArgs[7].trim()=="allNodes"?true:false;
		// String pushing_type = aArgs[7].trim().toLowerCase();

		// obtem a lista de arquivos de um diretorio
		QP.setXMLFileList();

		// obtem a lista de consultas a ser processada
		QP.setArrayOfQueries(maxQueryQuant);

		// Agrupamento das consultas considerando a sequencia das consultas
		// QueryGroupHash QGH = new QueryGroupHash();
		// QGH.groupQuerySequentially(QP.arrayOfQueries,percentual);
		// QGH.print();

		// Agrupamento das consultas, tentando colocar o maior numero possivel
		// de consultas
		// de um mesmo termo e uma pilha, respeitados a quant. max de consultas
		// por pilha
		// e o numero maximo de grupos

		//ELCASemantics = semantics.equals("ELCA") ? true : false;
		//System.out.println("Valor ELCASemantics: " + ELCASemantics);
		//System.out.println("Threads: " + numberOfThreads);
		// System.out.println("::Key ::JON".toLowerCase());
		int eachGroupSize = Math.round(maxQueryQuant / (numberOfStacks * 1.0f));
		// eachGroupSize = maxQueryQuant;
		//System.out.println("Each Group Size: " + eachGroupSize);
		// long numberOfGroups =
		// Math.round(Math.ceil(maxQueryQuant/(eachGroupSize*1.0f)));
		QueryGroupHash QGH = new QueryGroupHash(ELCASemantics);

		try {
			QGH.groupQueryWithQueriesWithAnyCommonTerms(QP.arrayOfQueries,
					numberOfStacks, eachGroupSize);
		} catch (MKStreamException e) {
			e.printStackTrace();
			
		}
        //System.out.println("QGH.print()");
		//QGH.print();

		TermOccurrences termOccurrenceHash = null;
        
        
        //return;
		//if (ELCASemantics) {
		//	termOccurrenceHash = new TermOccurrences(QGH, QP.numberOfQueries);
		//	if (!pushingType.equals("minnodes_groupstacks")) {
		//		System.out
		//				.println("Mininal nodes for group stacks only for ELCA semantics.");
		//		return;
		//	}
		//}

		TermOccurrences termOccurrenceHash2 = null;

		//if (ELCASemantics) {
		//	termOccurrenceHash2 = new TermOccurrences(QGH, QP.numberOfQueries);
		//	if (!pushingType.equals("minnodes_groupstacks")) {
		//		System.out
		//				.println("Mininal nodes for group stacks only for ELCA semantics.");
		//		return;
		//	}
		//}

		TermOccurrences termOccurrenceHash3 = null;

		//if (ELCASemantics) {
		//	termOccurrenceHash3 = new TermOccurrences(QGH, QP.numberOfQueries);
		//	if (!pushingType.equals("minnodes_groupstacks")) {
		//		System.out
		//				.println("Mininal nodes for group stacks only for ELCA semantics.");
		//		return;
		//	}
		//}

		TermOccurrences termOccurrenceHash4 = null;

		//if (ELCASemantics) {
		//	termOccurrenceHash4 = new TermOccurrences(QGH, QP.numberOfQueries);
		//	if (!pushingType.equals("minnodes_groupstacks")) {
		//		System.out
		//				.println("Mininal nodes for group stacks only for ELCA semantics.");
		//		return;
		//	}
		//}

		TermOccurrences termOccurrenceHash5 = null;

		//if (ELCASemantics) {
		//	termOccurrenceHash5 = new TermOccurrences(QGH, QP.numberOfQueries);
		//	if (!pushingType.equals("minnodes_groupstacks")) {
		//		System.out
		//				.println("Mininal nodes for group stacks only for ELCA semantics.");
		//		return;
		//	}
		//}

		TermOccurrences termOccurrenceHash6 = null;

		//if (ELCASemantics) {
		//	termOccurrenceHash6 = new TermOccurrences(QGH, QP.numberOfQueries);
		//	if (!pushingType.equals("minnodes_groupstacks")) {
		//		System.out
		//				.println("Mininal nodes for group stacks only for ELCA semantics.");
		//		return;
		//	}
		//}

		TermOccurrences termOccurrenceHash7 = null;

		//if (ELCASemantics) {
		//	termOccurrenceHash7 = new TermOccurrences(QGH, QP.numberOfQueries);
		//	if (!pushingType.equals("minnodes_groupstacks")) {
		//		System.out
		//				.println("Mininal nodes for group stacks only for ELCA semantics.");
		//		return;
		//	}
		//}

		TermOccurrences termOccurrenceHash8 = null;

		//if (ELCASemantics) {
		//	termOccurrenceHash8 = new TermOccurrences(QGH, QP.numberOfQueries);
		//	if (!pushingType.equals("minnodes_groupstacks")) {
		//		System.out
		//				.println("Mininal nodes for group stacks only for ELCA semantics.");
		//		return;
		//	}
		//}

		// termOccurrenceHash.print();
		// termOccurrenceHash.registerOccurence("address::", 2);
		// termOccurrenceHash.registerOccurence("address::", 4);
		// termOccurrenceHash.print();
		// System.out.println(" limpeza das ocorrencias.");
		// termOccurrenceHash.cleanOccurrences();
		// termOccurrenceHash.print();
		// Database db = new Database("jis2011","jis2011","jis2011");
		boolean trackMemory = false; // FIXME - Nunca ocorrerá
		/*
		 * FIXME:Resolver BD if (resultType.equals("memory")){ trackMemory =
		 * true; db.deletePreviousMemoryResults(algorithm,semantics, base_dir,
		 * query_file, maxQueryQuant,numberOfStacks, pushingType); } else{
		 * trackMemory = false;
		 * db.deletePreviousTimeResults(algorithm,semantics, base_dir,
		 * query_file, maxQueryQuant,numberOfStacks, pushingType); }
		 */
		util.MemoryTracker memory_tracker = new util.MemoryTracker();
		long initial_memory_size = 0;
		if (trackMemory) {
			memory_tracker.checkMemoryWithGC("initial");
		}

		// SearchEngine SE = new SearchEngine(aArgs[3], QGH,
		// QP.numberOfqueries, track_memory, memory_tracker, pushing_type );
		// SaxParser parser = new SaxParser();

		if (trackMemory) {
			//memory_tracker.checkMemoryWithGC("initial");
			//initial_memory_size = memory_tracker.getMemoryUsageInKB("initial");
			// initial_memory_size =0;
			//System.out.println("Initial Memory Size: " + initial_memory_size
			//		+ " KB");
            memory_tracker.setUsedMemoryInBytes("maxtotalmemory", 0);
            memory_tracker.setUsedMemoryInBytes("maxfreememory", 0);
            memory_tracker.setUsedMemoryInBytes("maxusedmemory", 0);
            memory_tracker.setUsedMemoryInBytes("sumtotalmemory", 0);
            memory_tracker.setUsedMemoryInBytes("sumfreememory", 0);
            memory_tracker.setUsedMemoryInBytes("sumusedmemory", 0);
            memory_tracker.setUsedMemoryInBytes("numberofmemorychecks", 0);
            
			//memory_tracker.setUsedMemoryInBytes("accumulateddeepmemory", 0);
			//memory_tracker.setUsedMemoryInBytes("maxdeepmemoryallstacks", 0);
			//memory_tracker.setUsedMemoryInBytes(
			//		"accumulateddeepmemoryallstacks", 0);
			// memory_tracker.setUsedMemoryInBytes("maxUsedDeepMemory",0);
		}

		fileCounter = 0;
		long totalElapsedTime = 0;
		long total_memory_parser_time = 0;
		// long total_deep_memory_usage;

		TimeTracker timeTracker = new TimeTracker();
		timeTracker.start("total");

		long tempoParalelismo = 0;
		long matchingTime = 0;
		long count_noSLCA = 0;
		long count_onlySLCA = 0;
		long startTime = 0, charTime = 0, endTime = 0;

		boolean first = true;
		Thread tp1 = new Thread();
		Thread tp2 = new Thread();
		Thread tp3 = new Thread();
		Thread tp4 = new Thread();
		Thread tp5 = new Thread();
		Thread tp6 = new Thread();
		Thread tp7 = new Thread();
		Thread tp8 = new Thread(); 

		Parallel p1 = null;
		Parallel p2 = null;
		Parallel p3 = null;
		Parallel p4 = null;
		Parallel p5 = null;
		Parallel p6 = null;
		Parallel p7 = null;
		Parallel p8 = null;
		Sequential s2 = null;

		int i = 1;

		ArrayList<QueryGroup> aqp = new ArrayList<QueryGroup>();
		ArrayList<QueryGroup> aqp2 = new ArrayList<QueryGroup>();
		ArrayList<QueryGroup> aqp3 = new ArrayList<QueryGroup>();
		ArrayList<QueryGroup> aqp4 = new ArrayList<QueryGroup>();
		ArrayList<QueryGroup> aqp5 = new ArrayList<QueryGroup>();
		ArrayList<QueryGroup> aqp6 = new ArrayList<QueryGroup>();
		ArrayList<QueryGroup> aqp7 = new ArrayList<QueryGroup>();
		ArrayList<QueryGroup> aqp8 = new ArrayList<QueryGroup>();

		ArrayList<String> queries1 = new ArrayList<String>();

		if(numberOfThreads == 1){

			for (QueryGroup q : QGH.getGroupList()) {
				aqp.add(q);
				i++;
			}
		}	

		else if(numberOfThreads == 2){

			for (QueryGroup q : QGH.getGroupList()) {
				if (i > (QGH.getGroupList().size() / 2)) {
					aqp2.add(q);
				} else {
					aqp.add(q);
				}
				i++;
			}
		}

		else if(numberOfThreads == 4){
			for (QueryGroup q : QGH.getGroupList()) {
				if (i > (QGH.getGroupList().size() / (4.0/3))) {
					aqp4.add(q);
				} 
				else if(i > (QGH.getGroupList().size() / 2)){
					aqp3.add(q);				
				}
				else if(i > (QGH.getGroupList().size() / 4)){
					aqp2.add(q);				
				}
				else {
					aqp.add(q);
				}
				i++;
			}
		}

		else{
			for (QueryGroup q : QGH.getGroupList()) {
				if (i <= (QGH.getGroupList().size() / 8)) {
					aqp.add(q);
				}
			
				else if (i <= (QGH.getGroupList().size() / 4)) {
					aqp2.add(q);
				}
			
				else if (i <= (QGH.getGroupList().size() / (8/3))) {
					aqp3.add(q);
				}
			
				else if (i <= (QGH.getGroupList().size() / 2)) {
					aqp4.add(q);
				}
			
				else if (i <= (QGH.getGroupList().size() / (8/5))) {
					aqp5.add(q);
				}
			
				else if (i <= (QGH.getGroupList().size() / (4/3))) {
					aqp6.add(q);
				}
			
				else if (i <= (QGH.getGroupList().size() / (8/7))) {
					aqp7.add(q);
				}
			
				else {
					aqp8.add(q);
				}
				i++;
			}		
		}

		HashMap<String, ArrayList<QueryGroup>> HQG1 = new HashMap<String, ArrayList<QueryGroup>>();
		HashMap<String, ArrayList<QueryGroup>> HQG2 = new HashMap<String, ArrayList<QueryGroup>>();
		HashMap<String, ArrayList<QueryGroup>> HQG3 = new HashMap<String, ArrayList<QueryGroup>>();
		HashMap<String, ArrayList<QueryGroup>> HQG4 = new HashMap<String, ArrayList<QueryGroup>>();
		HashMap<String, ArrayList<QueryGroup>> HQG5 = new HashMap<String, ArrayList<QueryGroup>>();
		HashMap<String, ArrayList<QueryGroup>> HQG6 = new HashMap<String, ArrayList<QueryGroup>>();
		HashMap<String, ArrayList<QueryGroup>> HQG7 = new HashMap<String, ArrayList<QueryGroup>>();
		HashMap<String, ArrayList<QueryGroup>> HQG8 = new HashMap<String, ArrayList<QueryGroup>>();
		

		
		for (Map.Entry<String, ArrayList<QueryGroup>> pair : QGH.getGroupHash()
				.entrySet()) {

			for (QueryGroup q : aqp) {

				if (pair.getValue().contains(q)) {
					HQG1.put(pair.getKey(), aqp);

					break;
				}
			}
			if(numberOfThreads > 1){
			for (QueryGroup q : aqp2) {
				if (pair.getValue().contains(q)) {
					HQG2.put(pair.getKey(), aqp2);
					break;
				}
			}
			if(numberOfThreads > 2){
			for (QueryGroup q : aqp3) {
				if (pair.getValue().contains(q)) {
					HQG3.put(pair.getKey(), aqp3);
					break;
				}
			}
			
			for (QueryGroup q : aqp4) {
				if (pair.getValue().contains(q)) {
					HQG4.put(pair.getKey(), aqp4);
					break;
				}
			}
			if(numberOfThreads > 4){
			for (QueryGroup q : aqp5) {

				if (pair.getValue().contains(q)) {
					HQG5.put(pair.getKey(), aqp5);

					break;
				}
			}

			for (QueryGroup q : aqp6) {
				if (pair.getValue().contains(q)) {
					HQG6.put(pair.getKey(), aqp6);
					break;
				}
			}
			
			for (QueryGroup q : aqp7) {
				if (pair.getValue().contains(q)) {
					HQG7.put(pair.getKey(), aqp7);
					break;
				}
			}
			
			for (QueryGroup q : aqp8) {
				if (pair.getValue().contains(q)) {
					HQG8.put(pair.getKey(), aqp8);
					break;
				}
			}
			}
			}
			}
		}
		
		

		QueryGroupHash QGH1 = new QueryGroupHash(ELCASemantics);
		QueryGroupHash QGH2 = new QueryGroupHash(ELCASemantics);
		QueryGroupHash QGH3 = new QueryGroupHash(ELCASemantics);
		QueryGroupHash QGH4 = new QueryGroupHash(ELCASemantics);
		QueryGroupHash QGH5 = new QueryGroupHash(ELCASemantics);
		QueryGroupHash QGH6 = new QueryGroupHash(ELCASemantics);
		QueryGroupHash QGH7 = new QueryGroupHash(ELCASemantics);
		QueryGroupHash QGH8 = new QueryGroupHash(ELCASemantics);

		QGH1.setGroupHash(HQG1);
		QGH2.setGroupHash(HQG2);
		QGH3.setGroupHash(HQG3);
		QGH4.setGroupHash(HQG4);
		QGH5.setGroupHash(HQG5);
		QGH6.setGroupHash(HQG6);
		QGH7.setGroupHash(HQG7);
		QGH8.setGroupHash(HQG8);

		QGH1.setGroupList(aqp);
		QGH2.setGroupList(aqp2);
		QGH3.setGroupList(aqp3);
		QGH4.setGroupList(aqp4);
		QGH5.setGroupList(aqp5);
		QGH6.setGroupList(aqp6);
		QGH7.setGroupList(aqp7);
		QGH8.setGroupList(aqp8);

		QGH1.setEachGroupSize(QGH1.getGroupList().size());
		QGH2.setEachGroupSize(QGH2.getGroupList().size());
		QGH3.setEachGroupSize(QGH3.getGroupList().size());
		QGH4.setEachGroupSize(QGH4.getGroupList().size());
		QGH5.setEachGroupSize(QGH5.getGroupList().size());
		QGH6.setEachGroupSize(QGH6.getGroupList().size());
		QGH7.setEachGroupSize(QGH7.getGroupList().size());
		QGH8.setEachGroupSize(QGH8.getGroupList().size());

		QGH1.setNumberOfGroups(numberOfStacks/numberOfThreads);
		QGH2.setNumberOfGroups(numberOfStacks/numberOfThreads);
		QGH3.setNumberOfGroups(numberOfStacks/numberOfThreads);
		QGH4.setNumberOfGroups(numberOfStacks/numberOfThreads);
		QGH5.setNumberOfGroups(numberOfStacks/numberOfThreads);
		QGH6.setNumberOfGroups(numberOfStacks/numberOfThreads);
		QGH7.setNumberOfGroups(numberOfStacks/numberOfThreads);
		QGH8.setNumberOfGroups(numberOfStacks/numberOfThreads);

		long time = System.currentTimeMillis();

        hashResult = new HashResult(maxQueryQuant);
        
        
		for (String XMLFile : QP.getXMLFileList()) {
			if (XMLFile.equals("dblp.dtd")) {
				continue; // FIXME: ??
			}
			fileCounter++;
			// duas proximas linhas iniciam o parser para todos aquivos novos

			if (first) {

				p1 = new Parallel(ELCASemantics, QGH2, trackMemory,
						memory_tracker, pushingType, termOccurrenceHash2, QP,
						XMLFile, time, hashResult);
				
				p3 = new Parallel(ELCASemantics, QGH3, trackMemory,
						memory_tracker, pushingType, termOccurrenceHash3, QP,
						XMLFile, time, hashResult);
				
				p4 = new Parallel(ELCASemantics, QGH4, trackMemory,
						memory_tracker, pushingType, termOccurrenceHash4, QP,
						XMLFile, time, hashResult);
				
				p5 = new Parallel(ELCASemantics, QGH5, trackMemory,
						memory_tracker, pushingType, termOccurrenceHash5, QP,
						XMLFile, time, hashResult);
				
				p6 = new Parallel(ELCASemantics, QGH6, trackMemory,
						memory_tracker, pushingType, termOccurrenceHash6, QP,
						XMLFile, time, hashResult);
				
				p7 = new Parallel(ELCASemantics, QGH7, trackMemory,
						memory_tracker, pushingType, termOccurrenceHash7, QP,
						XMLFile, time, hashResult);
				
				p8 = new Parallel(ELCASemantics, QGH8, trackMemory,
						memory_tracker, pushingType, termOccurrenceHash8, QP,
						XMLFile, time, hashResult);

				// tp2 = new Thread(p2);
				s2 = new Sequential(ELCASemantics, QGH1, trackMemory,
						memory_tracker, pushingType, termOccurrenceHash, QP,
						XMLFile, hashResult);

				tp1 = new Thread(p1);
				tp3 = new Thread(p3);
				tp4 = new Thread(p4);
				tp5 = new Thread(p5);
				tp6 = new Thread(p6);
				tp7 = new Thread(p7);
				tp8 = new Thread(p8);
				
				if(numberOfThreads > 1){
					tp1.start();
					if(numberOfThreads > 2){
						tp3.start();
						tp4.start();
						if(numberOfThreads > 4){
							tp5.start();
							tp6.start();
							tp7.start();
							tp8.start();
						}
					}
				}

				s2.run();

				// tp2.start();

				first = false;

			}

			else {
				p1.setQGHs(QGH2);
				p1.setXmlFiles(XMLFile);
				p3.setQGHs(QGH3);
				p3.setXmlFiles(XMLFile);
				p4.setQGHs(QGH4);
				p4.setXmlFiles(XMLFile);
				p5.setQGHs(QGH5);
				p5.setXmlFiles(XMLFile);
				p6.setQGHs(QGH6);
				p6.setXmlFiles(XMLFile);
				p7.setQGHs(QGH7);
				p7.setXmlFiles(XMLFile);
				p8.setQGHs(QGH8);
				p8.setXmlFiles(XMLFile);
				// p2.setQGHs(QGH2);
				// p2.setXmlFiles(XMLFile);
				s2.setQGH(QGH1);
				s2.setXMLFile(XMLFile);
				s2.run();
			}

			if (fileCounter == QP.getXMLFileList().length) {
				p1.setFim(true);
				p3.setFim(true);
				p4.setFim(true);
				p5.setFim(true);
				p6.setFim(true);
				p7.setFim(true);
				p8.setFim(true);
			}

		}
        //QGH.print();
        //QGH.printQueries();
        
        
		long tt = System.currentTimeMillis() - time;
		long media = tt + p1.getTempo() + p3.getTempo() + p4.getTempo() + p5.getTempo() + p6.getTempo() + p7.getTempo() + p8.getTempo();
		startTime = s2.getStartTime()+p1.getStartTime() + p3.getStartTime() + p4.getStartTime() + 
				         p5.getStartTime() + p6.getStartTime() + p7.getStartTime() + p8.getStartTime();
		endTime = 	s2.getEndTime()+p1.getEndTime() + p3.getEndTime() + p4.getEndTime() + 
		         		p5.getEndTime() + p6.getEndTime() + p7.getEndTime() + p8.getEndTime();
		charTime =  s2.getCharTime()+p1.getCharTime() + p3.getCharTime() + p4.getCharTime() + 
		         		p5.getCharTime() + p6.getCharTime() + p7.getCharTime() + p8.getCharTime();
        
        
        
        numberOfQueryChecks = s2.getNumberOfQueryChecks() + p1.getNumberOfQueryChecks() + p3.getNumberOfQueryChecks() + p4.getNumberOfQueryChecks() + p5.getNumberOfQueryChecks() + p6.getNumberOfQueryChecks() + p7.getNumberOfQueryChecks() + p8.getNumberOfQueryChecks();
        numberOfCheckedNodes = s2.getNumberOfCheckedNodes() + p1.getNumberOfCheckedNodes() + p3.getNumberOfCheckedNodes() + p4.getNumberOfCheckedNodes() + p5.getNumberOfCheckedNodes() + p6.getNumberOfCheckedNodes() + p7.getNumberOfCheckedNodes() + p8.getNumberOfCheckedNodes();
        numberOfNodes = s2.getNumberOfNodes() + p1.getNumberOfNodes() + p3.getNumberOfNodes() + p4.getNumberOfNodes() + p5.getNumberOfNodes() + p6.getNumberOfNodes() + p7.getNumberOfNodes() + p8.getNumberOfNodes();
        
        
		if(numberOfThreads > 1)
			media = media/(numberOfThreads);
		else
			media = tt;

		try {
			tp1.join();
			// tp2.join();
			tp3.join();
			tp4.join();
			tp5.join();
			tp6.join();
			tp7.join();
			tp8.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println("Memory tracker?:"+memory_tracker);
		//System.out.println("mediatime;totaltime");
		
		//print response times
		if (!trackMemory){ 
            //System.out.println("mediaTime;"+media+";maxTim;"+tt+";startTime;"+startTime+";chartime;"+charTime+";endTime;"+endTime+";#Comparisons;"+
            //                   QGH.getTotalNumberOfQueryComparisons()+
            //     ";#EvaluatedQueries;"+QGH.getNumberOfEvaluatedQueries()+
            //     ";mediaPerQuery;"+QGH.getTotalNumberOfQueryComparisons()/QGH.getNumberOfEvaluatedQueries());
            System.out.println(tt);
            //System.out.print("; Number of nodes; "+numberOfNodes);
            //System.out.print("; Number of query checks; "+numberOfQueryChecks);
            //System.out.print("; Number of checked nodes; "+numberOfCheckedNodes);
            //System.out.print("; Number of evaluated queries; "+QGH.getNumberOfEvaluatedQueries());
            //System.out.println("; Number of queries comparisons; "+QGH.getTotalNumberOfQueryComparisons());
			//System.out.println(tt);
		}
        //System.out.println("Lista de consultas");
        //QP.printArrayOfQuery();
		//System.out.println("número de nós: " + cont);
		//System.out.println("Number of non-SLCA nodes: " + count_noSLCA);
		//System.out.println("Number of only-SLCA nodes: " + count_onlySLCA);
		//System.out.println("Matching time: " + matchingTime + "(miliseconds)");
		//System.out.println("Start time (miliseconds): " + startTime);
		//System.out.println("char time (miliseconds): " + charTime);
		//System.out.println("end time (miliseconds): " + endTime);

		// termOccurrenceHash.print();

		long max_memory_tf, med_memory_tf, max_memory_deep, med_memory_deep, max_memory_deep_allstacks, med_memory_deep_allstacks;

		timeTracker.end("total");
		
		//System.out.println("Total of evaluated queries: "+(QGH1.getTotalAmountOfEvalutedQueries()+
		//		QGH2.getTotalAmountOfEvalutedQueries()+
		//		QGH3.getTotalAmountOfEvalutedQueries()+
		//		QGH4.getTotalAmountOfEvalutedQueries()+
		//		QGH5.getTotalAmountOfEvalutedQueries()+
		//		QGH6.getTotalAmountOfEvalutedQueries()+
		//		QGH7.getTotalAmountOfEvalutedQueries()+
		//		QGH8.getTotalAmountOfEvalutedQueries())
		//		);
		

		if (trackMemory) {
     
             //System.out.println("Number of files: " + QP.XMLFileList.length);
             
             long numberOfMemoryChecks = memory_tracker.getMemoryUsage("numberofmemorychecks");
             System.out.print("#memory_checks;"+numberOfMemoryChecks);
             
             //-- total memory allocated --//
             long maxTotalMemory = memory_tracker.getMemoryUsageInKB("maxtotalmemory");
             long sumTotalMemory = memory_tracker.getMemoryUsageInKB("sumtotalmemory");
             //System.out.println("Average of total memory allocated (Kbtyes): "+ Math.round(sumTotalMemory/numberOfMemoryChecks));
             
             //-- free memory released --//
             long maxFreeMemory = memory_tracker.getMemoryUsageInKB("maxfreememory");
             //System.out.println("Max of free memory released (Kbytes): " + maxFreeMemory);
             long sumFreeMemory = memory_tracker.getMemoryUsageInKB("sumfreememory");
             //System.out.println("Average of free memory released (Kbtyes): "+ Math.round(sumFreeMemory/numberOfMemoryChecks));
             
             
             //-- used memory (total allocated minus free memory released) --//
             long maxUsedMemory = memory_tracker.getMemoryUsageInKB("maxusedmemory");
             System.out.print(";Max_of_used_memory(Kbytes);" + maxUsedMemory);
             long sumUsedMemory = memory_tracker.getMemoryUsageInKB("sumusedmemory");
             System.out.println(";Average_used_memory(Kbtyes);"+ Math.round(sumUsedMemory/numberOfMemoryChecks));
             //System.out.print("maxusedmemory;"+);
             
	//System.out.println(media + "\t" + maxTotalMemory + "\t" + Math.round(sumTotalMemory/numberOfMemoryChecks)+ "\t" +
    //                    maxFreeMemory + "\t" + Math.round(sumFreeMemory/numberOfMemoryChecks) + "\t" + maxUsedMemory + "\t" +
    //                    Math.round(sumUsedMemory/numberOfMemoryChecks));

            //System.out.println("---------------------------------------------------------------");
                //System.out.println("------------------------- M E M O R I A -----------------------");
                //System.out.println("Total de memoria antes da thread começar: "+ Math.round(Runtime.getRuntime().totalMemory()/1024)+ " kB");
                //System.out.println("Total de memoria liberada antes da thread começar: "+ Math.round(Runtime.getRuntime().freeMemory()/1024)+ " kB");
                //System.out.println("Diferença: " + (Math.round(Runtime.getRuntime().totalMemory()/1024)- Math.round(Runtime.getRuntime().freeMemory()/1024)) +" kB");
                //System.out.println("---------------------------------------------------------------");
                //System.out.println("Total de memoria depois da thread terminar: "+ Math.round(Runtime.getRuntime().totalMemory()/1024)+ " kB");
                
                //System.out.println("Total de memoria liberada depois da thread terminar: "+ Math.round(Runtime.getRuntime().freeMemory()/1024)+ " kB");
                //System.out.println("Diferença: " + (Math.round(Runtime.getRuntime().totalMemory()/1024)- Math.round(Runtime.getRuntime().freeMemory()/1024)) +" kB");
                //System.out.println("---------------------------------------------------------------");
            
            
        
            
            //max_memory_deep = memory_tracker.getMemoryUsageInKB("maxdeepmemory");
			//System.out.println("Max usage memory with deep method  (Kbytes): "+ max_memory_deep);
			//med_memory_deep =
            //    Math.round(memory_tracker.getMemoryUsageInKB("accumulateddeepmemory")/(QP.XMLFileList.length * 1.0f));
			//System.out.println("Average for highest tree nodes on dataset (deep method) (Kbyte): "+ med_memory_deep);
			//max_memory_deep_allstacks = memory_tracker.getMemoryUsageInKB("maxdeepmemoryallstacks");
			//System.out.println("Max usage memory with deep method for all stacks (Kbytes): "+ max_memory_deep_allstacks);
			//med_memory_deep_allstacks =
            //    Math.round(memory_tracker.getMemoryUsageInKB("accumulateddeepmemoryallstacks")/(QP.XMLFileList.length * 1.0f));
			//System.out
			//		.println("Average for highest tree nodes on dataset for all stacks (deep method) (Kbytes): "
			//				+ med_memory_deep_allstacks); //totalMemory is measured in bytes
            //System.out.println("Total de memoria no final, depois da thread acabar."+ Math.round(Runtime.getRuntime().totalMemory()/1024)+ " kB");
            //System.out.println("Total de memoria liberada pelo GC - 21/Maio/2018: "+ Math.round(Runtime.getRuntime().freeMemory()/1024)+ " kB");
            // memory_tracker.setUsedMemoryInB("maxdeepmemoryallstacks", 0);
			// memory_tracker.setUsedMemoryInB("accumulateddeepmemoryallstacks",
			// 0);
			/*
			 * FIXME:Resolver BD db.insertOrUpdateNewMemoryResult(algorithm,
			 * semantics, total_memory_parser_time,QP.XMLFileList.length,
			 * base_dir,
			 * query_file,maxQueryQuant,numberOfStacks,initial_memory_size,
			 * max_memory_tf, med_memory_tf, max_memory_deep,
			 * med_memory_deep,count_onlySLCA, count_noSLCA); } else{
			 * System.out.println("Number of Stacks: "+numberOfStacks+
			 * " - Total elapsed time (ms): "+totalElapsedTime);
			 * 
			 * db.insertNewTimeResult(algorithm, semantics, totalElapsedTime,
			 * base_dir, query_file, maxQueryQuant, numberOfStacks,
			 * "mkestreamplus", count_noSLCA, count_onlySLCA);
			 * //db.insertNewTimeResult(algorithm, semantics, totalElapsedTime,
			 * base_dir, query_file, // maxQueryQuant, numberOfStacks,
			 * pushing_type, // count_noSLCA, count_onlySLCA);
			 */

		}

		// System.out.println("tempo start: "+SE.startTime);
		// System.out.println("tempo char:  "+SE.charTime);
		// System.out.println("tempo end:   "+SE.endTime);
		// System.out.println("processTextTime:   "+SE.processTextTime);
		// System.out.println("Pushig time: "+(SE.pushingTime));
		// System.out.println("setGroupStackTime: "+SE.setGroupStackTime);
		// System.out.println("addAllTime: "+SE.addAllTime);
		// System.out.println("termInstances: "+SE.termInstancesTime);
		// System.out.println("Fim da execucao: " + DateUtils.now());
        
        
        //System.out.println();
        //System.out.println("---------------------------------------------------");
        //QGH.printHowManyQueriesPerTerm();
        //System.out.println("---------------------------------------------------");
        //System.out.println("---------------------------------------------------");
        //QGH.printHowManyOccurencesPerTermInDataSet();
        //QGH.printHowManyOccurencesPerTermInQueriesAndDataSets();
        //System.out.println("---------------------------------------------------");
        
        //hashResult.printAllHashResult();
        
	}

	public int getNumberOfQueries() {
		return numberOfQueries;
	}

	public void setNumberOfQueries(int numberOfQueries) {
		this.numberOfQueries = numberOfQueries;
	}

	/**
	 * Gets XMLFilePath.
	 *
	 * @return Value of XMLFilePath.
	 */
	public String getXMLFilePath() {
		return XMLFilePath;
	}

	/**
	 * Gets XMLFileList.
	 *
	 * @return Value of XMLFileList.
	 */
	public String[] getXMLFileList() {
		return XMLFileList;
	}
}
