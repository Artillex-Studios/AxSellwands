package com.artillexstudios.axsellwands.hooks.shop;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xshyo.us.shopMaster.api.ShopMasterAPI;

public class ShopMasterHook implements PricesHook {

    @Override
    public void setup() {
    }

    @Override
    public double getPrice(ItemStack it) {
        return ShopMasterAPI.getItemStackPriceSell(it);
    }

    @Override
    public double getPrice(Player player, ItemStack it) {
        return ShopMasterAPI.getItemStackPriceSell(player, it);
    }
}