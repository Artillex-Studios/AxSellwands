package com.artillexstudios.axsellwands.hooks;

import com.artillexstudios.axapi.reflection.ClassUtils;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axsellwands.hooks.container.ContainerHook;
import com.artillexstudios.axsellwands.hooks.currency.CoinsEngineHook;
import com.artillexstudios.axsellwands.hooks.currency.CurrencyHook;
import com.artillexstudios.axsellwands.hooks.currency.PlayerPointsHook;
import com.artillexstudios.axsellwands.hooks.currency.RoyaleEconomyHook;
import com.artillexstudios.axsellwands.hooks.currency.VaultHook;
import com.artillexstudios.axsellwands.hooks.other.Placeholders;
import com.artillexstudios.axsellwands.hooks.protection.BentoBoxHook;
import com.artillexstudios.axsellwands.hooks.protection.GriefPreventionHook;
import com.artillexstudios.axsellwands.hooks.protection.HuskClaimsHook;
import com.artillexstudios.axsellwands.hooks.protection.HuskTownsHook;
import com.artillexstudios.axsellwands.hooks.protection.IridiumSkyBlockHook;
import com.artillexstudios.axsellwands.hooks.protection.KingdomsXHook;
import com.artillexstudios.axsellwands.hooks.protection.LandsHook;
import com.artillexstudios.axsellwands.hooks.protection.PlotSquaredHook;
import com.artillexstudios.axsellwands.hooks.protection.ProtectionHook;
import com.artillexstudios.axsellwands.hooks.protection.ResidenceHook;
import com.artillexstudios.axsellwands.hooks.protection.SaberFactionsHook;
import com.artillexstudios.axsellwands.hooks.protection.SuperiorSkyBlock2Hook;
import com.artillexstudios.axsellwands.hooks.protection.TownyHook;
import com.artillexstudios.axsellwands.hooks.protection.WorldGuardHook;
import com.artillexstudios.axsellwands.hooks.protection.RealmCoreHook;
import com.artillexstudios.axsellwands.hooks.shop.AxGensHook;
import com.artillexstudios.axsellwands.hooks.shop.BuiltinPrices;
import com.artillexstudios.axsellwands.hooks.shop.CMIPricesHook;
import com.artillexstudios.axsellwands.hooks.shop.DynamicShop3Hook;
import com.artillexstudios.axsellwands.hooks.shop.EconomyShopGuiHook;
import com.artillexstudios.axsellwands.hooks.shop.EssentialsHook;
import com.artillexstudios.axsellwands.hooks.shop.ExcellentShopHook;
import com.artillexstudios.axsellwands.hooks.shop.PricesHook;
import com.artillexstudios.axsellwands.hooks.shop.ShopGUIPlusHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

import static com.artillexstudios.axsellwands.AxSellwands.HOOKS;

public class HookManager {
    private static CurrencyHook currency = null;
    private static PricesHook shopPrices = null;
    private static final HashSet<ContainerHook> CONTAINER_HOOKS = new HashSet<>();
    private static final HashSet<ProtectionHook> PROTECTION_HOOKS = new HashSet<>();

