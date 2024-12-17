package com.daki.main;

import com.daki.main.christmas.ParticipantLeaveEvent;
import com.daki.main.christmas.PlayerConnectEvent;
import com.daki.main.event.listeners.EventEndEventListener;
import com.daki.main.event.manager.EventManager;
import com.daki.main.event.listeners.EventReloadEventListener;
import com.daki.main.event.listeners.EventStartEventListener;
import com.daki.main.objects.Event;
import com.daki.main.commands.hiders.HidersEffects;
import com.daki.main.commands.hiders.HidersEffectsTabComplete;
import com.daki.main.christmas.seeker.*;
import com.daki.main.christmas.seeker.items.onTryToGetRidOfItem;
import com.daki.main.commands.event.EventAdminCommands;
import com.daki.main.commands.event.EventAdminCommandsTabComplete;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WinterHideAndSeek extends JavaPlugin {

    @Getter
    public static WinterHideAndSeek instance;

    @Override
    public void onEnable() {
        instance = this;
        EventManager.setExistingEvent(new Event());

        WinterHideAndSeek.getInstance().getLogger().info("--------------------------------------------------");
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new onSnowballHit(), this);
        pluginManager.registerEvents(new EventStartEventListener(), this);
        pluginManager.registerEvents(new EventEndEventListener(), this);
        pluginManager.registerEvents(new EventReloadEventListener(), this);
        pluginManager.registerEvents(new onAbilityUse(), this);
        pluginManager.registerEvents(new onTryToGetRidOfItem(), this);
        pluginManager.registerEvents(new onSnowballThrow(), this);
        pluginManager.registerEvents(new ParticipantLeaveEvent(), this);
        pluginManager.registerEvents(new PlayerConnectEvent(), this);

        getCommand("hiders").setExecutor(new HidersEffects());
        getCommand("hiders").setTabCompleter(new HidersEffectsTabComplete());

        getCommand("event").setExecutor(new EventAdminCommands());
        getCommand("event").setTabCompleter(new EventAdminCommandsTabComplete());

        WinterHideAndSeek.getInstance().getLogger().info("WinterHideAndSeek plugin started!");
        WinterHideAndSeek.getInstance().getLogger().info("--------------------------------------------------");

    }
}
