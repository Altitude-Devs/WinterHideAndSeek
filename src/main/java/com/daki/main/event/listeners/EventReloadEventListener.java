package com.daki.main.event.listeners;

import com.daki.main.christmas.seeker.items.SeekerItems;
import com.daki.main.event.events.EventReloadEvent;
import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.Participant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class EventReloadEventListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEventReload(EventReloadEvent event) {

        for (Participant participant : EventManager.getExistingEvent().getParticipants()) {
            if (!participant.getEventRole().equals(EventRole.SEEKER)) {
                continue;
            }
            participant.getPlayer().getInventory().clear();
            participant.getPlayer().getInventory().addItem(SeekerItems.snowballs());
            participant.getPlayer().getInventory().addItem(SeekerItems.speed());
            participant.getPlayer().getInventory().setHelmet(SeekerItems.bed());
            participant.getPlayer().updateInventory();
        }

        Bukkit.broadcast(ChatColor.GREEN + "Event reloaded!", "winterhideandseek.admin");

    }

}