    public static void setupHooks() {
        updateHooks();

        if (HOOKS.getBoolean("hook-settings.PlaceholderAPI.register", true) && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders().register();
        }

        if (HOOKS.getBoolean("hook-settings.IridiumSkyBlock.register", true) && Bukkit.getPluginManager().getPlugin("IridiumSkyBlock") != null) {
            PROTECTION_HOOKS.add(new IridiumSkyBlockHook());
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into IridiumSkyBlock!"));
        }

        if (HOOKS.getBoolean("hook-settings.SuperiorSkyblock2.register", true) && Bukkit.getPluginManager().getPlugin("SuperiorSkyblock2") != null) {
            PROTECTION_HOOKS.add(new SuperiorSkyBlock2Hook());
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into SuperiorSkyblock2!"));
        }

        if (HOOKS.getBoolean("hook-settings.WorldGuard.register", true) && Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            PROTECTION_HOOKS.add(new WorldGuardHook());
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into WorldGuard!"));
        }

        if (HOOKS.getBoolean("hook-settings.Kingdoms.register", true) && Bukkit.getPluginManager().getPlugin("Kingdoms") != null) {
            PROTECTION_HOOKS.add(new KingdomsXHook());
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into Kingdoms!"));
        }

        if (HOOKS.getBoolean("hook-settings.BentoBox.register", true) && Bukkit.getPluginManager().getPlugin("BentoBox") != null) {
            PROTECTION_HOOKS.add(new BentoBoxHook());
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into BentoBox!"));
        }

        if (HOOKS.getBoolean("hook-settings.GriefPrevention.register", true) && Bukkit.getPluginManager().getPlugin("GriefPrevention") != null) {
            PROTECTION_HOOKS.add(new GriefPreventionHook());
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into GriefPrevention!"));
        }

        if (HOOKS.getBoolean("hook-settings.Lands.register", true) && Bukkit.getPluginManager().getPlugin("Lands") != null) {
            PROTECTION_HOOKS.add(new LandsHook());
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into Lands!"));
        }

        if (HOOKS.getBoolean("hook-settings.Residence.register", true) && Bukkit.getPluginManager().getPlugin("Residence") != null) {
            PROTECTION_HOOKS.add(new ResidenceHook());
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into Residence!"));
        }

        if (HOOKS.getBoolean("hook-settings.PlotSquared.register", true) && Bukkit.getPluginManager().getPlugin("PlotSquared") != null) {
            PROTECTION_HOOKS.add(new PlotSquaredHook());
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into PlotSquared!"));
        }

        if (HOOKS.getBoolean("hook-settings.HuskTowns.register", true) && Bukkit.getPluginManager().getPlugin("HuskTowns") != null) {
            PROTECTION_HOOKS.add(new HuskTownsHook());
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into HuskTowns!"));
        }

        if (HOOKS.getBoolean("hook-settings.HuskClaims.register", true) && Bukkit.getPluginManager().getPlugin("HuskClaims") != null) {
            PROTECTION_HOOKS.add(new HuskClaimsHook());
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into HuskClaims!"));
        }

        if (HOOKS.getBoolean("hook-settings.Towny.register", true) && Bukkit.getPluginManager().getPlugin("Towny") != null) {
            PROTECTION_HOOKS.add(new TownyHook());
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into Towny!"));
        }

        if (HOOKS.getBoolean("hook-settings.SaberFactions.register", true) && ClassUtils.INSTANCE.classExists("com.massivecraft.factions.listeners.SaberGUIListener")) {
            PROTECTION_HOOKS.add(new SaberFactionsHook());
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into SaberFactions!"));
        }

        if (HOOKS.getBoolean("hook-settings.RealmCore.register", true) && Bukkit.getPluginManager().getPlugin("RealmCore") != null) {
            PROTECTION_HOOKS.add(new RealmCoreHook());
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into RealmCore!"));
}
    }

