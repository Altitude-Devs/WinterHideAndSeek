package com.daki.main.christmas;

import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Participant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ParticipantLeaveEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event){
        Participant participant = EventManager.getExistingEvent().getParticipantFromPlayerName(event.getPlayer().getName());
        EventManager.getExistingEvent().removeParticipant(participant);
    }

}
