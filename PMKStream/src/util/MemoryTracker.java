package util;

import com.javamex.classmexer.MemoryUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Evandrino Barros, Jônatas Tonholo
 * @version 2.0
 */
public class MemoryTracker {
    private Map<String, Long> taskMap = null;
    private int numberOfMemoryUsageChecks = 0;

    /**
     * Constructor
     */
    public MemoryTracker() {
        taskMap = new HashMap<String, Long>();
    }

    /**
     * Accumulate the Memory Usage
     * @param task
     * @param newValue
     */
    public void accumulateMemoryUsage (String task, long newValue) {
        long currentValue = this.getMemoryUsage(task);
        currentValue += newValue;
        this.setUsedMemoryInBytes(task, currentValue);
    }

    /**
     * Check Memory of the Task
     * @param task
     * @return
     */
    public long checkMemory(String task) {
        long memoryUsage = ((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())); //in bytes
        this.taskMap.put(task, memoryUsage);
        return memoryUsage;
    }

    /**
     * <b>Check Deep Memory Usage of the object</b><br>
     *
     * Returns an estimation, in bytes, of the memory usage of the given objects plus
     * (recursively) objects referenced via non-static references from any of those objects via non-public fields.
     *
     * @param obj
     * @param task
     * @return
     */
    public long checkDeepMemoryUsageOf( Object obj, String task){
        long memSize = MemoryUtil.deepMemoryUsageOf(obj, MemoryUtil.VisibilityFilter.ALL);
        taskMap.put(task, memSize);
        return memSize;
    }

    /**
     * Check Deep Memory Usage of the object WithoutSaving
     * @param obj
     * @return
     */
    public long checkDeepMemoryUsageOfWithoutSaving( Object obj){
        return MemoryUtil.deepMemoryUsageOf(obj, MemoryUtil.VisibilityFilter.ALL);
    }

    /**
     * Check Deep Memory Usage of the Task with Garbage Collector
     * @param task
     * @return
     */
    public long checkMemoryWithGC(String task) {
        System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();
        System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();
        System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();System.gc();
        long memory_usage = ((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())); //in bytes
        taskMap.put(task, memory_usage);
        return memory_usage;
    }

    /**
     * Gets the Memory Usage in Bytes
     * @param task
     * @return
     */
    public final long getMemoryUsage(String task) {
        return taskMap.get(task);
    }

    /**
     * Gets the Memory Usage in KiloBytes
     * @param task
     * @return
     */
    public final long getMemoryUsageInKB(String task) {
        return (taskMap.get(task)/1024);
    }

    /**
     * Gets the Memory Usage in MegaBytes
     * @param task
     * @return
     */
    public final long getMemoryUsageInMB(String task) {
        return ((taskMap.get(task)/1024)/1024);
    }

    /**
     * Gets the Memory Usage in GigaBytes
     * @param task
     * @return
     */
    public final long getMemoryUsageInGB(String task) {
        return getMemoryUsageInMB(task)/1024;
    }

    /**
     * Set the Used Memory of the Task In Bytes
     * @param task
     * @param bytes
     */
    public void setUsedMemoryInBytes(String task, long bytes) {
        this.taskMap.put(task, bytes);
    }

    /**
     * Set the Used Memory of the Task In Mega Bytes
     * @param task
     * @param mbytes
     */
    public void setUsedMemoryInMB(String task, long mbytes) {
        this.taskMap.put(task, (mbytes * 1024) * 1024);
    }


    /**
     * The tester Main
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
