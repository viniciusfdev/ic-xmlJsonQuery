/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

/**
 *
 * @author vinicius
 */
public class Term {
    private String term;
    private boolean status;

    public Term(String term) {
        this.term = term;
        this.status = false;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean matched) {
        this.status = matched;
    }
    
    
}
