package com.artillexstudios.axsellwands.hooks.shop;

import com.willfp.ecoshop.integrations.EcoShopAdapter;
import com.willfp.eco.core.price.Price;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EcoShopHook implements PricesHook {

    @Override
    public void setup() {
    }

    @Override
    public double getPrice(ItemStack it) {
        return 0; // EcoShop doesn't have a way to get the price of an item without a player
    }

    @Override
    public double getPrice(Player player, ItemStack it) {
        return EcoShopAdapter.INSTANCE.getUnitValue(it, player).getValue(player);
    }
}