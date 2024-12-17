package com.daki.main.christmas.seeker.items;

import java.util.ArrayList;
import java.util.List;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SeekerItems {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static ItemStack speed() {
        ItemStack speed = new ItemStack(Material.SUGAR);
        ItemMeta meta = speed.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add("<blue>Activate speed 2 for 10 seconds! 60 sec cooldown.</blue>");
        lore.add("<blue>THEY SPEEEEEDIN!</blue>");
        meta.lore(lore.stream().map(miniMessage::deserialize).toList());
        meta.displayName(miniMessage.deserialize("<green>Sugar Rush!</green>"));
        speed.setItemMeta(meta);

        return speed;
    }

    public static ItemStack speedCoolDown() {
        ItemStack speed = new ItemStack(Material.SUGAR);
        ItemMeta meta = speed.getItemMeta();
        List<String> lore = new ArrayList<>();

        lore.add("<red>On Cooldown</red>");
        meta.lore(lore.stream().map(miniMessage::deserialize).toList());
        meta.displayName(miniMessage.deserialize("<red>Sugar Rush!</red>"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        speed.setItemMeta(meta);
        speed.addUnsafeEnchantment(Enchantment.MENDING, 0);

        return speed;
    }

    public static ItemStack snowballs() {
        ItemStack snowball = new ItemStack(Material.SNOWBALL);
        ItemMeta meta = snowball.getItemMeta();
        List<String> lore = new ArrayList<>();

        lore.add("<blue>Aim for their head!</blue>");
        meta.lore(lore.stream().map(miniMessage::deserialize).toList());
        meta.displayName(miniMessage.deserialize("<aqua>Snowball</aqua>"));
        snowball.setAmount(16);
        snowball.setItemMeta(meta);

        return snowball;

    }

    public static ItemStack bed() {
        ItemStack bed = new ItemStack(Material.RED_BED, 1);
        ItemMeta meta = bed.getItemMeta();
        List<String> lore = new ArrayList<>();

        lore.add("<blue>Marks you as the seeker</blue>");
        meta.lore(lore.stream().map(miniMessage::deserialize).toList());
        meta.displayName(miniMessage.deserialize("<aqua>Seeker Bed!</aqua>"));

        bed.setItemMeta(meta);

        return bed;
    }
}
