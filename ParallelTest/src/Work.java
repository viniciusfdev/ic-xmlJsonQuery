/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vinicius
 */
public class Work extends Thread{
    public static final int min = Integer.MIN_VALUE;
    public static final int max = Integer.MAX_VALUE;
    
    
    public void run(){
        long startTime = System.nanoTime();
        this.countWork();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.
        
        System.out.println("\nTime in milliseconds: "+duration);
    }
    
    public boolean countWork(){
        for(int i = 0; i < this.max/1000 ; i += 100){
            System.out.println(Thread.currentThread());
        }
        
        return true;
    }
    
}
