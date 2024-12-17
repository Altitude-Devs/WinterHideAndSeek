package com.daki.main.objects;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;

import java.time.Duration;

public class TitleCreator {

    private final static MiniMessage miniMessage = MiniMessage.miniMessage();

    public static Title createTitle(String title, String subtitle, int fadeInSecond, int staySecond, int fadeOutSecond) {
        return Title.title(miniMessage.deserialize(title),
                miniMessage.deserialize(subtitle),
                Title.Times.times(Duration.ofSeconds(fadeInSecond), Duration.ofSeconds(staySecond), Duration.ofSeconds(fadeOutSecond)));
    }

}
