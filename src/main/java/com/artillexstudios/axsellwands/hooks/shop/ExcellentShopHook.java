package com.artillexstudios.axsellwands.hooks.shop;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import su.nightexpress.nexshop.ShopAPI;
import su.nightexpress.nexshop.api.shop.product.VirtualProduct;
import su.nightexpress.nexshop.api.shop.type.TradeType;

public class ExcellentShopHook implements PricesHook {

    @Override
    public void setup() {

    }

    @Override
    public double getPrice(ItemStack it) {
        VirtualProduct product = ShopAPI.getVirtualShop().getBestProductFor(it, TradeType.SELL);
        if (product == null || !product.isSellable()) return 0;
        return product.getPrice(TradeType.SELL) / product.getUnitAmount() * it.getAmount();
    }

    @Override
    public double getPrice(Player player, ItemStack it) {
        VirtualProduct product = ShopAPI.getVirtualShop().getBestProductFor(it, TradeType.SELL);
        if (product == null || !product.isSellable()) return 0;
        return product.getPrice(TradeType.SELL, player) / product.getUnitAmount() * it.getAmount();
    }
}
