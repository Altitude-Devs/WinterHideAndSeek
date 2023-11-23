package com.daki.main.objects;

import com.daki.main.Release;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Event {

    Boolean running;
    List<Participant> participants;
    Release release = null;
    EventTimer timer = null;
    Duration duration = Duration.ofHours(1);

    public Event() {

        running = false;
        participants = new ArrayList<>();

    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void addParticipant(Participant participant) {
        participant.getPlayer().getInventory().clear();
        List<Participant> jf = new ArrayList<>();
        for (Participant participant1 : participants){
            if (participant1.getPlayer() == participant.getPlayer()){
                jf.add(participant1);
            }
        }

        for (Participant p : jf){
            participants.remove(p);
        }

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

    public void createRelease(){
        release = new Release();
    }

    public Release getRelease(){
        return release;
    }

    public EventTimer getTimer(){
        return timer;
    }

    public void createTimer(){
        timer = new EventTimer(duration);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
