package com.artillexstudios.axsellwands.hooks.shop;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import su.nightexpress.nexshop.ShopAPI;
import su.nightexpress.nexshop.api.shop.VirtualShop;
import su.nightexpress.nexshop.api.shop.packer.ItemPacker;
import su.nightexpress.nexshop.api.shop.packer.ProductPacker;
import su.nightexpress.nexshop.api.shop.product.VirtualProduct;

@SuppressWarnings("UnreachableCode")
public class ExcellentShopHook implements PricesHook {

    @Override
    public void setup() {

    }

    @Override
    public double getPrice(ItemStack it) {
        for (VirtualShop shop : ShopAPI.getVirtualShop().getShops()) {
            for (VirtualProduct product : shop.getProducts()) {
                ProductPacker packer = product.getPacker();

                if (!product.isSellable()) {
                    continue;
                }

                if (packer instanceof ItemPacker) {
                    ItemPacker itemPacker = (ItemPacker) packer;
                    if (itemPacker.isItemMatches(it)) {
                        return product.getPricer().getSellPrice() / itemPacker.getUnitAmount() * it.getAmount();
                    }
                }
            }
        }

        return 0;
    }

    @Override
    public double getPrice(Player player, ItemStack it) {
        for (VirtualShop shop : ShopAPI.getVirtualShop().getShops()) {
            for (VirtualProduct product : shop.getProducts()) {
                ProductPacker packer = product.getPacker();

                if (!product.isSellable()) {
                    continue;
                }

                if (packer instanceof ItemPacker) {
                    ItemPacker itemPacker = (ItemPacker) packer;
                    if (itemPacker.isItemMatches(it)) {
                        return product.getPriceSell(player) / itemPacker.getUnitAmount() * it.getAmount();
                    }
                }
            }
        }

        return 0;
    }
}
