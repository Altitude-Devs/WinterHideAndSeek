package com.daki.main.commands.event;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

public class EventAdminCommandsTabComplete implements TabCompleter {
    private static final String[] COMMANDS = { "start", "end", "reload", "hiders", "seekers", "remaining", "release", "addstartpoint", "deleteallstartpoints", "remainingtime" };

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            for (String string : COMMANDS) {
                if (string.startsWith(args[0])) {
                    completions.add(string);
                }
            }
            return completions;
        }
        return null;
    }
}
