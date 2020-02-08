/**
 * This class helps tracking time in your code without change it too much. The classes keeps track of different tasks.
 * Tasks should be tracked INDIVIDUALLY and in a sequential way, never in parallel.
 */
package util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Felipe, Evandrino Barros, Jônatas Tonholo
 * @version 2.0
 */
public class TimeTracker {
    private Map<String, Long> taskMap = null;
    private Map<String, Long> accumulated = null;

    /**
     * Constructor
     */
    public TimeTracker () {
        this.taskMap = new HashMap<String, Long>(1);
    }

    /**
     * Starts counting time
     */
    public void start (String task) {
        this.taskMap.put(task, System.nanoTime());
    }

    /**
     * Start Accumulated
     * @param task
     */
    public void startAccumulated (String task) {
        Long accumulatedTime = new Long(0);
        if (this.accumulated == null) {
            this.accumulated = new HashMap<String, Long>(1);
        }
        accumulatedTime = this.accumulated.get(task);
        accumulatedTime = accumulatedTime == null ? 0 : accumulatedTime;
        this.taskMap.put(task, System.nanoTime() - accumulatedTime);
    }

    /**
     * Stops counting time and returns elapsed time
     * @param task
     * @return elapsed_time in nanoseconds
     */
    public long end(String task) {
        long elapsedTime = (System.nanoTime() - this.taskMap.get(task));
        this.taskMap.put(task, elapsedTime);
        return elapsedTime;
    }

    /**
     * Gets the Elapsed Time
     * @param task
     * @return
     */
    public double getElapsedTime(String task) {
        return taskMap.get(task);
    }

    /**
     * Gets The Elapsed MilliTime
     * @param task
     * @return
     */
    public final double getElapsedMilliTime(String task) {
        return (taskMap.get(task)/1000)/1000;
    }

    /**
     * Gets The Elapsed Time
     * @param task
     * @param time
     * @return
     */
    public final double getElapsedTime(String task, final TimeUnit time) {
        long elapsed_time = taskMap.get(task);
        if (time == TimeUnit.SECONDS) {
            return ((elapsed_time/1000.0)/1000.0)/1000.0;
        }
        if (time == TimeUnit.MILLISECONDS) {
            return ((elapsed_time/1000.0)/1000.0);
        }
        if (time == TimeUnit.MICROSECONDS) {
            return elapsed_time/1000.0;
        }
        return elapsed_time; //else return nanoseconds
    }

    /**
     * The Tester Main
     * @param args
     */
    public static void main(String[] args) {
		TimeTracker x = new TimeTracker();
		x.start("test");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		x.end("test");
		System.out.println("In nano:"+x.getElapsedTime("test"));
		System.out.println("In micro:"+x.getElapsedTime("test", TimeUnit.MICROSECONDS));
		System.out.println("In milli:"+x.getElapsedTime("test", TimeUnit.MILLISECONDS));
		System.out.println("In milli:"+x.getElapsedMilliTime("test"));
    }
}
