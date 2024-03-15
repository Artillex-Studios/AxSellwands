package com.artillexstudios.axsellwands.hooks.shop;

import com.earth2me.essentials.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EssentialsHook implements PricesHook {
    private IEssentials manager;

    @Override
    public void setup() {
        manager = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");
    }

    @Override
    public double getPrice(ItemStack it) {
        if (manager.getWorth().getPrice(manager, it) == null) return -1.0d;
        return manager.getWorth().getPrice(manager, it).doubleValue() * it.getAmount();
    }

    @Override
    public double getPrice(Player player, ItemStack it) {
        if (manager.getWorth().getPrice(manager, it) == null) return -1.0d;
        return manager.getWorth().getPrice(manager, it).doubleValue() * it.getAmount();
    }
}