package node;

/**
 * @version 2.0
 * @author Evandrino Barros, Jônatas Tonholo
 */
public class ResultNode {
    private String label;
    private int tagId;
    private String nodeType;
    private String queryTerms;
    private int queryNumber;


    /**
     * Result Node constructor
     *
     * @param label
     * @param tagId
     * @param nodeType
     * @param queryNumber
     */
    public ResultNode(String label, int tagId, String nodeType, int queryNumber) {
        this.label = label;
        this.tagId = tagId;
        this.nodeType = nodeType;
        this.queryNumber = queryNumber;
    }

    /**
     * Sets new queryNumber.
     *
     * @param queryNumber New value of queryNumber.
     */
    public void setQueryNumber(int queryNumber) {
        this.queryNumber = queryNumber;
    }

    /**
     * Sets new nodeType.
     *
     * @param nodeType New value of nodeType.
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * Gets queryTerms.
     *
     * @return Value of queryTerms.
     */
    public String getQueryTerms() {
        return queryTerms;
    }

    /**
     * Sets new label.
     *
     * @param label New value of label.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Gets nodeType.
     *
     * @return Value of nodeType.
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * Gets queryNumber.
     *
     * @return Value of queryNumber.
     */
    public int getQueryNumber() {
        return queryNumber;
    }

    /**
     * Gets tagId.
     *
     * @return Value of tagId.
     */
    public int getTagId() {
        return tagId;
    }

    /**
     * Gets label.
     *
     * @return Value of label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets new tagId.
     *
     * @param tagId New value of tagId.
     */
    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    /**
     * Sets new queryTerms.
     *
     * @param queryTerms New value of queryTerms.
     */
    public void setQueryTerms(String queryTerms) {
        this.queryTerms = queryTerms;
    }
}
