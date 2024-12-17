package com.daki.main.event.listeners;

import com.daki.main.Sounds;
import com.daki.main.event.events.EventEndEvent;
import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.Event;
import com.daki.main.objects.Participant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.time.Duration;

public class EventEndEventListener implements Listener {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEventEnd(EventEndEvent event) {

        for (Participant participant : EventManager.getExistingEvent().getParticipants()) {
            if (!participant.getEventRole().equals(EventRole.SEEKER)) {
                continue;
            }
            participant.getPlayer().getInventory().clear();
        }

        Sounds.playSounds();

        Title endRoundTitle = Title.title(miniMessage.deserialize("<white>This round is now over!</white>"), Component.empty(),
                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(5), Duration.ofSeconds(1)));
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(endRoundTitle);
        }


        Event existingEvent = EventManager.getExistingEvent();
        existingEvent.clearParticipants();
        existingEvent.setRunning(false);
        existingEvent.getRelease().setCancelled(false);
        existingEvent.getTimer().stopTimer();

        Title newRoundTitle = Title.title(miniMessage.deserialize("<dark_green>JOINED EVENT</dark_green>"),
                miniMessage.deserialize("<green>You were re-entered into the next round!</green>"),
                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)));
        for (Player player : Bukkit.getOnlinePlayers()){
            existingEvent.addParticipant(new Participant(player, EventRole.HIDER));
            player.showTitle(newRoundTitle);
        }

        Bukkit.broadcast(miniMessage.deserialize("<red>Event shut down!</red>"), "winterhideandseek.admin");
    }

}
