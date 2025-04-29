package com.artillexstudios.axsellwands.hooks.shop;

import com.willfp.eco.core.price.Price;
import com.willfp.ecoshop.event.EcoShopSellEvent;
import com.willfp.ecoshop.integrations.EcoShopAdapter;
import com.willfp.ecoshop.shop.ShopItem;
import com.willfp.ecoshop.shop.ShopItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EcoShopHook implements PricesHook {

    @Override
    public void setup() {}

    @Override
    public double getPrice(ItemStack it) {
        return 0;
    }

    @Override
    public double getPrice(Player player, ItemStack it) {
        ShopItem shopItem = ShopItemUtils.getShopItem(it);
        if (shopItem == null) {
            return 0;
        }

        Price unitPrice = EcoShopAdapter.INSTANCE.getUnitValue(it, player);

        EcoShopSellEvent event = new EcoShopSellEvent(player, shopItem, unitPrice, it);
        Bukkit.getPluginManager().callEvent(event);

        double multiplier = event.getMultiplier();
        double baseValue   = unitPrice.getValue(player);

        return baseValue * multiplier * it.getAmount();
    }
}