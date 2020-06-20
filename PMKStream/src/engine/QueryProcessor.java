package engine;

import database.Database;
import exception.MKStreamException;
import parallelize.Parallel;
import parallelize.Sequential;
import query.QueryGroup;
import query.QueryGroupHash;
import query.TermOccurrences;
import util.OnlyExtension;
import util.TimeTracker;
import node.HashResult;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static int numberOfQueries;
    public static int cont = 0;
    public static double numberOfQueryChecks = 0;
    public static double numberOfCheckedNodes = 0;
    public static double numberOfNodes = 0;
    private static HashResult hashResult;
    private static int nThreads = 1;
    private static long media = 0;
    private static String query_file;
    private static String absPath;

    /**
     * Constructor
     *
     * @param XMLFilePath
     * @param queriesFileName
     */
    public QueryProcessor(String XMLFilePath, String queriesFileName, String absPath) {
        this.absPath = absPath;
        this.numberOfQueries = 0;
        this.XMLFilePath = XMLFilePath;
        this.resultsSummary = new Hashtable();
        this.queriesFileName = queriesFileName;
        this.arrayOfQueries = new ArrayList<String>();

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

        this.XMLFileList = new java.io.File(this.XMLFilePath).list(xmlExt);

        java.util.Arrays.sort(this.XMLFileList);

    }

    /**
     * Print the XML File List
     */
    public void printXMLFileList() {
        for (int i = 0; i < this.XMLFileList.length; i++) {
            System.out.println(this.XMLFilePath + "/" + this.XMLFileList[i]);
        }
    }

    /**
     * Set the Array of Queries
     *
     * @param maxQuant
     */
    public void setArrayOfQueries(int maxQuant) {
        try {

            File f = new File(this.queriesFileName);
            BufferedReader in = new BufferedReader(new FileReader(f));
            String line;
            while ((line = in.readLine()) != null) {
                if (this.numberOfQueries == maxQuant) {

                    break;
                }

                this.arrayOfQueries.add(line.toLowerCase().trim());

                this.numberOfQueries++;
            }

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
    }

    public static void run(String args[]) throws FileNotFoundException {

        if (args.length < 9) {
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

        int fileCounter = 0;
        long totalProcessingTimePerFile = 0;

        query_file = args[1];

        QueryProcessor QP = new QueryProcessor(args[0], args[9] + "" + args[1], args[9]);

        String algorithm = args[2].trim();
        String semantics = new String(args[3].trim());

        int maxQueryQuant = args[4].equalsIgnoreCase("") ? 0 : new Integer(
                args[4].trim());
        String resultType = new String(args[5].trim());
        int numberOfStacks = args[6].equalsIgnoreCase("") ? 0 : new Integer(
                args[6].trim());
        int numberOfThreads = args[8].equalsIgnoreCase("") ? 0 : new Integer(
                args[8].trim());
        boolean ELCASemantics = false;

        nThreads = Integer.parseInt(args[8]);

        QP.setXMLFileList();

        QP.setArrayOfQueries(maxQueryQuant);

        int eachGroupSize = Math.round(maxQueryQuant / (numberOfStacks * 1.0f));

        QueryGroupHash QGH = new QueryGroupHash(ELCASemantics);

        try {
            QGH.groupQueryWithQueriesWithAnyCommonTerms(QP.arrayOfQueries,
                    numberOfStacks, eachGroupSize);
        } catch (MKStreamException e) {
            e.printStackTrace();

        }

        TermOccurrences termOccurrenceHash = null;

        TermOccurrences termOccurrenceHash2 = null;

        TermOccurrences termOccurrenceHash3 = null;

        TermOccurrences termOccurrenceHash4 = null;

        TermOccurrences termOccurrenceHash5 = null;

        TermOccurrences termOccurrenceHash6 = null;

        TermOccurrences termOccurrenceHash7 = null;

        TermOccurrences termOccurrenceHash8 = null;

        boolean trackMemory = false;
        util.MemoryTracker memory_tracker = new util.MemoryTracker();
        long initial_memory_size = 0;
        if (trackMemory) {
            memory_tracker.checkMemoryWithGC("initial");
        }

        if (trackMemory) {

            memory_tracker.setUsedMemoryInBytes("maxtotalmemory", 0);
            memory_tracker.setUsedMemoryInBytes("maxfreememory", 0);
            memory_tracker.setUsedMemoryInBytes("maxusedmemory", 0);
            memory_tracker.setUsedMemoryInBytes("sumtotalmemory", 0);
            memory_tracker.setUsedMemoryInBytes("sumfreememory", 0);
            memory_tracker.setUsedMemoryInBytes("sumusedmemory", 0);
            memory_tracker.setUsedMemoryInBytes("numberofmemorychecks", 0);

        }

        fileCounter = 0;
        long totalElapsedTime = 0;
        long total_memory_parser_time = 0;

        TimeTracker timeTracker = new TimeTracker();
        timeTracker.start("total");

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

        if (numberOfThreads == 1) {

            for (QueryGroup q : QGH.getGroupList()) {
                aqp.add(q);
                i++;
            }
        } else if (numberOfThreads == 2) {

            for (QueryGroup q : QGH.getGroupList()) {
                if (i > (QGH.getGroupList().size() / 2)) {
                    aqp2.add(q);
                } else {
                    aqp.add(q);
                }
                i++;
            }
        } else if (numberOfThreads == 4) {
            for (QueryGroup q : QGH.getGroupList()) {
                if (i > (QGH.getGroupList().size() / (4.0 / 3))) {
                    aqp4.add(q);
                } else if (i > (QGH.getGroupList().size() / 2)) {
                    aqp3.add(q);
                } else if (i > (QGH.getGroupList().size() / 4)) {
                    aqp2.add(q);
                } else {
                    aqp.add(q);
                }
                i++;
            }
        } else {
            for (QueryGroup q : QGH.getGroupList()) {
                if (i <= (QGH.getGroupList().size() / 8)) {
                    aqp.add(q);
                } else if (i <= (QGH.getGroupList().size() / 4)) {
                    aqp2.add(q);
                } else if (i <= (QGH.getGroupList().size() / (8 / 3))) {
                    aqp3.add(q);
                } else if (i <= (QGH.getGroupList().size() / 2)) {
                    aqp4.add(q);
                } else if (i <= (QGH.getGroupList().size() / (8 / 5))) {
                    aqp5.add(q);
                } else if (i <= (QGH.getGroupList().size() / (4 / 3))) {
                    aqp6.add(q);
                } else if (i <= (QGH.getGroupList().size() / (8 / 7))) {
                    aqp7.add(q);
                } else {
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
            if (numberOfThreads > 1) {
                for (QueryGroup q : aqp2) {
                    if (pair.getValue().contains(q)) {
                        HQG2.put(pair.getKey(), aqp2);
                        break;
                    }
                }
                if (numberOfThreads > 2) {
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
                    if (numberOfThreads > 4) {
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

        QGH1.setNumberOfGroups(numberOfStacks / numberOfThreads);
        QGH2.setNumberOfGroups(numberOfStacks / numberOfThreads);
        QGH3.setNumberOfGroups(numberOfStacks / numberOfThreads);
        QGH4.setNumberOfGroups(numberOfStacks / numberOfThreads);
        QGH5.setNumberOfGroups(numberOfStacks / numberOfThreads);
        QGH6.setNumberOfGroups(numberOfStacks / numberOfThreads);
        QGH7.setNumberOfGroups(numberOfStacks / numberOfThreads);
        QGH8.setNumberOfGroups(numberOfStacks / numberOfThreads);

        long time = System.currentTimeMillis();

        hashResult = new HashResult(maxQueryQuant);

        for (String XMLFile : QP.getXMLFileList()) {
            fileCounter++;

            if (first) {

                p1 = new Parallel(ELCASemantics, QGH1, trackMemory,
                        memory_tracker, pushingType, termOccurrenceHash, QP,
                        XMLFile, time, hashResult);

                p2 = new Parallel(ELCASemantics, QGH2, trackMemory,
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

//                if (numberOfThreads == 1) {
//                    s2 = new Sequential(ELCASemantics, QGH1, trackMemory,
//                            memory_tracker, pushingType, termOccurrenceHash, QP,
//                            XMLFile, hashResult);
//                }

                tp1 = new Thread(p1);
                tp2 = new Thread(p2);
                tp3 = new Thread(p3);
                tp4 = new Thread(p4);
                tp5 = new Thread(p5);
                tp6 = new Thread(p6);
                tp7 = new Thread(p7);
                tp8 = new Thread(p8);

                if (numberOfThreads >= 1) {
                    tp1.start();
                    if (numberOfThreads >= 2) {
                        tp2.start();
                        if (numberOfThreads >= 4) {
                            tp4.start();
                            tp3.start();
                            if (numberOfThreads >= 8) {
                                tp5.start();
                                tp6.start();
                                tp7.start();
                                tp8.start();
                            }
                        }
                    }
                }
                first = false;

            } else {
                p1.setQGHs(QGH1);
                p1.setXmlFiles(XMLFile);
                p2.setQGHs(QGH2);
                p2.setXmlFiles(XMLFile);
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

                if (numberOfThreads == 1) {
//                    s2.setQGH(QGH1);
//                    s2.setXMLFile(XMLFile);
//                    s2.run();
                    p1.setQGHs(QGH1);
                    p1.setXmlFiles(XMLFile);
                }
            }

            if (fileCounter == QP.getXMLFileList().length) {
                p1.setFim(true);
                p2.setFim(true);
                p3.setFim(true);
                p4.setFim(true);
                p5.setFim(true);
                p6.setFim(true);
                p7.setFim(true);
                p8.setFim(true);
            }

        }

        try {
            tp1.join();
            tp2.join();
            tp3.join();
            tp4.join();
            tp5.join();
            tp6.join();
            tp7.join();
            tp8.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        numberOfQueryChecks = s2.getNumberOfQueryChecks() + p1.getNumberOfQueryChecks() + p3.getNumberOfQueryChecks() + p4.getNumberOfQueryChecks() + p5.getNumberOfQueryChecks() + p6.getNumberOfQueryChecks() + p7.getNumberOfQueryChecks() + p8.getNumberOfQueryChecks();
//        numberOfCheckedNodes = s2.getNumberOfCheckedNodes() + p1.getNumberOfCheckedNodes() + p3.getNumberOfCheckedNodes() + p4.getNumberOfCheckedNodes() + p5.getNumberOfCheckedNodes() + p6.getNumberOfCheckedNodes() + p7.getNumberOfCheckedNodes() + p8.getNumberOfCheckedNodes();
//        numberOfNodes = s2.getNumberOfNodes() + p1.getNumberOfNodes() + p3.getNumberOfNodes() + p4.getNumberOfNodes() + p5.getNumberOfNodes() + p6.getNumberOfNodes() + p7.getNumberOfNodes() + p8.getNumberOfNodes();
        
//        System.out.println(p1.getTempo());
//        System.out.println(p2.getTempo());
//        System.out.println(p3.getTempo());
//        System.out.println(p4.getTempo());
//        System.out.println(p5.getTempo());
//        System.out.println(p6.getTempo());
//        System.out.println(p7.getTempo());
//        System.out.println(p8.getTempo());
        
        media = 0;
        if (numberOfThreads > 1) {
            long[] times = {p1.getTempo(), p2.getTempo(), p3.getTempo(), p4.getTempo(), p5.getTempo(), p6.getTempo(), p7.getTempo(), p8.getTempo()};
            for (long t : times) {
                if (t > media) {
                    media = t;
                }
            }
            media = media;
        } else {
            media = p1.getTempo();
        }

        long max_memory_tf, med_memory_tf, max_memory_deep, med_memory_deep, max_memory_deep_allstacks, med_memory_deep_allstacks;

        timeTracker.end("total");

        if (trackMemory) {

            long numberOfMemoryChecks = memory_tracker.getMemoryUsage("numberofmemorychecks");

            long maxTotalMemory = memory_tracker.getMemoryUsageInKB("maxtotalmemory");
            long sumTotalMemory = memory_tracker.getMemoryUsageInKB("sumtotalmemory");

            long maxFreeMemory = memory_tracker.getMemoryUsageInKB("maxfreememory");

            long sumFreeMemory = memory_tracker.getMemoryUsageInKB("sumfreememory");

            long maxUsedMemory = memory_tracker.getMemoryUsageInKB("maxusedmemory");

            long sumUsedMemory = memory_tracker.getMemoryUsageInKB("sumusedmemory");
        }

        writeTimeForSearch();

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

    public static void writeTimeForSearch() {
        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter(absPath + "results/time_" + query_file, true));
            String q = numberOfQueries + ";" + nThreads + ";" + media;
            wr.write(q, 0, q.length());
            wr.newLine();
            wr.close();
        } catch (IOException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
