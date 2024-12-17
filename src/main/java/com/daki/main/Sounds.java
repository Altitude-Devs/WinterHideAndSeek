package com.daki.main;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class Sounds {

    public static void playSounds() {
        Bukkit.getScheduler().runTaskLater(WinterHideAndSeek.getInstance(),
                () -> Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 0.75F)),
                0);

        Bukkit.getScheduler().runTaskLater(WinterHideAndSeek.getInstance(),
                () -> Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 0.87F)),
                3);

        Bukkit.getScheduler().runTaskLater(WinterHideAndSeek.getInstance(),
                () -> Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F)),
                6);
    }

}
