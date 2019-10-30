/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

import java.util.HashMap;

/**
 *
 * @author vinicius
 */
public class TermOcurrence {
    private int nOcurrences;
    private HashMap<String, Boolean> termOcurrences;
    
    public TermOcurrence() {
        nOcurrences = 0;
        this.termOcurrences = new HashMap<String, Boolean>();
    }
    
    public TermOcurrence(String term) {
        this.nOcurrences = 0;
        this.nOcurrences++;
        this.termOcurrences = new HashMap<String, Boolean>();
        this.termOcurrences.put(term, Boolean.TRUE);
    }
    
    public void setOcurrence(String term){
        this.termOcurrences.put(term, Boolean.TRUE);
        this.nOcurrences++;
    }
    
    public HashMap<String, Boolean> getTermOcurrences() {
        return termOcurrences;
    }

    public int getnOcurrences() {
        return nOcurrences;
    }
}
