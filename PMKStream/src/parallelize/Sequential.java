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
    long t = 0;
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
        endTime = 0;
        charTime = 0;
        startTime = 0;
        this.hashResult = hashResult;
    }

    public double getNumberOfCheckedNodes() {
        return numberOfCheckedNodes;
    }

    public double getNumberOfQueryChecks() {
        return numberOfQueryChecks;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getCharTime() {
        return charTime;
    }

    public double getNumberOfNodes() {
        return this.numberOfNodes;
    }

    public void run() {
        long initial = System.currentTimeMillis();
        
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
        } catch (Exception e) {
            System.out
                    .println("Erro in Query Processor: Sequential " + e.getMessage());
        }
        SE.getHashResult().resetCountNoSLCAAndOnlySLCA();
        SE.getHashResult().countNoSLCAAndOnlySLCA();
        QueryProcessor.cont += SE.getHashResult().getCountOnlySLCA();
        count_noSLCA += SE.getHashResult().getCountNoSLCA();
        count_onlySLCA += SE.getHashResult().getCountOnlySLCA();

        startTime = SE.getStartTime();
        charTime = SE.getCharTime();
        endTime = SE.getEndTime();
        this.numberOfCheckedNodes = SE.getNumberOfCheckedNodes();
        this.numberOfQueryChecks = SE.getNumberOfQueryChecks();
        this.numberOfNodes = SE.getNumberOfNodes();
        SE.cleanResultsAndTermOccurrences();
        
        t = System.currentTimeMillis() - initial;

    }
    
    public long getTempo() {
        return t;
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
