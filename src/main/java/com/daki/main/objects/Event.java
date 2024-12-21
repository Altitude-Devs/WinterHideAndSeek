package com.daki.main.objects;

import com.daki.main.Release;
import com.daki.main.objects.Enums.EventRole;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Event {

    @Setter
    @Getter
    Boolean running;
    @Setter
    @Getter
    List<Participant> participants;
    @Getter
    Release release = null;
    @Getter
    EventTimer timer = null;
    @Setter
    Duration duration = Duration.ofHours(1);

    private final Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
    private Team hidersTeam;

    public Event() {
        running = false;
        participants = new ArrayList<>();
        initHidersTeam();
    }

    public void addParticipant(Participant participant) {
        Player player = participant.getPlayer();
        player.getInventory().clear();
        List<Participant> filteredParticipants = participants.stream().filter(filterParticipant -> filterParticipant.getPlayer().getUniqueId().equals(player.getUniqueId())).toList();
        filteredParticipants.forEach(p -> participants.remove(p));
        this.participants.add(participant);
        if (participant.getEventRole().equals(EventRole.HIDER)) {
            hidersTeam.addEntry(player.getName());
            player.setScoreboard(board);
        }
        PotionEffect pot = new PotionEffect(PotionEffectType.NIGHT_VISION, (int) TimeUnit.HOURS.toSeconds(2) * 20, 1000); //Set it to 2 hours, shouldn't ever last longer
        player.addPotionEffect(pot);
    }

    public void removeParticipant(Participant participant) {
        this.participants.remove(participant);
        if (participant.getEventRole().equals(EventRole.HIDER)) {
            hidersTeam.removeEntry(participant.getPlayer().getName());
        }
        participant.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    public Participant getParticipantFromPlayerName(String playerName) {
        for (Participant participant : participants) {
            if (participant.getPlayer().getName().equals(playerName)) {
                return participant;
            }
        }
        return null;
    }

    public void clearParticipants() {
        participants.clear();
        hidersTeam.removeEntries(hidersTeam.getEntries());
    }

    public void createRelease() {
        release = new Release();
    }

    public void createTimer() {
        timer = new EventTimer(duration);
    }

    private void initHidersTeam() {
        hidersTeam = board.getTeam("Hiders");
        if (hidersTeam == null) {
            hidersTeam = board.registerNewTeam("Hiders");
        }
        hidersTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

}