    public static void updateHooks() {
        final String eco = HOOKS.getString("hooks.economy-plugin").toUpperCase();
        switch (eco) {
            case "VAULT": {
                if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                    currency = new VaultHook();
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into Vault!"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF3333[AxSellwands] Vault is set in hooks.yml, but it is not installed, please download it or change it to stop errors!"));
                }
                break;
            }

            case "PLAYERPOINTS": {
                if (Bukkit.getPluginManager().getPlugin("PlayerPoints") != null) {
                    currency = new PlayerPointsHook();
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into PlayerPoints!"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF3333[AxSellwands] PlayerPoints is set in hooks.yml, but it is not installed, please download it or change it to stop errors!"));
                }
                break;
            }

            case "COINSENGINE": {
                if (Bukkit.getPluginManager().getPlugin("CoinsEngine") != null) {
                    currency = new CoinsEngineHook();
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into CoinsEngine!"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF3333[AxSellwands] CoinsEngine is set in hooks.yml, but it is not installed, please download it or change it to stop errors!"));
                }
                break;
            }

            case "ROYALEECONOMY": {
                if (Bukkit.getPluginManager().getPlugin("RoyaleEconomy") != null) {
                    currency = new RoyaleEconomyHook();
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into RoyaleEconomy!"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF3333[AxSellwands] RoyaleEconomy is set in hooks.yml, but it is not installed, please download it or change it to stop errors!"));
                }
                break;
            }
        }
        if (currency != null)
            currency.setup();

        final String shop = HOOKS.getString("hooks.price-plugin").toUpperCase();
        switch (shop) {
            case "AXGENS": {
                if (Bukkit.getPluginManager().getPlugin("AxGens") != null) {
                    shopPrices = new AxGensHook();
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into AxGens!"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF3333[AxSellwands] AxGens is set in hooks.yml, but it is not installed, please download it or change it to stop errors!"));
                }
                break;
            }

            case "SHOPGUIPLUS": {
                if (Bukkit.getPluginManager().getPlugin("ShopGUIPlus") != null) {
                    shopPrices = new ShopGUIPlusHook();
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into ShopGUIPlus!"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF3333[AxSellwands] ShopGUIPlus is set in hooks.yml, but it is not installed, please download it or change it to stop errors!"));
                }
                break;
            }

            case "ESSENTIALS": {
                if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
                    shopPrices = new EssentialsHook();
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into Essentials!"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF3333[AxSellwands] Essentials is set in hooks.yml, but it is not installed, please download it or change it to stop errors!"));
                }
                break;
            }

            case "CMI": {
                if (Bukkit.getPluginManager().getPlugin("CMI") != null) {
                    shopPrices = new CMIPricesHook();
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into CMI (prices)!"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF3333[AxSellwands] CMI is set in hooks.yml, but it is not installed, please download it or change it to stop errors!"));
                }
                break;
            }

            case "ECONOMYSHOPGUI": {
                if (Bukkit.getPluginManager().getPlugin("EconomyShopGUI") != null || Bukkit.getPluginManager().getPlugin("EconomyShopGUI-Premium") != null) {
                    shopPrices = new EconomyShopGuiHook();
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into EconomyShopGUI!"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF3333[AxSellwands] EconomyShopGUI is set in hooks.yml, but it is not installed, please download it or change it to stop errors!"));
                }
                break;
            }

            case "DYNAMICSHOP": {
                if (Bukkit.getPluginManager().getPlugin("DynamicShop") != null) {
                    shopPrices = new DynamicShop3Hook();
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into DynamicShop!"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF3333[AxSellwands] DynamicShop is set in hooks.yml, but it is not installed, please download it or change it to stop errors!"));
                }
                break;
            }

            case "EXCELLENTSHOP": {
                if (Bukkit.getPluginManager().getPlugin("ExcellentShop") != null) {
                    shopPrices = new ExcellentShopHook();
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into ExcellentShop!"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF3333[AxSellwands] ExcellentShop is set in hooks.yml, but it is not installed, please download it or change it to stop errors!"));
                }
                break;
            }

            default: {
                shopPrices = new BuiltinPrices();
                Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Using builtin prices!"));
                break;
            }
        }
        if (shopPrices != null)
            shopPrices.setup();
        if (getShopPrices() == null) {
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF3333[AxSellwands] Shop prices hook not found! Please check your hooks.yml!"));
        }

        if (getCurrency() == null) {
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF3333[AxSellwands] Currency hook not found! Please check your hooks.yml!"));
        }
    }

    @SuppressWarnings("unused")
    public static void registerProtectionHook(@NotNull Plugin plugin, @NotNull ProtectionHook protectionHook) {
        PROTECTION_HOOKS.add(protectionHook);
        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into " + plugin.getName() + "!"));
    }

    @SuppressWarnings("unused")
    public static void registerPriceProviderHook(@NotNull Plugin plugin, @NotNull PricesHook pricesHook) {
        shopPrices = pricesHook;
        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into " + plugin.getName() + "! Note: You must set the price provider to CUSTOM or it will be overridden after reloading!"));
    }

    @SuppressWarnings("unused")
    public static void registerCurrencyHook(@NotNull Plugin plugin, @NotNull CurrencyHook currencyHook) {
        currency = currencyHook;
        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#33FF33[AxSellwands] Hooked into " + plugin.getName() + "! Note: You must set the currency provider to CUSTOM or it will be overridden after reloading!"));
    }

    @Nullable
    public static CurrencyHook getCurrency() {
        return currency;
    }

    @Nullable
    public static PricesHook getShopPrices() {
        return shopPrices;
    }

    @NotNull
    public static HashSet<ContainerHook> getContainerHooks() {
        return CONTAINER_HOOKS;
    }

    @NotNull
    public static HashSet<ProtectionHook> getProtectionHooks() {
        return PROTECTION_HOOKS;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean canBuildAt(@NotNull Player player, @NotNull Location location) {
        for (ProtectionHook hook : PROTECTION_HOOKS) {
            if (!hook.canPlayerBuildAt(player, location)) return false;
        }
        return true;
    }

    @Nullable
    public static ContainerHook getContainerAt(@NotNull Player player, @NotNull Block block) {
        for (ContainerHook hook : CONTAINER_HOOKS) {
            if (!hook.isContainer(player, block)) continue;
            return hook;
        }
        return null;
    }
}
