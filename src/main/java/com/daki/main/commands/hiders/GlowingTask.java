package com.daki.main.commands.hiders;

import com.daki.main.WinterHideAndSeek;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class GlowingTask {
    private final List<Player> hiders;
    private final long everyXseconds;
    private final long forYseconds;
    private int currentIndex = 0;

    public GlowingTask(List<Player> hiders, long everyXseconds, long forYseconds) {
        this.hiders = hiders;
        this.everyXseconds = everyXseconds;
        this.forYseconds = forYseconds;
    }

    public void start() {
        scheduleNext();
    }

    private void scheduleNext() {
        if (currentIndex >= hiders.size()) {
            return;
        }

        Player player = hiders.get(currentIndex);

        Bukkit.getScheduler().runTask(WinterHideAndSeek.getInstance(), () -> {
            player.setGlowing(true);

            Bukkit.getScheduler().runTaskLater(WinterHideAndSeek.getInstance(), () -> player.setGlowing(false), forYseconds * 20);

            currentIndex++;
            Bukkit.getScheduler().runTaskLater(WinterHideAndSeek.getInstance(), this::scheduleNext, everyXseconds * 20);
        });
    }
}