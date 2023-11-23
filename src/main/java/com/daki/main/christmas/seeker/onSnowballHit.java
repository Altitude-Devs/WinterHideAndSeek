package com.daki.main.christmas.seeker;

import com.daki.main.WinterHideAndSeek;
import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.Event;
import com.daki.main.objects.Participant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class onSnowballHit implements Listener {

    @EventHandler
    public void onSnowballHitHider(ProjectileHitEvent e) {
        if (!EventManager.getExistingEvent().getRunning()){
            return;
        }
        if (!e.getEntity().getType().equals(EntityType.SNOWBALL)) {
            return;
        }
        if (!(e.getHitEntity() instanceof Player receiver)) {
            return;
        }
        Participant participantFromName = EventManager.getExistingEvent().getParticipantFromPlayerName(receiver.getName());
        if (participantFromName == null || !participantFromName.getEventRole().equals(EventRole.HIDER)) {
            return;
        }
        receiver.setHealth(0);
        Player sender = (Player) e.getEntity().getShooter();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "lp user " + receiver.getName() + " permission unset christmas.hider");
        if (GV.SnowballChatCoolDown.containsKey(receiver)) {
            return;
        }

        int hiders = 0;
        Event existingEvent = EventManager.getExistingEvent();
        existingEvent.removeParticipant(existingEvent.getParticipantFromPlayerName(receiver.getName()));

        for (Participant participant : existingEvent.getParticipants()){
            if (participant.getEventRole().equals(EventRole.HIDER)){
                hiders++;
            }
        }
        if (hiders == 1) {
            Bukkit.getServer().broadcastMessage(ChatColor.RED + sender.getName() + " has found "
                    + receiver.getName() + ". " + hiders + " hider remaining.");
            GV.SnowballChatCoolDown.put(receiver, true);
        } else if (hiders != 1) {
            Bukkit.getServer().broadcastMessage(ChatColor.RED + sender.getName() + " has found "
                    + receiver.getName() + ". " + hiders + " hiders remaining.");
            GV.SnowballChatCoolDown.put(receiver, true);
        }

        new BukkitRunnable() {
            public void run() {
                GV.SnowballChatCoolDown.remove(receiver);
            }
        }.runTaskLater(WinterHideAndSeek.getInstance(), 100);
    }
}
