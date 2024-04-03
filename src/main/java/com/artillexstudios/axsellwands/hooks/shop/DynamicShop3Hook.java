package com.artillexstudios.axsellwands.hooks.shop;

import me.sat7.dynamicshop.DynaShopAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DynamicShop3Hook implements PricesHook {

    @Override
    public void setup() {
    }

    @Override
    public double getPrice(ItemStack it) {
        double most = -1.0D;
        for (String str : DynaShopAPI.getShops()) {
            most = Math.max(DynaShopAPI.getBuyPrice(str, it), most);
        }
        if (most == -1.0D) return most;
        return most * it.getAmount();
    }

    @Override
    public double getPrice(Player player, ItemStack it) {
        return getPrice(it);
    }
}