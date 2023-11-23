package com.daki.main.event.listeners;

import com.daki.main.Sounds;
import com.daki.main.event.events.EventEndEvent;
import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.Event;
import com.daki.main.objects.Participant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class EventEndEventListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEventEnd(EventEndEvent event) {

        for (Participant participant : EventManager.getExistingEvent().getParticipants()) {
            if (!participant.getEventRole().equals(EventRole.SEEKER)) {
                continue;
            }
            participant.getPlayer().getInventory().clear();
        }

        Sounds.playSounds();

        for (Player player : Bukkit.getOnlinePlayers()) {

            player.sendTitle(ChatColor.WHITE + "This round is now over!", "", 20, 100, 20);

        }


        Event existingEvent = EventManager.getExistingEvent();
        existingEvent.clearParticipants();
        existingEvent.setRunning(false);
        existingEvent.getRelease().setCancelled(false);
        existingEvent.getTimer().stopTimer();

        for (Player player : Bukkit.getOnlinePlayers()){
            existingEvent.addParticipant(new Participant(player, EventRole.HIDER));
            player.sendTitle(ChatColor.DARK_GREEN + "JOINED EVENT",
                    ChatColor.GREEN + "You were reentered for the next round!",
                    20, 60, 20 );
        }

        Bukkit.broadcast(ChatColor.RED + "Event shut down!", "winterhideandseek.admin");


    }

}
