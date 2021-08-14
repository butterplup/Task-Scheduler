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
    private AtomicInteger bestFinishTime;

    private ThreadAnalytics(int threadsNeeded) {
        this.threadsNeeded = threadsNeeded;
    }

    public static ThreadAnalytics getInstance(int threads) {
        if (instance == null) {
            instance = new ThreadAnalytics(threads);
        }

        return instance;
    }

    /* Threads call this when they have produced what
        they believe to be a good schedule
     */
    public synchronized void newScheduleTime(int time) {
        if (bestFinishTime == null || time < bestFinishTime.get()) {
            if (bestFinishTime == null) {
                bestFinishTime = new AtomicInteger();
            }
            bestFinishTime.set(time);
        }
    }

    public boolean hasGlobalBestTime() {
        return bestFinishTime != null;
    }

    public int getGlobalBestTime() {
        return bestFinishTime.get();
    }

    public void addThread(Scheduler t) {
        this.threadPool.add(t);
        if (this.threadsAlive.incrementAndGet() > threadsNeeded) {
            System.out.println("WARNING: Processor overprovision!");
        }
        t.start();
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

    public Schedule getBestSchedule() {
        Schedule best = null;

        for (Scheduler t : threadPool) {
            // Get the schedule
            Schedule threadBest = t.getOutput();
            if (threadBest != null) {
                if (best == null || best.getFinishTime() > threadBest.getFinishTime()) {
                    best = threadBest;
                }
            }
        }

        // Nullify the instance so it doesn't clash with consequent runs
        instance = null;
        return best;
    }
}
