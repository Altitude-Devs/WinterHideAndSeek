package com.daki.main.objects;

import com.daki.main.christmas.TimerRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EventTimer {
    private final Instant startTime;
    private final Instant endTime;
    private TimerRunnable tr;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> currentTask;

    public EventTimer(Duration duration){
        startTime = Instant.now();
        endTime = startTime.plus(duration);
    }

    public Duration getRemainingTime(){
        Duration remainingTime = Duration.between(Instant.now(), endTime);
        return remainingTime.isNegative() ? Duration.ZERO : remainingTime;
    }

    public void startTimer(){
        if (tr != null){
            tr.stopTimer();
        }
        tr = new TimerRunnable(this);
        currentTask = scheduler.scheduleAtFixedRate(tr, 1, 1, TimeUnit.SECONDS);
    }

    public void stopTimer(){
        if (tr != null){
            tr.stopTimer();
            currentTask.cancel(true);
        }
    }
}
