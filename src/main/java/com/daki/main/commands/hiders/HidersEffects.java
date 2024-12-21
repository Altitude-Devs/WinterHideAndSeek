package com.daki.main.commands.hiders;

import java.util.ArrayList;
import java.util.List;

import com.daki.main.WinterHideAndSeek;
import com.daki.main.Sounds;
import com.daki.main.event.manager.EventManager;
import com.daki.main.objects.Enums.EventRole;
import com.daki.main.objects.Participant;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public class HidersEffects implements CommandExecutor {

    public static Boolean glowRunning = false;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (args.length == 0) return true;

        switch (args[0]) {
            case "glow" -> {
                if (args[1].equals("off")) {
                    glowOff();
                } else
                    glow(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                Sounds.playSounds();
            }
            case "chicken" -> {
                chicken(Integer.parseInt(args[1]));
                Sounds.playSounds();
            }
            case "fireworks" -> {
                fireworks(Integer.parseInt(args[1]));
                Sounds.playSounds();
            }
            case "freeze" -> {
                freeze(Integer.parseInt(args[1]));
                Sounds.playSounds();
            }
        }

        return false;

    }

    public void glow(Integer everyXseconds, Integer forYseconds) {
        glowRunning = true;
        Bukkit.broadcast(miniMessage.deserialize("<gold><b>Hiders have begun to glow! Keep an eye out seekers!</b></gold>"));
        List<Player> hiders = new ArrayList<>();
        for (Participant participant : EventManager.getExistingEvent().getParticipants()) {
            if (participant.getEventRole().equals(EventRole.HIDER)) {
                hiders.add(participant.getPlayer());
            }
        }

        GlowingTask glowingTask = new GlowingTask(hiders, everyXseconds, forYseconds);
        glowingTask.start();
    }

    public void glowOff() {
        glowRunning = false;
        Bukkit.broadcast(miniMessage.deserialize("<red>Glow turned off.</red>"), "winterhideandseek.admin");

    }

    public void chicken(Integer duration) {
        Bukkit.broadcast(miniMessage.deserialize("<gold><b>The hiders have begun clucking!</b></gold>"));
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!EventManager.getExistingEvent().getParticipantFromPlayerName(player.getName()).getEventRole().equals(EventRole.HIDER)) {
                continue;
            }
            for (int i = 0; i < duration; i++) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(WinterHideAndSeek.getInstance(), () -> {
                    World world = Bukkit.getWorld(player.getWorld().getName());
                    if (world == null) {
                        return;
                    }
                    world.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_AMBIENT, 1.0F, 1.0F);
                }, i * 20L);
            }
        }
    }

    public void fireworks(Integer amount) {
        String message = "<dark_red>F<dark_green>I<dark_red>R<dark_green>E<dark_red>W<dark_green>O<dark_red>R<dark_green>K<dark_red>S"
                         + "<gold><bold> just went off above the hiders!";
        Bukkit.broadcast(miniMessage.deserialize(message));
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!EventManager.getExistingEvent().getParticipantFromPlayerName(player.getName()).getEventRole().equals(EventRole.HIDER)) {
                continue;
            }
            for (int i = 0; i < amount; i++) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(WinterHideAndSeek.getInstance(), () -> {
                    Firework fw = (Firework) player.getWorld()
                            .spawnEntity(player.getLocation(), EntityType.FIREWORK_ROCKET);
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

                }, i * 2L);
            }
        }
    }

    public void levitate(Integer duration) {
        Bukkit.getServer().broadcast(miniMessage.deserialize("<gold><b>levitate</b></gold>"));
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!EventManager.getExistingEvent().getParticipantFromPlayerName(player.getName()).getEventRole().equals(EventRole.HIDER)) {
                continue;
            }
            PotionEffect pot = new PotionEffect(PotionEffectType.LEVITATION, duration * 20, 0);
            player.addPotionEffect(pot);
        }
    }

    public void freeze(Integer duration) {
        Bukkit.getServer().broadcast(miniMessage.deserialize("<gold><b>The hiders are now frozen in place for <duration> seconds!</b></gold>",
                Placeholder.parsed("duration", duration.toString())));
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!EventManager.getExistingEvent().getParticipantFromPlayerName(player.getName()).getEventRole().equals(EventRole.HIDER)) {
                continue;
            }
            setPlayerSpeed(player, 0);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(WinterHideAndSeek.getInstance(), () -> {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (!EventManager.getExistingEvent().getParticipantFromPlayerName(player.getName()).getEventRole().equals(EventRole.HIDER)) {
                    continue;
                }
                setPlayerSpeed(player, 0.1);
            }
        }, duration * 20);
    }

    private static void setPlayerSpeed(Player player, double value) {
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (attribute == null) {
            return;
        }
        attribute.setBaseValue(value);
    }
}
