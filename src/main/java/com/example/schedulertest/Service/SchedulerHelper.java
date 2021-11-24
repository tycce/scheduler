package com.example.schedulertest.Service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class SchedulerHelper implements Runnable {

    private final Scheduler scheduler;

    public SchedulerHelper(Scheduler scheduler) {
        this.scheduler = scheduler;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(15_000);
                LocalDateTime newDate = LocalDateTime.now().plusMinutes(1).plusMinutes(new Random().nextInt(5));
                System.out.println("newDate: " + newDate);
                scheduler.getLocalDateTimes().add(newDate);
                scheduler.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
