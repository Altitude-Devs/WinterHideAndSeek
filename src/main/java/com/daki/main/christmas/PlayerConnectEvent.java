package com.daki.main.christmas;

import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.Event;
import com.daki.main.objects.Participant;
import com.daki.main.objects.TitleCreator;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnectEvent implements Listener {

    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent e) {
        Event event = EventManager.getExistingEvent();
        Player player = e.getPlayer();
        setPlayerSpeed(player, 0.1);
        if (player.hasPermission("winterhideandseek.bypass")) {
            return;
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "lp user " + player.getName() + " permission settemp cmi.kit.christmas true 7d"); //Give them the participation reward
        if (!event.getRunning()) {
            event.addParticipant(new Participant(player, EventRole.HIDER));
            Title title = TitleCreator.createTitle("<dark_green>JOINED EVENT</dark_green>",
                    "<green>You are a hider, the game will start soon!</green>",
                    1, 3, 1);
            player.showTitle(title);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "lp user " + player.getName() + " permission set christmas.hider");
        } else {
            Title title = TitleCreator.createTitle("<dark_red>ALREADY RUNNING</dark_red>",
                    "<red>You will be able to join next round!</red>",
                    1, 3, 1);
            player.showTitle(title);
        }
    }

    private static void setPlayerSpeed(Player player, double value) {
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (attribute == null) {
            return;
        }
        attribute.setBaseValue(value);
    }
}
