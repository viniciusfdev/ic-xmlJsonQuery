package query;

import node.ResultNode;

import java.util.ArrayList;

/**
 * @version 2.0
 * @author Evandrino Barros, Jônatas Tonholo
 */
public class QueryResult {
    private int numConsulta;
    private String termoConsulta;
    private int numArquivo;
    private String arquivo;
    private String processador;
    private int quantNosOnlySLCA;
    private int quantNosNonSLCA;
    private long tempoTotal;

    private ArrayList<ResultNode> resultNodeList;

    /**
     * Default Constructor
     */
    public QueryResult() {
        this.resultNodeList = new ArrayList();
    }

    /**
     * Add the Result Node in the Result Node List
     * @param resultNode
     */
    public void addResultNode(ResultNode resultNode){
        this.resultNodeList.add(resultNode);
    }

    /**
     * Add the Result Node with the Node Type in the Result Node List
     * @param resultNode
     * @param nodeType
     */
    public void addResultNode (ResultNode resultNode, String nodeType) {
        resultNode.setNodeType(nodeType);
        this.resultNodeList.add(resultNode);
    }

    /**
     * Get the Query Insert Statment
     * @return
     */
    public String getQueryInsertStatement () {
        return ("insert into consulta values ("+this.numConsulta
                +",\""+this.termoConsulta+"\","
                +this.numArquivo
                +",\""+this.arquivo+"\","
                +"\""+this.processador+"\","
                +this.quantNosOnlySLCA+","
                +this.tempoTotal+","
                +this.quantNosNonSLCA+")");
    }

    /**
     * Gets numConsulta.
     *
     * @return Value of numConsulta.
     */
    public int getNumConsulta() {
        return numConsulta;
    }

    /**
     * Gets processador.
     *
     * @return Value of processador.
     */
    public String getProcessador() {
        return processador;
    }

    /**
     * Sets new resultNodeList.
     *
     * @param resultNodeList New value of resultNodeList.
     */
    public void setResultNodeList(ArrayList<ResultNode> resultNodeList) {
        this.resultNodeList = resultNodeList;
    }

    /**
     * Gets termoConsulta.
     *
     * @return Value of termoConsulta.
     */
    public String getTermoConsulta() {
        return termoConsulta;
    }

    /**
     * Sets new quantNosOnlySLCA.
     *
     * @param quantNosOnlySLCA New value of quantNosOnlySLCA.
     */
    public void setQuantNosOnlySLCA(int quantNosOnlySLCA) {
        this.quantNosOnlySLCA = quantNosOnlySLCA;
    }

    /**
     * Sets new processador.
     *
     * @param processador New value of processador.
     */
    public void setProcessador(String processador) {
        this.processador = processador;
    }

    /**
     * Gets arquivo.
     *
     * @return Value of arquivo.
     */
    public String getArquivo() {
        return arquivo;
    }

    /**
     * Gets resultNodeList.
     *
     * @return Value of resultNodeList.
     */
    public ArrayList<ResultNode> getResultNodeList() {
        return resultNodeList;
    }

    /**
     * Gets numArquivo.
     *
     * @return Value of numArquivo.
     */
    public int getNumArquivo() {
        return numArquivo;
    }

    /**
     * Gets quantNosOnlySLCA.
     *
     * @return Value of quantNosOnlySLCA.
     */
    public int getQuantNosOnlySLCA() {
        return quantNosOnlySLCA;
    }

    /**
     * Sets new arquivo.
     *
     * @param arquivo New value of arquivo.
     */
    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    /**
     * Sets new numConsulta.
     *
     * @param numConsulta New value of numConsulta.
     */
    public void setNumConsulta(int numConsulta) {
        this.numConsulta = numConsulta;
    }

    /**
     * Gets quantNosNonSLCA.
     *
     * @return Value of quantNosNonSLCA.
     */
    public int getQuantNosNonSLCA() {
        return quantNosNonSLCA;
    }

    /**
     * Gets tempoTotal.
     *
     * @return Value of tempoTotal.
     */
    public long getTempoTotal() {
        return tempoTotal;
    }

    /**
     * Sets new quantNosNonSLCA.
     *
     * @param quantNosNonSLCA New value of quantNosNonSLCA.
     */
    public void setQuantNosNonSLCA(int quantNosNonSLCA) {
        this.quantNosNonSLCA = quantNosNonSLCA;
    }

    /**
     * Sets new numArquivo.
     *
     * @param numArquivo New value of numArquivo.
     */
    public void setNumArquivo(int numArquivo) {
        this.numArquivo = numArquivo;
    }

    /**
     * Sets new termoConsulta.
     *
     * @param termoConsulta New value of termoConsulta.
     */
    public void setTermoConsulta(String termoConsulta) {
        this.termoConsulta = termoConsulta;
    }

    /**
     * Sets new tempoTotal.
     *
     * @param tempoTotal New value of tempoTotal.
     */
    public void setTempoTotal(long tempoTotal) {
        this.tempoTotal = tempoTotal;
    }
}
