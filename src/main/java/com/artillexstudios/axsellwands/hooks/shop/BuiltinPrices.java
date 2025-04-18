package com.artillexstudios.axsellwands.hooks.shop;

import com.artillexstudios.axapi.config.Config;
import com.artillexstudios.axapi.libs.boostedyaml.settings.dumper.DumperSettings;
import com.artillexstudios.axapi.libs.boostedyaml.settings.general.GeneralSettings;
import com.artillexstudios.axapi.libs.boostedyaml.settings.loader.LoaderSettings;
import com.artillexstudios.axapi.libs.boostedyaml.settings.updater.UpdaterSettings;
import com.artillexstudios.axsellwands.AxSellwands;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class BuiltinPrices implements PricesHook {
    private Config PRICES;

    @Override
    public void setup() {
        PRICES = new Config(new File(AxSellwands.getInstance().getDataFolder(), "prices.yml"), AxSellwands.getInstance().getResource("prices.yml"), GeneralSettings.builder().setUseDefaults(false).build(), LoaderSettings.builder().build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setKeepAll(true).build());
    }

    @Override
    public double getPrice(ItemStack it) {
        if (it == null) return -1.0D;
        final double price = PRICES.getDouble(it.getType().name(), -1.0D);
        if (price == -1.0D) return price;
        return price * it.getAmount();
    }

    @Override
    public double getPrice(Player player, ItemStack it) {
        if (it == null) return -1.0D;
        final double price = PRICES.getDouble(it.getType().name(), -1.0D);
        if (price == -1.0D) return price;
        return price * it.getAmount();
    }
}