package com.daki.main.event.listeners;

import com.daki.main.Release;
import com.daki.main.WinterHideAndSeek;
import com.daki.main.Sounds;
import com.daki.main.christmas.seeker.items.SeekerItems;
import com.daki.main.event.events.EventStartEvent;
import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.Event;
import com.daki.main.objects.Participant;
import com.daki.main.objects.TitleCreator;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class EventStartEventListener implements Listener {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEventStart(EventStartEvent event) {

        for (Participant participant : EventManager.getExistingEvent().getParticipants()) {
            if (!participant.getEventRole().equals(EventRole.SEEKER)) {
                continue;
            }
            participant.getPlayer().getInventory().clear();
            participant.getPlayer().getInventory().addItem(SeekerItems.snowballs());
            participant.getPlayer().getInventory().addItem(SeekerItems.speed());

        }

        Sounds.playSounds();

        ArrayList<Location> seekerSpawnLocations = getSeekerSpawnLocations();
        Random r = new Random();
        Event existingEvent = EventManager.getExistingEvent();

        Title startingTitle = TitleCreator.createTitle("<white>WINTER</white>",
                "<white>HIDE AND SEEK EVENT IS STARTING</white>", 1, 5, 1);
        for (Participant participant : existingEvent.getParticipants()) {

            if (!participant.getEventRole().equals(EventRole.HIDER)){
                break;
            }

            Player player = participant.getPlayer();

            player.teleport(seekerSpawnLocations.get(r.nextInt(seekerSpawnLocations.size())));

            player.showTitle(startingTitle);

        }
        Title hideSpotTitle = TitleCreator.createTitle("<white>FIND A SPOT TO HIDE IN",
                "SEEKERS WILL BE RELEASED IN 5 MINUTES", 1, 5, 1);
        new BukkitRunnable() {
            public void run() {
                existingEvent.getParticipants().forEach(participant -> participant.getPlayer().showTitle(hideSpotTitle));
            }

        }.runTaskLater(WinterHideAndSeek.getInstance(), 120);

        Bukkit.broadcast(miniMessage.deserialize("<green>Event started!</green>"), "winterhideandseek.admin");

        existingEvent.createRelease();
        existingEvent.setRunning(true);

        Release release = existingEvent.getRelease();
        new BukkitRunnable() {
            public void run() {
                existingEvent.createTimer();
                release.release();
            }

        }.runTaskLater(WinterHideAndSeek.getInstance(), 6000); //5 minutes in ticks

        AtomicInteger counter = new AtomicInteger(0);
        new BukkitRunnable() {
            public void run() {
                int count = counter.getAndIncrement();
                if (count > 2) { //Run 3 times
                    this.cancel();
                    return;
                }
                Title hideSpotTitle = TitleCreator.createTitle("<white>FIND A SPOT TO HIDE IN",
                        String.format("SEEKERS WILL BE RELEASED IN %d MINUTE(S)", 4 - count), 1, 5, 1);
                existingEvent.getParticipants().forEach(participant -> participant.getPlayer().showTitle(hideSpotTitle));
            }
        }.runTaskTimer(WinterHideAndSeek.getInstance(), 1200, 1200); //Run every minute (internal logic stops it at 1 minute remaining assuming 5 minutes initially)

        EventManager.getExistingEvent().setRunning(true);
    }

    private ArrayList<Location> getSeekerSpawnLocations() {
        List<String> stringList = WinterHideAndSeek.getInstance().getConfig().getStringList("startpoints");
        ArrayList<Location> locations = new ArrayList<>();

        for (String s : stringList){
            String[] split = s.split(", ");
            World world = Bukkit.getServer().getWorld(split[0]);
            Location l = new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));

            locations.add(l);
        }

        return locations;
    }
}
