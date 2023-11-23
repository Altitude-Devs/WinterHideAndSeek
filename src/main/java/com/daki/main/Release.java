package com.daki.main;

import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.Participant;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Release {
    private boolean cancelled;
    private boolean started;

    public Release() {
        cancelled = false;
        started = false;
    }

    public void setCancelled(boolean cancelled){
        this.cancelled = cancelled;
    }

    public void release(){
        System.out.println("releasing");
        if (!cancelled && !started) {
            started = true;
            Sounds.playSounds();

            for (Participant participant : EventManager.getExistingEvent().getParticipants()) {
                if (participant.getEventRole().equals(EventRole.SEEKER)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi warp SeekersStartWarp " + participant.getPlayer().getName() + " -s");
                }
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle("SEEKERS RELEASED", "GOOD LUCK", 10, 60, 10);
            }
        }
        EventManager.getExistingEvent().getTimer().startTimer();
    }
}
