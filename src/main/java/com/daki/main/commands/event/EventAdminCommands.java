package com.daki.main.commands.event;

import com.daki.main.WinterHideAndSeek;
import com.daki.main.event.events.EventEndEvent;
import com.daki.main.event.manager.EventManager;
import com.daki.main.event.events.EventReloadEvent;
import com.daki.main.event.events.EventStartEvent;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.EventTimer;
import com.daki.main.objects.Participant;
import com.daki.main.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class EventAdminCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length < 1) return true;

        switch (args[0]) {
            case "start" -> {
                if (EventManager.getExistingEvent().getRunning()) {
                    sender.sendMessage(ChatColor.RED + "Event is already running!");
                    return true;
                }
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Please specify an event duration in seconds!");
                    return true;
                } else {
                    Duration duration;
                    try {
                        duration = Duration.ofSeconds(Integer.parseInt(args[1]));
                    } catch (Exception e) {
                        //lazy but im in a hurry
                        sender.sendMessage(ChatColor.RED + "Please specify an event duration in seconds (numbers only)!");
                        return true;
                    }

                    Bukkit.getPluginManager().callEvent(new EventStartEvent(duration));

                }
            }
            case "end" -> {
                if (!EventManager.getExistingEvent().getRunning()) {
                    sender.sendMessage(ChatColor.RED + "The event is not running!");
                    return true;
                }
                Bukkit.getPluginManager().callEvent(new EventEndEvent());
            }
            case "reload" -> {
                if (!EventManager.getExistingEvent().getRunning()) {
                    sender.sendMessage(ChatColor.RED + "The event is not running!");
                    return true;
                }
                Bukkit.getPluginManager().callEvent(new EventReloadEvent());
            }
            case "hiders" -> {
                if (args.length < 3) return true;
                if (args[1].equals("add")) {

                    Player player = Bukkit.getPlayer(args[2]);

                    Participant participant = new Participant(player, EventRole.HIDER);

                    EventManager.getExistingEvent().addParticipant(participant);

                } else if (args[1].equals("remove")) {

                    Player player = Bukkit.getPlayer(args[2]);

                    Participant participant = new Participant(player, EventRole.HIDER);

                    EventManager.getExistingEvent().removeParticipant(participant);

                }
            }
            case "seekers" -> {
                if (args.length < 3) return true;
                if (args[1].equals("add")) {

                    Player player = Bukkit.getPlayer(args[2]);

                    Participant participant = new Participant(player, EventRole.SEEKER);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission settemp cmi.kit.christmasseeker true 7d");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi warp seekerswaitwarp " + player.getName() + " -s");
                    EventManager.getExistingEvent().addParticipant(participant);

                } else if (args[1].equals("remove")) {

                    Player player = Bukkit.getPlayer(args[2]);

                    Participant participant = new Participant(player, EventRole.SEEKER);

                    EventManager.getExistingEvent().removeParticipant(participant);

                }
            }
            case "remaining" -> {

                List<String> hiders = new ArrayList<>();
                List<String> seekers = new ArrayList<>();
                List<String> notParticipating = new ArrayList<>();

                for (Player player : Bukkit.getOnlinePlayers()) {

                    Participant participant = EventManager.existingEvent.getParticipantFromPlayerName(player.getName());

                    if (participant == null) {
                        notParticipating.add(player.getName());
                        continue;

                    }
                    if (participant.getEventRole().equals(EventRole.HIDER)) {
                        hiders.add(participant.getPlayer().getName());
                    } else if (participant.getEventRole().equals(EventRole.SEEKER)) {
                        seekers.add(participant.getPlayer().getName());
                    }

                }

                String message = "";

                message = message.concat("Hiders (" + hiders.size() + "): ");
                for (Integer i = 0; i < hiders.size(); i++) {
                    message = message.concat(hiders.get(i));
                    if (i != hiders.size() - 1) {
                        message = message.concat(", ");
                    }
                }

                message = message.concat("\nSeekers (" + seekers.size() + "): ");
                for (Integer i = 0; i < seekers.size(); i++) {
                    message = message.concat(seekers.get(i));
                    if (i != seekers.size() - 1) {
                        message = message.concat(", ");
                    }
                }

                message = message.concat("\nNot participating (" + notParticipating.size() + "): ");
                for (Integer i = 0; i < notParticipating.size(); i++) {
                    message = message.concat(notParticipating.get(i));
                    if (i != notParticipating.size() - 1) {
                        message = message.concat(", ");
                    }
                }

                sender.sendMessage(message);

            }
            case "release" -> {
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
            case "addstartpoint" -> {
                FileConfiguration config = WinterHideAndSeek.getInstance().getConfig();
                List<String> startpoints;
                if (!config.contains("startpoints")) {
                    startpoints = new ArrayList<>();
                } else {
                    startpoints = config.getStringList("startpoints");
                }
                Location location = sender.getServer().getPlayer(sender.getName()).getLocation();
                String loc = location.getWorld().getName() + ", " + (Math.round(location.getX()) + .5) + ", " +
                        (Math.round(location.getY()) + .5) + ", " + (Math.round(location.getZ()) + .5);
                startpoints.add(loc);
                config.set("startpoints", startpoints);
                WinterHideAndSeek.getInstance().saveConfig();
                sender.sendMessage(ChatColor.GREEN + "Added " + loc + " as a start point for hiders.");
            }
            case "deleteallstartpoints" -> {
                FileConfiguration configFile = WinterHideAndSeek.getInstance().getConfig();
                List<String> startpointsList = new ArrayList<>();
                configFile.set("startpoints", startpointsList);
                WinterHideAndSeek.getInstance().saveConfig();
            }
            case "remainingtime" -> {
                EventTimer timer = EventManager.getExistingEvent().getTimer();
                if (timer == null) {
                    sender.sendMessage(ChatColor.RED + "No timer started yet!");
                    return true;
                }
                Duration remainingTime = timer.getRemainingTime();
                String message;
                long remainingSeconds = remainingTime.toSeconds();
                if (remainingTime.isZero()) {
                    message = remainingSeconds + " seconds left in the event.";
                } else if (remainingSeconds == 1) {
                    message = remainingSeconds + " second left in the event.";
                } else if (remainingSeconds < 60) {
                    message = remainingSeconds + " seconds left in the event.";
                } else {
                    int minutes = (int) remainingTime.toMinutes();
                    int seconds = remainingTime.toSecondsPart();
                    message = minutes + " minute";
                    if (minutes > 1) {
                        message += "s";
                    }
                    if (seconds != 0) {
                        message += " and " + seconds + " second";
                        if (seconds > 1) {
                            message += "s";
                        }
                    }
                    message += " left in the event.";
                }
                sender.sendMessage(ChatColor.AQUA + message);
            }
        }

        return true;

    }

    private void start() {

    }

}