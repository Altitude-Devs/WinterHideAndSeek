package com.daki.main.christmas.seeker;

import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class onSnowballThrow implements Listener {

    @EventHandler
    public void onThrow(ProjectileLaunchEvent e) {
        if (!(e.getEntity() instanceof Snowball)) {
            return;
        }
        Player player = (Player) e.getEntity().getShooter();
        if (player == null || !EventManager.getExistingEvent().getParticipantFromPlayerName(player.getName()).getEventRole().equals(EventRole.SEEKER)) {
            return;
        }
        if (player.getInventory().getItemInMainHand().getAmount() < 16) {
            player.getInventory().getItemInMainHand().setAmount(16);
            player.updateInventory();
        }
    }
}
