package parallelize;

import java.util.ArrayList;

import query.QueryGroupHash;
import query.TermOccurrences;
import util.MemoryTracker;
import engine.QueryProcessor;
import engine.SaxParser;
import engine.SearchEngine;
import node.HashResult;

public class Parallel implements Runnable {

    boolean ELCASemantics;
    QueryGroupHash QGH;
    ArrayList<QueryGroupHash> QGHs = new ArrayList<QueryGroupHash>();
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
    ArrayList<String> xmlFiles = new ArrayList<String>();
    boolean first = true;
    boolean fim = false;
    long time;
    long endTime;
    long charTime;
    long startTime;
    long t;
    public double numberOfCheckedNodes;
    public double numberOfQueryChecks;
    public double numberOfNodes;
    private HashResult hashResult;

    public Parallel(boolean eLCASemantics, QueryGroupHash qGH,
            boolean trackMemory, MemoryTracker memory_tracker,
            String pushingType, TermOccurrences termOccurrenceHash,
            QueryProcessor qP, String xMLFile, long tempo, HashResult hashResult) {
        super();
        ELCASemantics = eLCASemantics;
        QGH = qGH;
        this.trackMemory = trackMemory;
        this.memory_tracker = memory_tracker;
        this.pushingType = pushingType;
        this.termOccurrenceHash = termOccurrenceHash;
        QP = qP;
        XMLFile = xMLFile;
        time = tempo;
        t = 0;
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

    public double getNumberOfNodes() {
        return this.numberOfNodes;
    }

    public long getTempo() {
        return t;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getCharTime() {
        return charTime;
    }

    public long getEndTime() {
        return endTime;
    }
    
    public void run() {
        while (!fim || !QGHs.isEmpty()) {

            boolean acaba = false;

            if (!fim) {
                acaba = true;
            }

            if (!first) {
                if (QGHs.isEmpty()) {
                    t = System.currentTimeMillis() - time;
                }
                while (QGHs.isEmpty()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }

                    if (fim && acaba) {
                        break;
                    }
                }

                if (fim && acaba && QGHs.isEmpty()) {
                    break;
                }

                QueryGroupHash q = null;

                for (QueryGroupHash qh : QGHs) {
                    q = qh;
                    break;
                }

                QGH = q;

                QGHs.remove(q);

                String xFile = null;

                for (String file : xmlFiles) {
                    xFile = file;
                    break;
                }

                XMLFile = xFile;

                xmlFiles.remove(xFile);

            }

            SearchEngine SE = new SearchEngine(ELCASemantics, QGH,
                    QP.getNumberOfQueries(), trackMemory, memory_tracker,
                    pushingType, termOccurrenceHash, hashResult);
            SaxParser parser = new SaxParser();

            try {
                totalProcessingTimePerFile = System.currentTimeMillis();

                parser.parse(QP.getXMLFilePath() + XMLFile, SE);

                long elapsedTime = (System.currentTimeMillis() - totalProcessingTimePerFile);
                totalElapsedTime = totalElapsedTime + elapsedTime;

            } catch (Exception e) {
                System.out.println("Erro in Parallel: - aqui! " + e.getMessage());
            }
            SE.getHashResult().resetCountNoSLCAAndOnlySLCA();
            SE.getHashResult().countNoSLCAAndOnlySLCA();

            QueryProcessor.cont += SE.getHashResult().getCountOnlySLCA();
            count_noSLCA += SE.getHashResult().getCountNoSLCA();
            count_onlySLCA += SE.getHashResult().getCountOnlySLCA();

            endTime = SE.getEndTime();

            charTime = SE.getCharTime();
            startTime = SE.getStartTime();
            SE.cleanResultsAndTermOccurrences();
            this.numberOfCheckedNodes = SE.getNumberOfCheckedNodes();
            this.numberOfQueryChecks = SE.getNumberOfQueryChecks();
            this.numberOfNodes = SE.getNumberOfNodes();
            first = false;
        }
        t = System.currentTimeMillis() - time;
    }

    public void setQGHs(QueryGroupHash q) {
        QGHs.add(q);
    }

    public void setXmlFiles(String file) {
        this.xmlFiles.add(file);
    }

    public boolean isFim() {
        return fim;
    }

    public void setFim(boolean fim) {
        this.fim = fim;
    }

}
