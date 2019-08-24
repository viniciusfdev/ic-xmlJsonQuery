/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vinicius
 */
public class Work implements Runnable {

    public static final double max = Double.MAX_VALUE/10e298;
    public int count;
    public int nProcessors;
    public int threadID;

    public Work(int threadID, int nProcessors) {
        this.threadID = threadID;
        this.nProcessors = nProcessors;
        this.count = 0;
    }

    public void run() {
        try {
            System.out.println(Thread.currentThread().getId() + " is running");
            for (int i = (int) ((this.max/nProcessors)*(this.threadID)); i < (this.max/this.nProcessors)*(this.threadID+1) ; i++) {
                
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean countWork() {
        return true;
    }

}
