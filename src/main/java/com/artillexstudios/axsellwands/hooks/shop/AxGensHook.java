package com.artillexstudios.axsellwands.hooks.shop;

import com.artillexstudios.axgens.hooks.HookManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AxGensHook implements PricesHook {

    @Override
    public void setup() {
    }

    @Override
    public double getPrice(ItemStack it) {
        if (HookManager.getShopPrices() == null) return -1.0D;
        return HookManager.getShopPrices().getPrice(it);
    }

    @Override
    public double getPrice(Player player, ItemStack it) {
        if (HookManager.getShopPrices() == null) return -1.0D;
        return HookManager.getShopPrices().getPrice(player, it);
    }
}