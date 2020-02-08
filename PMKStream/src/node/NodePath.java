package node;

import java.util.ArrayList;

/**
 * @version 2.0
 * @author Evandrino Barros, Jônatas Tonholo
 * FIXME: Verificar a performance com ArrayList, caso seja pior, retornar para implementação original
 */
public class NodePath {
    private ArrayList<String> path;
    private ArrayList<Integer> tagIdList;
    private int lastPosition = -1; // empty

    /**
     * Default Constuctor
     */
    public NodePath () {
        this.path = new ArrayList<String>();
        this.tagIdList = new ArrayList<Integer>();
    }

    /**
     * Add the Node Path
     * @param label
     * @param tagId
     */
    public void addNode (String label, Integer tagId) {
        lastPosition++;
        this.path.add(lastPosition,label);
        this.tagIdList.add(lastPosition,tagId);
    }

    /**
     * Remove the Node Path
     */
    public void removeNode () {
        if(lastPosition > -1) {
            this.lastPosition--;
        }
    }

    /**
     * Print the Node Path
     */
    public void print() {
        int i=0;
        for(String s : path) {
            System.out.print(s + "(" + i++ + ").");
        }
        System.out.println();
    }

    /**
     * Get The Label in the last position
     * @return
     */
    public String getLabel () {
        return this.path.get(this.lastPosition);
    }

    /**
     * Get The Label in the position
     * @param position
     * @return
     */
    public String getLabel (int position) {
        return this.path.get(position);
    }

    /**
     * Return the Tag ID in the last position
     * @return
     */
    public Integer getTagId () {
        return this.tagIdList.get(this.lastPosition);
    }

    /**
     * Return the Tag ID in the position
     * @return
     */
    public Integer getTagId (int position) {
        return this.tagIdList.get(position);
    }

    /**
     * Get the Last Position of the Node Path
     * @return
     */
    public int getLastPosition() {
        return lastPosition;
    }
}
