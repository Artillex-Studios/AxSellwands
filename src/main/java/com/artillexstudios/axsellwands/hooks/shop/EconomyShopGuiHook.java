package com.artillexstudios.axsellwands.hooks.shop;

import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EconomyShopGuiHook implements PricesHook {

    @Override
    public void setup() {
    }

    @Override
    public double getPrice(ItemStack it) {
        if (EconomyShopGUIHook.getShopItem(it) == null) return -1.0;

        Double sellPrice = EconomyShopGUIHook.getItemSellPrice(EconomyShopGUIHook.getShopItem(it), it);

        if (sellPrice == null) return -1.0;

        return sellPrice;
    }

    @Override
    public double getPrice(Player player, ItemStack it) {
        if (EconomyShopGUIHook.getShopItem(it) == null) return -1.0;

        Double sellPrice = EconomyShopGUIHook.getItemSellPrice(EconomyShopGUIHook.getShopItem(it), it, player);

        if (sellPrice == null) return -1.0;

        return sellPrice;
    }
}