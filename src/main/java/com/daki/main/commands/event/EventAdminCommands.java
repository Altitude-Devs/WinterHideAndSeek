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
import com.daki.main.objects.TitleCreator;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class EventAdminCommands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length < 1) {
            return true;
        }

        switch (args[0]) {
            case "start" -> {
                if (EventManager.getExistingEvent().getRunning()) {
                    sender.sendRichMessage("<red>Event is already running!</red>");
                    return true;
                }
                if (args.length != 2) {
                    sender.sendRichMessage("<red>Please specify an event duration in seconds!</red>");
                    return true;
                } else {
                    Duration duration;
                    try {
                        duration = Duration.ofSeconds(Integer.parseInt(args[1]));
                    } catch (Exception e) {
                        //lazy but im in a hurry
                        sender.sendRichMessage("<red>Please specify an event duration in seconds (numbers only)!</red>");
                        return true;
                    }

                    Bukkit.getPluginManager().callEvent(new EventStartEvent(duration));
                }
            }
            case "end" -> {
                if (!EventManager.getExistingEvent().getRunning()) {
                    sender.sendRichMessage("<red>The event is not running!</red>");
                    return true;
                }
                Bukkit.getPluginManager().callEvent(new EventEndEvent());
            }
            case "reload" -> {
                if (!EventManager.getExistingEvent().getRunning()) {
                    sender.sendRichMessage("<red>The event is not running!</red>");
                    return true;
                }
                Bukkit.getPluginManager().callEvent(new EventReloadEvent());
            }
            case "hiders" -> {
                if (args.length < 3) {
                    return true;
                }
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
                if (args.length < 3) {
                    return true;
                }
                if (args[1].equals("add")) {
                    Player player = Bukkit.getPlayer(args[2]);
                    if (player == null) {
                        return false;
                    }

                    Participant participant = new Participant(player, EventRole.SEEKER);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("lp user %s permission settemp cmi.kit.christmasseeker true 7d", player.getName()));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("cmi warp seekerswaitwarp %s -s", player.getName()));
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
                for (int i = 0; i < hiders.size(); i++) {
                    message = message.concat(hiders.get(i));
                    if (i != hiders.size() - 1) {
                        message = message.concat(", ");
                    }
                }

                message = message.concat("\nSeekers (" + seekers.size() + "): ");
                for (int i = 0; i < seekers.size(); i++) {
                    message = message.concat(seekers.get(i));
                    if (i != seekers.size() - 1) {
                        message = message.concat(", ");
                    }
                }

                message = message.concat("\nNot participating (" + notParticipating.size() + "): ");
                for (int i = 0; i < notParticipating.size(); i++) {
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
                Title title = TitleCreator.createTitle("SEEKERS RELEASED", "GOOD LUCK", 1, 3, 1);
                Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(title));
            }
            case "addstartpoint" -> {
                FileConfiguration config = WinterHideAndSeek.getInstance().getConfig();
                List<String> startpoints;
                if (!config.contains("startpoints")) {
                    startpoints = new ArrayList<>();
                } else {
                    startpoints = config.getStringList("startpoints");
                }
                Player player = sender.getServer().getPlayer(sender.getName());
                if (player == null) {
                    return false;
                }
                Location location = player.getLocation();
                String loc = location.getWorld().getName() + ", " + (Math.round(location.getX()) + .5) + ", " + (Math.round(location.getY()) + .5) + ", " + (Math.round(location.getZ()) + .5);
                startpoints.add(loc);
                config.set("startpoints", startpoints);
                WinterHideAndSeek.getInstance().saveConfig();
                sender.sendRichMessage("<green>Added <location> as a start point for hiders.</green>", Placeholder.parsed("location", loc));
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
                    sender.sendRichMessage("<red>No timer started yet!</red>");
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
                sender.sendRichMessage("<aqua><message></aqua>", Placeholder.parsed("message", message));
            }
        }
        return true;
    }
}