package com.daki.main.event.events;

import com.daki.main.event.manager.EventManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.time.Duration;

public class EventStartEvent extends Event {

    public EventStartEvent(Duration duration){
        EventManager.getExistingEvent().setDuration(duration);
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
