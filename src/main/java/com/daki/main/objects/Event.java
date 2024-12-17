package com.daki.main.objects;

import com.daki.main.Release;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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

    public Event() {
        running = false;
        participants = new ArrayList<>();
    }

    public void addParticipant(Participant participant) {
        participant.getPlayer().getInventory().clear();
        List<Participant> filteredParticipants = participants.stream().filter(filterParticipant -> filterParticipant.getPlayer().getUniqueId().equals(participant.getPlayer().getUniqueId())).toList();
        filteredParticipants.forEach(p -> participants.remove(p));
        this.participants.add(participant);
    }

    public void removeParticipant(Participant participant) {
        this.participants.remove(participant);
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
    }

    public void createRelease() {
        release = new Release();
    }

    public void createTimer() {
        timer = new EventTimer(duration);
    }

}
