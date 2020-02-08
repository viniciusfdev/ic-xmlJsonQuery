package parallelize;

import java.util.ArrayList;

import query.QueryGroupHash;
import query.TermOccurrences;
import util.MemoryTracker;
import engine.QueryProcessor;
import engine.SaxParser;
import engine.SearchEngine;
import node.HashResult;

public class Sequential {

	boolean ELCASemantics;
	QueryGroupHash QGH;
	boolean trackMemory;
	MemoryTracker memory_tracker;
	String pushingType;
	TermOccurrences termOccurrenceHash;
	long totalProcessingTimePerFile = 0;
	long totalElapsedTime = 0;
	QueryProcessor QP;
	long count_noSLCA = 0;
	long count_onlySLCA = 0;
	String XMLFile;
	long endTime;
	long charTime;
	long startTime;
    public double numberOfCheckedNodes;
    public double numberOfQueryChecks;
    public double numberOfNodes;
    private HashResult hashResult;

	public Sequential(boolean eLCASemantics, QueryGroupHash qGH,
			boolean trackMemory, MemoryTracker memory_tracker,
			String pushingType, TermOccurrences termOccurrenceHash,
			QueryProcessor qP, String xMLFile, HashResult hashResult) {
		super();
		ELCASemantics = eLCASemantics;
		QGH = qGH;
		this.trackMemory = trackMemory;
		this.memory_tracker = memory_tracker;
		this.pushingType = pushingType;
		this.termOccurrenceHash = termOccurrenceHash;
		QP = qP;
		XMLFile = xMLFile;
		endTime=0;
		charTime=0;
		startTime=0;
        this.hashResult = hashResult;
	}
    
    public double getNumberOfCheckedNodes(){
        return numberOfCheckedNodes;
    }
    
    public double getNumberOfQueryChecks(){
        return numberOfQueryChecks;
    }

	public long getEndTime(){
		return endTime;
	}

	public long getStartTime() {
		return startTime;
	}
	
	public long getCharTime() {
		return charTime;
	}	
	
    public double getNumberOfNodes(){
        return this.numberOfNodes;
    }
	
	
	public void run() {
			
			SearchEngine SE = new SearchEngine(ELCASemantics, QGH,
					QP.getNumberOfQueries(), trackMemory, memory_tracker,
					pushingType, termOccurrenceHash, hashResult);
			SaxParser parser = new SaxParser();

			try {
				totalProcessingTimePerFile = System.currentTimeMillis();
				// parser.parse(QP.getXMLFilePath()+"/"+XMLFile, SE); //FIXME:
				// Verificar se está correta a utilização abaixo

				parser.parse(QP.getXMLFilePath() + XMLFile, SE);

				long elapsedTime = (System.currentTimeMillis() - totalProcessingTimePerFile);
				totalElapsedTime = totalElapsedTime + elapsedTime;
				/*if (trackMemory) {
					// long initialFreeMemory =
					// memory_tracker.getMemoryUsage("initial");
					long currentFreeTotalMemory = memory_tracker
							.getMemoryUsage("totalfreememory");
					// long accumulatedFreeTotalMemory =
					// memory_tracker.getMemoryUsage("accumulatedtotalfreememory");
					memory_tracker.accumulateMemoryUsage(
							"accumulatedtotalfreememory",
							currentFreeTotalMemory);// -initialFreeMemory));
					long currentDeepMemory = memory_tracker
							.getMemoryUsage("deepmemory");
					memory_tracker.accumulateMemoryUsage(
							"accumulateddeepmemory", currentDeepMemory);
					long currentDeepMemory_allstacks = memory_tracker
							.getMemoryUsage("deepmemory_allstacks");
					memory_tracker.accumulateMemoryUsage(
							"accumulateddeepmemoryallstacks",
							currentDeepMemory_allstacks);
				}*/
				
				// QueryResult qr = new QueryResult();
				// qr.setArquivo(XMLFile);
				// qr.setNumArquivo(fileCounter);
				// qr.setProcessador(aArgs[2]);
				// qr.setTempoTotal(elapsedTime);
				// if (semantics.equals("ELCA"))
				// termOccurrenceHash.cleanOccurrences();
			}

			catch (Exception e) {
				System.out
						.println("Erro in Query Processor: Sequential " + e.getMessage());
			}
			SE.getHashResult().resetCountNoSLCAAndOnlySLCA();
			SE.getHashResult().countNoSLCAAndOnlySLCA();
			// System.out.println("Number of SCLA nodes: "+SE.getHashResult().count_onlySLCA);
			// System.out.println("Number of onSCLA nodes: "+SE.getHashResult().count_noSLCA);
			QueryProcessor.cont += SE.getHashResult().getCountOnlySLCA();
			count_noSLCA += SE.getHashResult().getCountNoSLCA();
			count_onlySLCA += SE.getHashResult().getCountOnlySLCA();

			// count_onlySLCA += SE.number_SLCA;
			// count_noSLCA += SE.number_nonSLCA;

			// QueryProcessor.recordResultsInDB (SE, aArgs, XMLFile,
			// fileCounter, db, pushing_type);
			startTime = SE.getStartTime();
			charTime = SE.getCharTime();
			endTime = SE.getEndTime();
            this.numberOfCheckedNodes = SE.getNumberOfCheckedNodes();
            this.numberOfQueryChecks = SE.getNumberOfQueryChecks();
            this.numberOfNodes = SE.getNumberOfNodes();
			SE.cleanResultsAndTermOccurrences();

		
	}

	public QueryGroupHash getQGH() {
		return QGH;
	}

	public void setQGH(QueryGroupHash qGH) {
		QGH = qGH;
	}

	public String getXMLFile() {
		return XMLFile;
	}

	public void setXMLFile(String xMLFile) {
		XMLFile = xMLFile;
	}

	
}
