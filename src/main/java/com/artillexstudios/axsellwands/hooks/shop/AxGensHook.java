package com.artillexstudios.axsellwands.hooks.shop;

import com.artillexstudios.axgens.api.AxGensAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AxGensHook implements PricesHook {

    @Override
    public void setup() {
    }

    @Override
    public double getPrice(ItemStack it) {
        return AxGensAPI.getPrice(it);
    }

    @Override
    public double getPrice(Player player, ItemStack it) {
        return AxGensAPI.getPrice(player, it);
    }
}