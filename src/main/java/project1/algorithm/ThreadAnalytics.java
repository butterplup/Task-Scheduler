package project1.algorithm;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import lombok.Getter;
import lombok.Setter;

public class ThreadAnalytics {
    private static ThreadAnalytics instance;

    private AtomicInteger threadsAlive = new AtomicInteger();
    @Getter private final int threadsNeeded;
    private Queue<Scheduler> threadPool = new ConcurrentLinkedQueue<>();

    private ThreadAnalytics(int threadsNeeded) {
        this.threadsNeeded = threadsNeeded;
    }

    public static ThreadAnalytics getInstance(int threads) {
        if (instance == null) {
            instance = new ThreadAnalytics(threads);
        }

        return instance;
    }

    public void addThread(Scheduler t) {
        this.threadPool.add(t);
        this.threadsAlive.incrementAndGet();
        System.out.printf("%d threads %n", threadsAlive.get());
    }

    public void decThreadsAlive() {
        if (this.threadsAlive.decrementAndGet() == 0) {
            System.out.println("Notifying...");
            synchronized (instance) {
                instance.notifyAll();
            }
        }
        System.out.printf("%d threads %n", threadsAlive.get());
    }

    public int numThreadsAlive() {
        return this.threadsAlive.get();
    }

    public Schedule getBestSchedule() {
        Schedule best = null;

        for (Scheduler t : threadPool) {
            try {
                // Wait until done
                t.join();
                // Get the schedule
                Schedule threadBest = t.getOutput();
                if (best == null || best.getFinishTime() > threadBest.getFinishTime()) {
                    best = threadBest;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return best;
    }
}
