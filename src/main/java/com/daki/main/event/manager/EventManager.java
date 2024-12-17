package com.daki.main.event.manager;

import com.daki.main.objects.Event;
import lombok.Getter;

public class EventManager {

    @Getter
    public static Event existingEvent;

    public static void setExistingEvent(Event existingEvent) {
        EventManager.existingEvent = existingEvent;
    }

}
