package com.artillexstudios.axsellwands.hooks.shop;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PricesHook {
    void setup();

    double getPrice(ItemStack it);

    double getPrice(Player player, ItemStack it);
}
