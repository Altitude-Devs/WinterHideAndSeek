package com.daki.main;

import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.Participant;
import com.daki.main.objects.TitleCreator;
import lombok.Setter;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;

public class Release {
    @Setter
    private boolean cancelled;
    private boolean started;

    public Release() {
        cancelled = false;
        started = false;
    }

    public void release() {
        System.out.println("releasing");
        if (!cancelled && !started) {
            started = true;
            Sounds.playSounds();

            for (Participant participant : EventManager.getExistingEvent().getParticipants()) {
                if (participant.getEventRole().equals(EventRole.SEEKER)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi warp SeekersStartWarp " + participant.getPlayer().getName() + " -s");
                }
            }

            Title title = TitleCreator.createTitle("SEEKERS RELEASED", "GOOD LUCK", 1, 3, 1);
            Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(title));
        }
        EventManager.getExistingEvent().getTimer().startTimer();
    }
}
