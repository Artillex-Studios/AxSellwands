package com.artillexstudios.axsellwands.hooks.shop;

import com.Zrips.CMI.Modules.Worth.WorthItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CMIPricesHook implements PricesHook {
    private com.Zrips.CMI.CMI manager;

    @Override
    public void setup() {
        manager = com.Zrips.CMI.CMI.getInstance();
    }

    @Override
    public double getPrice(ItemStack it) {
        final WorthItem worth = manager.getWorthManager().getWorth(it);
        if (worth == null) return -1.0D;

        return worth.getSellPrice() == 0.0D ? -1.0 : worth.getSellPrice();
    }

    @Override
    public double getPrice(Player player, ItemStack it) {
        final WorthItem worth = manager.getWorthManager().getWorth(it);
        if (worth == null) return -1.0D;

        return worth.getSellPrice() == 0.0D ? -1.0 : worth.getSellPrice();
    }
}