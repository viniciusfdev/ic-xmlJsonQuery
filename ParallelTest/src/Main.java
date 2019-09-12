
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
        
        //System.out.println("With Multithreading: "+WithMultithreading());
        //System.out.println("");
        //System.out.println("No Multithreading: "+normalProcess());
        
        String s = "dsajikd       sss dddff rrrr                k";
        
        for(String a: s.split("\\s+")){
            
            System.out.println(":"+a);
        }

    }
    
    public static long WithMultithreading(){
        long []times = new long[10];
        int nProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println(nProcessors);
        long startTime = System.nanoTime();
        int i, j;
        
        for(j = 0; j < 10 ; j++){
            for (i = 0; i < nProcessors; i++) {
                Thread t = new Thread((new Work(i, nProcessors)), "Thread" + i);
                t.start();
                if(i == (nProcessors - 1)){
                    try {
                        t.join();
                        long endTime = System.nanoTime();
                        long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
                        times[j] = duration;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        long timeAverage = 0;
        for(long t: times){
            timeAverage += t;
        }
        return (timeAverage/10);
    }
    
    public static long normalProcess(){
        long []times = new long[10];
        int nProcessors = Runtime.getRuntime().availableProcessors();
        long startTime = System.nanoTime();
        int i, j;
        
        for(j = 0; j < 10 ; j++){
            Work w = new Work(0, 1);
            w.run();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
            times[j] = duration;
        }
        long timeAverage = 0;
        for(long t: times){
            timeAverage += t;
        }
        return (timeAverage/10);
    }
}
