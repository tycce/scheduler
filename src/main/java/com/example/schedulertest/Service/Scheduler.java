package com.example.schedulertest.Service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


@Service
public class Scheduler extends Thread{

    private final List<LocalDateTime> localDateTimes = Collections.synchronizedList(new ArrayList<>());
    private long currentSleepTime = 1_000;

    public Scheduler(){
        localDateTimes.add(LocalDateTime.now().plusMinutes(10));
        start();
    }

    public List<LocalDateTime> getLocalDateTimes(){return localDateTimes;}

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(currentSleepTime);
            } catch (InterruptedException e) {
                System.out.println("поток был прерван");
            } finally {
                System.out.println("localDateTimes: " + localDateTimes);

                LocalDateTime current = LocalDateTime.now();
                localDateTimes.stream().filter(ldt -> ldt.isBefore(current)).peek(this::remove);

                setCurrentSleepTime();
                System.out.println("currentSleepTime: " + (currentSleepTime / 1000.0 / 60) + " min");
            }

        }
    }

    private void setCurrentSleepTime() {
        this.currentSleepTime = localDateTimes.isEmpty() ? 1000 * 60 * 60 * 24 :
                this.localDateTimes.stream().mapToLong(this::convertFromLDTtoLong).min().getAsLong() - System.currentTimeMillis();
    }

    private LocalDateTime remove(LocalDateTime time) {
        localDateTimes.remove(time);
        System.out.println("delete time: " + time);
        return time;
    }

    public static LocalDateTime convertFromLongToLDT(long timeMilli){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMilli), ZoneId.systemDefault());
    }

    public long convertFromLDTtoLong(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }


    private static class TestRun{
        private final LocalDateTime startDate = null;
        private final LocalDateTime endDate = null;
    }

}

