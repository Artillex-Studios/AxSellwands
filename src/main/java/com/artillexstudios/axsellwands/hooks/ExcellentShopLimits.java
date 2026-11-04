package com.artillexstudios.axsellwands.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import su.nightexpress.excellentshop.ShopPlugin;
import su.nightexpress.excellentshop.api.product.TradeType;
import su.nightexpress.excellentshop.api.product.limit.LimitData;
import su.nightexpress.excellentshop.api.product.stock.StockData;
import su.nightexpress.excellentshop.virtualshop.VirtualShopModule;
import su.nightexpress.excellentshop.virtualshop.product.VirtualProduct;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ExcellentShopLimits {
    private final VirtualShopModule module;
    private final Map<UUID, Integer> previewUnits = new HashMap<>();

    public ExcellentShopLimits() {
        ShopPlugin plugin = (ShopPlugin) Bukkit.getPluginManager().getPlugin("ExcellentShop");
        if (plugin == null || !plugin.isEnabled()) throw new IllegalStateException("ExcellentShop is not enabled");
        String version = plugin.getDescription().getVersion();
        if (!version.matches("^5\\.1(?:\\..*)?$")) {
            throw new IllegalStateException("Unsupported ExcellentShop version " + version + "; expected 5.1.x");
        }
        module = plugin.getModuleRegistry().byType(VirtualShopModule.class).orElseThrow();
    }

    public record Quote(VirtualProduct product, LimitData limit, StockData stock, int units, int items, double price) {}

    static int allowedUnits(int items, int unitSize, int limit, int used, int space, int reserved) {
        if (items <= 0 || unitSize <= 0) return 0;
        int units = items / unitSize;
        if (limit >= 0) units = Math.min(units, (int) Math.max(0L, (long) limit - used - reserved));
        if (space >= 0) units = Math.min(units, (int) Math.max(0L, (long) space - reserved));
        return units;
    }

    public Quote quote(Player player, ItemStack stack, float multiplier, boolean preview) {
        if (stack == null || stack.getAmount() <= 0) return null;
        ItemStack search = stack.clone();
        search.setAmount(1);
        VirtualProduct product = module.getBestProductFor(search, TradeType.SELL);
        if (product == null || !product.isSellable() || !product.canTrade(player)) return null;
        LimitData limit = product.getLimitData(player);
        int reserved = preview ? previewUnits.getOrDefault(product.getGlobalId(), 0) : 0;
        int units = allowedUnits(stack.getAmount(), product.getUnitSize(), product.getSellLimit(),
                limit.getSales(), product.getSpace(), reserved);
        if (units <= 0) return null;
        double price = product.getFinalSellPrice(player, units) * multiplier;
        if (!Double.isFinite(price) || price <= 0) return null;
        if (preview) previewUnits.merge(product.getGlobalId(), units, Integer::sum);
        return new Quote(product, limit, product.getStockData(), units, units * product.getUnitSize(), price);
    }

    public void record(Quote quote) {
        quote.stock().store(quote.units());
        quote.limit().addSales(quote.units());
        if (quote.product().getSellLimit() >= 0 && !quote.limit().isRestocking()) {
            quote.limit().setRestockDate(quote.product().getLimitOptions().generateRestockTimestamp());
            quote.limit().setRestocking(true);
        }
        quote.stock().markDirty();
        quote.limit().markDirty();
    }
}
