package com.daki.main.christmas;

import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.Event;
import com.daki.main.objects.Participant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onPlayerConnect implements Listener{

    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent e){
        Event event = EventManager.getExistingEvent();
        Player player = e.getPlayer();

        if (player.hasPermission("winterhideandseek.bypass")){
            return;
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "lp user " + player.getName() + " permission settemp cmi.kit.christmas true 7d"); //Give them the participation reward
        if (!event.getRunning()){
            event.addParticipant(new Participant(player, EventRole.HIDER));
            player.sendTitle(ChatColor.DARK_GREEN + "JOINED EVENT",
                    ChatColor.GREEN + "You are a hider, the game will start soon!",
                    20, 60, 20 );
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "lp user " + player.getName() + " permission set christmas.hider");
        } else {
            player.sendTitle(ChatColor.DARK_RED + "ALREADY RUNNING",
                    ChatColor.RED + "You will be able to join next round!",
                    20, 60, 20 );
        }
    }
}
