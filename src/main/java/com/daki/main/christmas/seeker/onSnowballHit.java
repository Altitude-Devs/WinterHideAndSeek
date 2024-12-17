package com.daki.main.christmas.seeker;

import com.daki.main.WinterHideAndSeek;
import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.Event;
import com.daki.main.objects.Participant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class onSnowballHit implements Listener {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @EventHandler
    public void onSnowballHitHider(ProjectileHitEvent e) {
        if (!EventManager.getExistingEvent().getRunning()) {
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
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("lp user %s permission unset christmas.hider", receiver.getName()));
        if (GV.SnowballChatCoolDown.containsKey(receiver)) {
            return;
        }

        int hiders = 0;
        Event existingEvent = EventManager.getExistingEvent();
        existingEvent.removeParticipant(existingEvent.getParticipantFromPlayerName(receiver.getName()));

        for (Participant participant : existingEvent.getParticipants()) {
            if (participant.getEventRole().equals(EventRole.HIDER)) {
                hiders++;
            }
        }

        if (sender == null) {
            return;
        }

        Component deserialize;
        if (hiders == 1) {
            deserialize = miniMessage.deserialize("<red><sender> has found <receiver>. <amount_hiders> hider remaining.",
                    TagResolver.resolver(Placeholder.parsed("sender", sender.getName()), Placeholder.parsed("receiver", receiver.getName()), Placeholder.parsed("amount_hiders", String.valueOf(hiders))));
        } else {
            deserialize = miniMessage.deserialize("<red><sender> has found <receiver>. <amount_hiders> hiders remaining.",
                    TagResolver.resolver(Placeholder.parsed("sender", sender.getName()), Placeholder.parsed("receiver", receiver.getName()), Placeholder.parsed("amount_hiders", String.valueOf(hiders))));
        }

        Bukkit.getServer().broadcast(deserialize);
        GV.SnowballChatCoolDown.put(receiver, true);

        new BukkitRunnable() {
            public void run() {
                GV.SnowballChatCoolDown.remove(receiver);
            }
        }.runTaskLater(WinterHideAndSeek.getInstance(), 100);
    }
}
