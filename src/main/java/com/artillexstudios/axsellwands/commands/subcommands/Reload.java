package com.artillexstudios.axsellwands.commands.subcommands;

import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axsellwands.hooks.HookManager;
import com.artillexstudios.axsellwands.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Map;

import static com.artillexstudios.axsellwands.AxSellwands.CONFIG;
import static com.artillexstudios.axsellwands.AxSellwands.HOOKS;
import static com.artillexstudios.axsellwands.AxSellwands.LANG;
import static com.artillexstudios.axsellwands.AxSellwands.MESSAGEUTILS;

public enum Reload {
    INSTANCE;

    public void execute(CommandSender sender) {
        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF5500[AxSellwands] &#FFAAAAReloading configuration..."));
        if (!CONFIG.reload()) {
            MESSAGEUTILS.sendFormatted(sender, "reload.failed", Map.of("%file%", "config.yml"));
            return;
        }
        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF5500╠ &#FFEEAAReloaded &fconfig.yml&#FFEEAA!"));

        if (!LANG.reload()) {
            MESSAGEUTILS.sendFormatted(sender, "reload.failed", Map.of("%file%", "lang.yml"));
            return;
        }
        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF5500╠ &#FFEEAAReloaded &flang.yml&#FFEEAA!"));

        if (!HOOKS.reload()) {
            MESSAGEUTILS.sendFormatted(sender, "reload.failed", Map.of("%file%", "hooks.yml"));
            return;
        }
        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF5500╠ &#FFEEAAReloaded &fhooks.yml&#FFEEAA!"));

        com.artillexstudios.axsellwands.sellwands.Sellwands.reload();
        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF5500╠ &#FFEEAALoaded &f" + com.artillexstudios.axsellwands.sellwands.Sellwands.getSellwands().size() + " &#FFEEAAsellwands!"));

        HookManager.updateHooks();
        NumberUtils.reload();

        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF5500╚ &#FFEEAASuccessful reload!"));
        MESSAGEUTILS.sendLang(sender, "reload.success");
    }
}
