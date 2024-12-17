package com.daki.main.christmas.seeker;

import com.daki.main.WinterHideAndSeek;
import com.daki.main.christmas.seeker.items.SeekerItems;
import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class onAbilityUse implements Listener {

    @EventHandler
    public void onSpeedUse(PlayerInteractEvent e) {

        Player player = e.getPlayer();
        if (!player.getInventory().getItemInMainHand().equals(SeekerItems.speed())) {
            if (player.getInventory().getItemInMainHand().equals(SeekerItems.speedCoolDown())) {
                player.sendRichMessage("<red>Sugar Rush is on a cooldown!</red>");
            }
            return;
        }
        if ((!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) ||
                !EventManager.getExistingEvent().getParticipantFromPlayerName(player.getName()).getEventRole().equals(EventRole.SEEKER)) {
            return;
        }
        player.getInventory().remove(SeekerItems.speed());
        player.getInventory().addItem(SeekerItems.speedCoolDown());
        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 200, 1);
        player.addPotionEffect(speed);
        new BukkitRunnable() {
            public void run() {
                player.getInventory().remove(SeekerItems.speedCoolDown());
                player.getInventory().addItem(SeekerItems.speed());
                player.sendRichMessage("<green>Sugar Rush is ready to be used again!</green>");
            }
        }.runTaskLater(WinterHideAndSeek.getInstance(), 600);
    }

}
