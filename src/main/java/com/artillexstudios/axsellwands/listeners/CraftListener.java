package com.artillexstudios.axsellwands.listeners;

import com.artillexstudios.axsellwands.utils.NBTUtils;
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

            if (NBTUtils.readStringFromNBT(it, "axsellwands-type") != null) {
                event.getInventory().setResult(null);
                return;
            }
        }
    }
}
