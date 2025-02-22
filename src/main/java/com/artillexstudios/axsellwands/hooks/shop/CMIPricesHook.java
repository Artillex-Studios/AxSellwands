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
        ItemStack clone = it.clone();
        clone.setAmount(1);
        WorthItem worth = manager.getWorthManager().getWorth(clone);
        if (worth == null) return -1.0D;

        return worth.getSellPrice() == 0.0D ? -1.0D : (worth.getSellPrice() * it.getAmount());
    }

    @Override
    public double getPrice(Player player, ItemStack it) {
        ItemStack clone = it.clone();
        clone.setAmount(1);
        final WorthItem worth = manager.getWorthManager().getWorth(clone);
        if (worth == null) return -1.0D;

        return worth.getSellPrice() == 0.0D ? -1.0D : (worth.getSellPrice() * it.getAmount());
    }
}