package com.artillexstudios.axsellwands.hooks;

import com.artillexstudios.axapi.config.Config;
import com.artillexstudios.axapi.libs.boostedyaml.settings.dumper.DumperSettings;
import com.artillexstudios.axapi.libs.boostedyaml.settings.general.GeneralSettings;
import com.artillexstudios.axapi.libs.boostedyaml.settings.loader.LoaderSettings;
import com.artillexstudios.axapi.libs.boostedyaml.settings.updater.UpdaterSettings;
import com.artillexstudios.axintegrations.types.ShopIntegration;
import com.artillexstudios.axsellwands.AxSellwands;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.util.UUID;

public class BuiltinPrices extends ShopIntegration {
    private Config PRICES;

    public BuiltinPrices() {
        super("builtin");
    }

    @Override
    public boolean canLoad() {
        return true;
    }

    @Override
    public boolean setup() {
        PRICES = new Config(new File(AxSellwands.getInstance().getDataFolder(), "prices.yml"), AxSellwands.getInstance().getResource("prices.yml"), GeneralSettings.builder().setUseDefaults(false).build(), LoaderSettings.builder().build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setKeepAll(true).build());
        return PRICES.getBackingDocument() != null;
    }

    @Override
    public @Nullable Double getBuyPrice(@NonNull ItemStack itemStack) {
        return getSellPrice(itemStack);
    }

    @Override
    public @Nullable Double getBuyPrice(UUID uuid, @NonNull ItemStack itemStack) {
        return getSellPrice(uuid, itemStack);
    }

    @Override
    public @Nullable Double getSellPrice(@NonNull ItemStack itemStack) {
        Double price = PRICES.getBackingDocument().getDouble(itemStack.getType().name());
        if (price == null || price <= 0) return null;
        return price * itemStack.getAmount();
    }

    @Override
    public @Nullable Double getSellPrice(UUID uuid, @NonNull ItemStack itemStack) {
        return getSellPrice(itemStack);
    }
}
