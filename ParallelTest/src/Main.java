
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vinicius
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int nProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println(nProcessors);
        long startTime = System.nanoTime();
        int i;
        
        for (i = 0; i < nProcessors; i++) {
            Thread t = new Thread((new Work(i)), "Thread" + i);
            t.start();
            if(i == (nProcessors - 1)){
                try {
                    t.join();
                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
                    System.out.println("\nTime in milliseconds: " + duration);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }

}
