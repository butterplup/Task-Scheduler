package project1.algorithm;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

public class ThreadAnalytics {
    private static ThreadAnalytics instance;

    private final AtomicInteger threadsAlive = new AtomicInteger();
    @Getter private final int threadsNeeded;
    private final Queue<Scheduler> threadPool = new ConcurrentLinkedQueue<>();
    // The best complete schedule length thus far
    private int bestFinishTime = Integer.MAX_VALUE;
    @Getter private Schedule bestSchedule;

    private ThreadAnalytics(int threadsNeeded) {
        this.threadsNeeded = threadsNeeded;
    }

    public static ThreadAnalytics getInstance(int threads) {
        instance = new ThreadAnalytics(threads);
        return instance;
    }

    public static ThreadAnalytics getInstance() {
        return instance;
    }

    /* Threads call this when they have produced a
        complete schedule.
     */
    public synchronized void newSchedule(int time, Schedule s) {
        if (time < bestFinishTime) {
            bestFinishTime = time;
            bestSchedule = s;
        }
    }

    public int getGlobalBestTime() {
        return bestFinishTime;
    }

    public void addThread(Scheduler t) {
        this.threadPool.add(t);
        if (this.threadsAlive.incrementAndGet() > threadsNeeded) {
            System.out.println("WARNING: Processor overprovision!");
        }
        t.start();
    }

    public boolean threadNeeded() {
        return threadsAlive.get() < threadsNeeded;
    }

    public void decThreadsAlive() {
        this.threadsAlive.decrementAndGet();
    }

    public int numThreadsAlive() {
        return this.threadsAlive.get();
    }

    public void waitTillDone() throws InterruptedException {
        while (threadsAlive.get() > 0) {
            for (Thread t : threadPool) {
                t.join();
            }
        }
    }
}
