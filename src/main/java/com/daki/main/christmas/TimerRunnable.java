package com.daki.main.christmas;

import com.daki.main.WinterHideAndSeek;
import com.daki.main.event.events.EventEndEvent;
import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.EventTimer;
import com.daki.main.objects.Participant;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;

public class TimerRunnable extends Thread {

    EventTimer eventTimer;
    private boolean cancelled;

    public TimerRunnable(EventTimer eventTimer){
        this.eventTimer = eventTimer;
        cancelled = false;
    }

    private void endEvent() {
        if (!EventManager.getExistingEvent().getRunning()) {
            WinterHideAndSeek.getInstance().getLogger().warning("Timer attempted to close the event while it wasn't running");
            return;
        }
        List<String> winnerNames = EventManager.getExistingEvent().getParticipants().stream()
                .filter(participant -> participant.getEventRole().equals(EventRole.SEEKER))
                .map(Participant::getPlayer).map(Player::getName)
                .toList();
        String message = "The winners are: " + String.join(", ", winnerNames) + "!";
        Bukkit.broadcastMessage(message);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("winterhideandseek.admin")) {
                Bukkit.getScheduler().runTask(WinterHideAndSeek.getInstance(), () -> {
                    Bukkit.getPluginManager().callEvent(new EventEndEvent());
                });
                break;
            }
        }
    }

    private void lessThan15Min(Duration remainingTime) {
        int remainingMinutes = (int) remainingTime.toMinutes();
        if (remainingMinutes != 0) {
            switch (remainingMinutes) {
                case 10 -> sendMessage("10 minutes remaining!");
                case 5 -> sendMessage("5 minutes remaining!");
                case 2 -> sendMessage("2 minutes remaining!");
                case 1 -> sendMessage("1 minute remaining!");
            }
            return;
        }
        switch (remainingTime.toSecondsPart()) {
            case 30 -> sendMessage("30 seconds remaining");
            case 15 -> sendMessage("15 seconds remaining");
            case 10 -> sendMessage("10 seconds remaining");
            case 5 -> sendMessage("5 seconds remaining");
            case 4 -> sendMessage("4 seconds remaining");
            case 3 -> sendMessage("3 seconds remaining");
            case 2 -> sendMessage("2 seconds remaining");
            case 1 -> sendMessage("1 seconds remaining");
            case 0 -> {
                endEvent();
                cancelled = true;
            }
        }
    }

    public void run(){
        int lastX = -1;
        while (!cancelled){
            Duration remainingDuration = eventTimer.getRemainingTime();
            int remainingTime = (int) remainingDuration.toSeconds();
            int x = remainingTime / 900; // 900 sec is 15 min
            int y = remainingTime % 900;
            if (lastX != x && -3 < y && 3 > y){ // If this isn't the same x as last x and y is between -3 and 3
                lastX = x;

                if (x > 4){
                    int z = x % 4;
                    if (z * 4 == x){
                        if (z == 1){
                            sendMessage(z + " hours remaining!");
                        } else {
                            sendMessage("1 hour remaining!");
                        }
                    }
                } else if (x > 0) {
                    sendMessage(x * 15 + " minutes remaining!");
                }

            } else if (x == 0){
                lessThan15Min(remainingDuration);
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void sendMessage(String title){
        for (Participant p : EventManager.getExistingEvent().getParticipants()){
            p.getPlayer().sendTitle(title, "", 20, 60 , 20);
        }
    }

    public void stopTimer(){
        cancelled = true;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Closing object");
        super.finalize();
    }
}
