package com.homanti.wacorp.Listener;

import com.homanti.wacorp.DiscordBot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class ItemRenameListener implements Listener {
    public ItemRenameListener() {
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() instanceof AnvilInventory) {
            AnvilInventory anvil = (AnvilInventory) event.getInventory();

            int slot = event.getRawSlot();

            if (slot == 2) {
                ItemStack result = anvil.getItem(2);
                ItemStack original = anvil.getItem(0);
                if (result != null && result.hasItemMeta() && result.getItemMeta().hasDisplayName()) {
                    String newName = result.getItemMeta().getDisplayName();
                    String player;
                    
                    if (original != null && original.hasItemMeta() && original.getItemMeta().hasDisplayName()) {
                        player = event.getWhoClicked().getName();
                        DiscordBot.sendMessage("1230912142442631168", player + " переименовал " + result.getType() + '"' + original.getItemMeta().getDisplayName() + '"' + " в " + '"' + newName + '"');
                    } else {
                        player = event.getWhoClicked().getName();
                        DiscordBot.sendMessage("1230912142442631168", player + " переименовал " + result.getType() + " в " + newName);
                    }
                }
            }
        }

    }
}
