package com.daki.main.christmas;

import com.daki.main.WinterHideAndSeek;
import com.daki.main.event.events.EventEndEvent;
import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.EventTimer;
import com.daki.main.objects.Participant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;

public class TimerRunnable extends Thread {

    private final EventTimer eventTimer;
    private boolean cancelled;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    int lastRemaining = 0;

    public TimerRunnable(EventTimer eventTimer) {
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
        Bukkit.broadcast(MiniMessage.miniMessage().deserialize(message));
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("winterhideandseek.admin")) {
                Bukkit.getScheduler().runTask(WinterHideAndSeek.getInstance(), () ->
                        Bukkit.getPluginManager().callEvent(new EventEndEvent()));
                break;
            }
        }
    }

    private void lessThan15Min(Duration remainingTime) {
        int remainingMinutes = (int) remainingTime.toMinutes();
        if (remainingMinutes != 0 && remainingMinutes == lastRemaining) {
            return;
        }
        lastRemaining = remainingMinutes;
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

    private int lastRemainingQuarters = -1;
    public void run() {
        if (cancelled) {
            return;
        }

        Duration remainingDuration = eventTimer.getRemainingTime();
        int remainingTime = (int) remainingDuration.toSeconds();
        int remainingQuarterHours = remainingTime / 900; // 900 sec is 15 min
        int remainingTimeInSeconds = remainingTime % 900;
        if (lastRemainingQuarters == remainingQuarterHours || -3 >= remainingTimeInSeconds || 3 <= remainingTimeInSeconds) {
            if (remainingQuarterHours == 0) {
                lessThan15Min(remainingDuration);
            }
            return;
        } // If this isn't the same remainingQuarterHours as last remainingQuarterHours and remainingTimeInSeconds is between -3 and 3

        lastRemainingQuarters = remainingQuarterHours;

        if (remainingQuarterHours > 4) {
            int z = remainingQuarterHours % 4;
            if (z * 4 == remainingQuarterHours) {
                if (z == 1) {
                    sendMessage(z + " hours remaining!");
                } else {
                    sendMessage("1 hour remaining!");
                }
            }
        } else if (remainingQuarterHours > 0) {
            sendMessage(remainingQuarterHours * 15 + " minutes remaining!");
        }
    }

    private void sendMessage(String title) {
        Title timedTitle = Title.title(miniMessage.deserialize(title), Component.empty(),
                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)));
        for (Participant p : EventManager.getExistingEvent().getParticipants()) {
            p.getPlayer().showTitle(timedTitle);
        }
    }

    public void stopTimer() {
        cancelled = true;
    }

}
