package com.daki.main.objects;

import com.daki.main.objects.Enums.EventRole;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Setter
@Getter
public class Participant {

    Player player;
    EventRole eventRole;

    public Participant(Player player, EventRole eventRole) {
        this.player = player;
        this.eventRole = eventRole;
    }

}
