package com.daki.main.christmas.seeker.items;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class onTryToGetRidOfItem implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null){return;}
        if (event.getWhoClicked().hasPermission("winterhideandseek.admin")
                || (!event.getCurrentItem().getType().equals(Material.SUGAR)
                && !event.getCurrentItem().getType().equals(Material.SNOWBALL))) {
            return;
        }
        event.setCancelled(true);
        event.getWhoClicked().closeInventory();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlayerDropItem(PlayerDropItemEvent event) {
        if (!event.getPlayer().hasPermission("winterhideandseek.admin")
                && (event.getItemDrop().getItemStack().getType().equals(Material.SUGAR)
                        || event.getItemDrop().getItemStack().getType().equals(Material.SNOWBALL))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlayerSwapHandItem(PlayerSwapHandItemsEvent event) {
        if (event.getPlayer().hasPermission("winterhideandseek.admin") || event.getOffHandItem().getType().isAir()) {
            return;
        }
        if (!event.getOffHandItem().getType().equals(Material.SUGAR)
                && !event.getOffHandItem().getType().equals(Material.SNOWBALL)) {
            return;
        }
        event.setCancelled(true);
    }
}
