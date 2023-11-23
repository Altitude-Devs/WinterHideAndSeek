package com.daki.main.christmas.seeker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class GV {

    public static List<Player> seekers = new ArrayList<>();
    public static List<Player> hiders = new ArrayList<>();
    public static HashMap<Player, Boolean> SnowballChatCoolDown = new HashMap<>();
    public static HashMap<Player, Boolean> SugarRushCoolDown = new HashMap<>();

}
