package com.daki.main.commands.hiders;

import java.util.ArrayList;
import java.util.List;

import com.daki.main.WinterHideAndSeek;
import com.daki.main.Sounds;
import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.Participant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class HidersEffects implements CommandExecutor {

    public static Boolean glowRunning = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) return true;

        if (args[0].equals("glow")) {
            if (args[1].equals("off")) {
                glowOff();
            } else
                glow(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            Sounds.playSounds();
        } else if (args[0].equals("chicken")) {
            chicken(Integer.parseInt(args[1]));
            Sounds.playSounds();
        } else if (args[0].equals("fireworks")) {
            fireworks(Integer.parseInt(args[1]));
            Sounds.playSounds();
        } else if (args[0].equals("freeze")) {
            freeze(Integer.parseInt(args[1]));
            Sounds.playSounds();
        }

        return false;

    }

    public void glow(Integer everyXseconds, Integer forYseconds) {

        glowRunning = true;
        Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Hiders have begun to glow! Keep an eye out seekers!");
        List<Player> hiders = new ArrayList<>();
        for (Participant participant : EventManager.getExistingEvent().getParticipants()) {
            if(participant.getEventRole().equals(EventRole.HIDER)) {
                hiders.add(participant.getPlayer());
            }
        }

        for (Integer i = 0; i < hiders.size(); i++) {
            Player player = hiders.get(i);
            new BukkitRunnable() {
                public void run() {
                    player.setGlowing(true);
                    new BukkitRunnable() {
                        public void run() {
                            player.setGlowing(false);
                        }
                    }.runTaskLater(WinterHideAndSeek.getInstance(), forYseconds * 20);
                }
            }.runTaskLater(WinterHideAndSeek.getInstance(), everyXseconds * 20 + i * everyXseconds * 20);
        }

    }

    public void glowOff() {

        glowRunning = false;
        Bukkit.broadcast(ChatColor.RED + "Glow turned off.", "winterhideandseek.admin");

    }

    public void chicken(Integer duration) {
        Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "The hiders have begun clucking!");
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!EventManager.getExistingEvent().getParticipantFromPlayerName(player.getName()).getEventRole().equals(EventRole.HIDER)) {
                continue;
            }
            for (int i = 0; i < duration; i++) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(WinterHideAndSeek.getInstance(), new Runnable() {
                    public void run() {
                        Bukkit.getWorld(player.getWorld().getName()).playSound(player.getLocation(),
                                Sound.ENTITY_CHICKEN_AMBIENT, 1.0F, 1.0F);
                    }
                }, i * 20L);

            }
        }
    }

    public void fireworks(Integer amount) {

        Bukkit.broadcastMessage(ChatColor.DARK_RED + "F" + ChatColor.DARK_GREEN + "I" + ChatColor.DARK_RED + "R"
                + ChatColor.DARK_GREEN + "E" + ChatColor.DARK_RED + "W" + ChatColor.DARK_GREEN + "O"
                + ChatColor.DARK_RED + "R" + ChatColor.DARK_GREEN + "K" + ChatColor.DARK_RED + "S" + ChatColor.GOLD
                + ChatColor.BOLD.toString() + " just went off above the hiders!");
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!EventManager.getExistingEvent().getParticipantFromPlayerName(player.getName()).getEventRole().equals(EventRole.HIDER)) {
                continue;
            }
            for (Integer i = 0; i < amount; i++) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(WinterHideAndSeek.getInstance(), new Runnable() {
                    public void run() {
                        Firework fw = (Firework) Bukkit.getWorld(player.getWorld().getName())
                                .spawnEntity(player.getLocation(), EntityType.FIREWORK);
                        FireworkMeta fireworkMeta = fw.getFireworkMeta();
                        FireworkEffect.Builder builder = FireworkEffect.builder();
                        builder.withColor(Color.GREEN);
                        builder.withColor(Color.RED);
                        builder.withFlicker();
                        builder.withFade(Color.GREEN);
                        builder.withFade(Color.RED);
                        builder.with(FireworkEffect.Type.STAR);
                        builder.trail(true);
                        FireworkEffect effect = builder.build();
                        fireworkMeta.addEffect(effect);
                        fireworkMeta.setPower(0);
                        fw.setFireworkMeta(fireworkMeta);

                    }
                }, i * 2);
            }
        }
    }

    public void levitate(Integer duration) {
        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "levitate");
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!EventManager.getExistingEvent().getParticipantFromPlayerName(player.getName()).getEventRole().equals(EventRole.HIDER)) {
                continue;
            }
            PotionEffect pot = new PotionEffect(PotionEffectType.LEVITATION, duration * 20, 0);
            player.addPotionEffect(pot);
        }
    }

    public void freeze(Integer duration) {
        Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "The hiders are now frozen in place for "
                + duration.toString() + " seconds!");
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!EventManager.getExistingEvent().getParticipantFromPlayerName(player.getName()).getEventRole().equals(EventRole.HIDER)) {
                continue;
            }
            PotionEffect pot = new PotionEffect(PotionEffectType.SLOW, duration * 20, 1000);
            player.addPotionEffect(pot);
        }
    }

    public void nametag(String string) {
        if (string.equals("off")) {
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Team team = board.registerNewTeam("Hiders");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (EventManager.getExistingEvent().getParticipantFromPlayerName(player.getName()).getEventRole().equals(EventRole.HIDER)) {
                    team.addEntry(player.getName());
                    player.setScoreboard(board);
                }
            }
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        } else if (string.equals("on")) {
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Team team = board.registerNewTeam("Hiders");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (EventManager.getExistingEvent().getParticipantFromPlayerName(player.getName()).getEventRole().equals(EventRole.HIDER)) {
                    team.addEntry(player.getName());
                    player.setScoreboard(board);
                }
            }
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        }

    }
}
