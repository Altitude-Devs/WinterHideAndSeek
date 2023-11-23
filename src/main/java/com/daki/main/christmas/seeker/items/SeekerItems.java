package com.daki.main.christmas.seeker.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SeekerItems {

    public static ItemStack speed() {

        ItemStack speed = new ItemStack(Material.SUGAR);
        ItemMeta meta = speed.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Activate speed 2 for 10 seconds! 60 sec cooldown.");
        lore.add(ChatColor.BLUE + "THEY SPEEEEEDIN!");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.GREEN + "Sugar Rush!");
        speed.setItemMeta(meta);

        return speed;

    }

    public static ItemStack speedCoolDown() {

        ItemStack speed = new ItemStack(Material.SUGAR);
        ItemMeta meta = speed.getItemMeta();
        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.RED + "On Cooldown");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.RED + "Sugar Rush!");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        speed.setItemMeta(meta);
        speed.addUnsafeEnchantment(Enchantment.MENDING, 0);

        return speed;

    }

    public static ItemStack snowballs() {

        ItemStack snowball = new ItemStack(Material.SNOWBALL);
        ItemMeta meta = snowball.getItemMeta();
        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.BLUE + "Aim for their head!");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.AQUA + "Snowball");
        snowball.setAmount(16);
        snowball.setItemMeta(meta);

        return snowball;

    }

    public static ItemStack bed() {

        ItemStack bed = new ItemStack(Material.RED_BED, 1);
        ItemMeta meta = bed.getItemMeta();
        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.BLUE + "Marks you as the seeker");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.AQUA + "Seeker Bed!");

        bed.setItemMeta(meta);

        return bed;
    }
}
