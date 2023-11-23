package com.daki.main.objects;

import com.daki.main.christmas.TimerRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class EventTimer {
    private Instant startTime;
    private Instant endTime;
    private TimerRunnable tr;

    public EventTimer(Duration duration){
        startTime = Instant.now();
        endTime = startTime.plus(duration);
    }

    public Duration getRemainingTime(){
        Duration remainingTime = Duration.between(startTime, endTime);
        return remainingTime.isNegative() ? Duration.ZERO : remainingTime;
    }

    public void startTimer(){
        if (tr != null){
            tr.stopTimer();
        }
        tr = new TimerRunnable(this);
        tr.start();
    }

    public void stopTimer(){
        if (tr != null){
            tr.stopTimer();
        }
    }
}
