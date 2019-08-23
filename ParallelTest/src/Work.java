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

    public static final double min = Double.MIN_VALUE;
    public static final double max = Double.MAX_VALUE;
    public int threadID;

    public Work(int threadID) {
        this.threadID = threadID;
    }

    public void run() {
        try {
            System.out.println(Thread.currentThread().getId() + " is running");
            for (int i = 0; i < this.max ; i+=0.1) {
                
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean countWork() {
        return true;
    }

}
