package com.artillexstudios.axsellwands.hooks.shop;

import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopGUIPlusHook implements PricesHook {

    @Override
    public void setup() {
    }

    @Override
    public double getPrice(ItemStack it) {
        return ShopGuiPlusApi.getItemStackPriceSell(it);
    }

    @Override
    public double getPrice(Player player, ItemStack it) {
        return ShopGuiPlusApi.getItemStackPriceSell(player, it);
    }
}