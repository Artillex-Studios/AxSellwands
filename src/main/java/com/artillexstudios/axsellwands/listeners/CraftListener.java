package com.artillexstudios.axsellwands.listeners;

import com.artillexstudios.axapi.items.NBTWrapper;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CraftListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onCraft(@NotNull PrepareItemCraftEvent event) {
        for (ItemStack it : event.getInventory().getMatrix()) {
            if (it == null) continue;
            if (it.getType() == Material.AIR) continue;

            NBTWrapper wrapper = new NBTWrapper(it);
            if (wrapper.getString("axsellwands-type") != null) {
                event.getInventory().setResult(null);
                return;
            }
        }
    }
}
