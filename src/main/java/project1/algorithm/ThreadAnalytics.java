package project1.algorithm;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

/**
 * A ThreadAnalytics singleton is used to manage a thread pool
 * for the DFS algorithm, and keeps track of the best Schedule.
 */
public class ThreadAnalytics {
    private static ThreadAnalytics instance;

    // Track the current number of threads running
    private final AtomicInteger threadsAlive = new AtomicInteger();
    // Track the number of thread starts over the lifetime of this object
    private final AtomicInteger threadsSpawned = new AtomicInteger();
    @Getter private final int threadsNeeded;
    @Getter private final Queue<DFSThread> threadPool = new ConcurrentLinkedQueue<>();
    // The best complete schedule length thus far
    private int bestFinishTime = Integer.MAX_VALUE;
    @Getter private Schedule bestSchedule;

    /**
     * Constructor to create a single instance of ThreadAnalytics.
     * @param threadsNeeded Number of threads required, to be running in parallel.
     */
    private ThreadAnalytics(int threadsNeeded) {
        this.threadsNeeded = threadsNeeded;
    }

    /**
     * Instantiate an instance of ThreadAnalytics, and return it.
     * @param threads Number of threads required, to be running in parallel.
     * @return The single ThreadAnalytics instance.
     */
    public static ThreadAnalytics getInstance(int threads) {
        instance = new ThreadAnalytics(threads);
        return instance;
    }

    /**
     * Return the single ThreadAnalytics instance.
     * @return The single ThreadAnalytics instance.
     */
    public static ThreadAnalytics getInstance() {
        return instance;
    }

    /**
     * Updates the best finish time and its schedule.
     * Called by threads after they have produced a complete schedule, every time.
     * @param time Time for the produced schedule to run.
     * @param s The produced schedule.
     */
    public synchronized void newSchedule(int time, Schedule s) {
        if (time < bestFinishTime) {
            bestFinishTime = time;
            bestSchedule = s;
        }
    }

    /**
     * Returns the time of the best schedule.
     * @return The time taken to run the best schedule.
     */
    public int getGlobalBestTime() {
        return bestFinishTime;
    }

    /**
     * Add a Scheduler thread to the thread pool, and run it.
     * @param t The Scheduler thread to be run.
     */
    public void addThread(DFSThread t) {
        this.threadPool.add(t);
        if (this.threadsAlive.incrementAndGet() > threadsNeeded) {
            System.out.println("WARNING: Processor overprovision!");
        }
        t.start();
        this.threadsSpawned.incrementAndGet();
    }

    /**
     * Determine whether or not enough threads are currently running.
     * @return If the number of current threads running is lesser than required.
     */
    public boolean threadNeeded() {
        return threadsAlive.get() < threadsNeeded;
    }

    /**
     * Decrement the number of threads running.
     */
    public void decThreadsAlive() {
        this.threadsAlive.decrementAndGet();
    }

    /**
     * Get the current number of threads running.
     * @return Number of threads running (alive).
     */
    public int numThreadsAlive() {
        return this.threadsAlive.get();
    }

    /**
     * Get the total number of threads that have been created over the
     * course of the algorithm.
     * @return Total number of threads created.
     */
    public int numThreadsSpawned() {
        return this.threadsSpawned.get();
    }

    /**
     * Wait for all threads to die.
     * @throws InterruptedException
     */
    public void waitTillDone() throws InterruptedException {
        while (threadsAlive.get() > 0) {
            for (Thread t : threadPool) {
                t.join();
            }
        }
    }
}
