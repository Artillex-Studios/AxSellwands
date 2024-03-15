package com.artillexstudios.axsellwands;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.config.Config;
import com.artillexstudios.axapi.data.ThreadedQueue;
import com.artillexstudios.axapi.libs.boostedyaml.boostedyaml.dvs.versioning.BasicVersioning;
import com.artillexstudios.axapi.libs.boostedyaml.boostedyaml.settings.dumper.DumperSettings;
import com.artillexstudios.axapi.libs.boostedyaml.boostedyaml.settings.general.GeneralSettings;
import com.artillexstudios.axapi.libs.boostedyaml.boostedyaml.settings.loader.LoaderSettings;
import com.artillexstudios.axapi.libs.boostedyaml.boostedyaml.settings.updater.UpdaterSettings;
import com.artillexstudios.axapi.utils.FeatureFlags;
import com.artillexstudios.axapi.utils.MessageUtils;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axsellwands.commands.Commands;
import com.artillexstudios.axsellwands.hooks.HookManager;
import com.artillexstudios.axsellwands.listeners.CraftListener;
import com.artillexstudios.axsellwands.listeners.InventoryClickListener;
import com.artillexstudios.axsellwands.listeners.SellwandUseListener;
import com.artillexstudios.axsellwands.sellwands.Sellwand;
import com.artillexstudios.axsellwands.sellwands.Sellwands;
import com.artillexstudios.axsellwands.utils.FileUtils;
import com.artillexstudios.axsellwands.utils.NumberUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class AxSellwands extends AxPlugin {
    public static Config CONFIG;
    public static Config LANG;
    public static Config HOOKS;
    public static MessageUtils MESSAGEUTILS;
    private static AxPlugin instance;
    private static ThreadedQueue<Runnable> threadedQueue;
    public static BukkitAudiences BUKKITAUDIENCES;

    public static ThreadedQueue<Runnable> getThreadedQueue() {
        return threadedQueue;
    }

    public static AxPlugin getInstance() {
        return instance;
    }

    public void enable() {
        instance = this;

        int pluginId = 21332;
        new Metrics(this, pluginId);

        CONFIG = new Config(new File(getDataFolder(), "config.yml"), getResource("config.yml"), GeneralSettings.builder().setUseDefaults(false).build(), LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setKeepAll(true).setVersioning(new BasicVersioning("version")).build());
        LANG = new Config(new File(getDataFolder(), "lang.yml"), getResource("lang.yml"), GeneralSettings.builder().setUseDefaults(false).build(), LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setKeepAll(true).setVersioning(new BasicVersioning("version")).build());
        HOOKS = new Config(new File(getDataFolder(), "hooks.yml"), getResource("hooks.yml"), GeneralSettings.builder().setUseDefaults(false).build(), LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setKeepAll(true).setVersioning(new BasicVersioning("version")).build());

        MESSAGEUTILS = new MessageUtils(LANG.getBackingDocument(), "prefix", CONFIG.getBackingDocument());

        threadedQueue = new ThreadedQueue<>("AxSellwands-Datastore-thread");

        BUKKITAUDIENCES = BukkitAudiences.create(this);

        HookManager.setupHooks();
        NumberUtils.reload();

//        switch (CONFIG.getString("database.type").toLowerCase()) {
//            case "sqlite" -> database = new SQLite();
//            case "mysql" -> database = new MySQL();
//            case "postgresql" -> database = new PostgreSQL();
//            default -> database = new H2();
//        }
//
//        database.setup();

        final BukkitCommandHandler handler = BukkitCommandHandler.create(this);

        handler.getAutoCompleter().registerSuggestionFactory(parameter -> {
            if (parameter.hasAnnotation(com.artillexstudios.axsellwands.commands.annotations.Sellwands.class)) {
                return (args, sender, command) -> {
                    final List<String> suggestions = new ArrayList<>();
                    Sellwands.getSellwands().forEach((id, sellwand) -> suggestions.add(id));

                    return suggestions;
                };
            }
            return null;
        });

        handler.registerValueResolver(Sellwand.class, resolver -> {
            final String str = resolver.popForParameter();
            return Sellwands.getSellwands().get(str);
        });

        handler.register(new Commands());

        if (FileUtils.PLUGIN_DIRECTORY.resolve("sellwands/").toFile().mkdirs()) {
            FileUtils.copyFromResource("sellwands");
        }

        Sellwands.reload();
        getServer().getPluginManager().registerEvents(new SellwandUseListener(), this);
        getServer().getPluginManager().registerEvents(new CraftListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);

        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF5500[AxSellwands] Loaded plugin!"));
    }

    public void updateFlags() {
        FeatureFlags.PACKET_ENTITY_TRACKER_ENABLED.set(true);
        FeatureFlags.HOLOGRAM_UPDATE_TICKS.set(20L);
    }
}