package com.artillexstudios.axsellwands.commands;

import com.artillexstudios.axapi.items.NBTWrapper;
import com.artillexstudios.axapi.utils.ContainerUtils;
import com.artillexstudios.axapi.utils.ItemBuilder;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axsellwands.hooks.HookManager;
import com.artillexstudios.axsellwands.sellwands.Sellwand;
import com.artillexstudios.axsellwands.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.artillexstudios.axsellwands.AxSellwands.CONFIG;
import static com.artillexstudios.axsellwands.AxSellwands.HOOKS;
import static com.artillexstudios.axsellwands.AxSellwands.LANG;
import static com.artillexstudios.axsellwands.AxSellwands.MESSAGEUTILS;

@Command({"axsellwands", "axsellwand", "sellwands", "sellwand"})
@CommandPermission("axsellwands.admin")
public class Commands {

    @DefaultFor({"~", "~ help"})
    public void help(@NotNull CommandSender sender) {
        for (String m : LANG.getStringList("help")) {
            sender.sendMessage(StringUtils.formatToString(m));
        }
    }

    @Subcommand("give") // todo: add optional overrides for uses, multiplier
    public void give(@NotNull CommandSender sender, Player player, @NotNull Sellwand sellwand, @Optional @Range(min = 1, max = 64) Integer amount, @Optional @Range(min = 1, max = 9999) Integer CustomUse) {
        float multiplier = sellwand.getMultiplier();
        int uses = sellwand.getUses();
        if(CustomUse!=null){
            uses = CustomUse;
        }

        Map<String, String> replacements = new HashMap<>();
        replacements.put("%multiplier%", "" + multiplier);
        replacements.put("%uses%", "" + (uses == -1 ? LANG.getString("unlimited", "∞") : uses));
        replacements.put("%max-uses%", "" + (uses == -1 ? LANG.getString("unlimited", "∞") : uses));
        replacements.put("%sold-amount%", "" + 0);
        replacements.put("%sold-price%", "" + 0);

        ItemBuilder builder = new ItemBuilder(sellwand.getItemSection(), replacements);
        ItemStack it = builder.get();

        NBTWrapper wrapper = new NBTWrapper(it);
        wrapper.set("axsellwands-type", sellwand.getId());
        wrapper.set("axsellwands-multiplier", multiplier);
        wrapper.set("axsellwands-lastused", 0L);
        wrapper.set("axsellwands-uses", uses);
        wrapper.set("axsellwands-max-uses", uses);
        wrapper.set("axsellwands-sold-amount", 0);
        wrapper.set("axsellwands-sold-price", 0D);

        int am = 1;
        if (amount != null) am = amount;

        for (int i = 0; i < am; i++) {
            if (CONFIG.getInt("stacking-mode", 0) != 2) wrapper.set("axsellwands-uuid", UUID.randomUUID());
            wrapper.build();
            ContainerUtils.INSTANCE.addOrDrop(player.getInventory(), List.of(it.clone()), player.getLocation());
        }

        replacements.put("%amount%", "" + am);
        replacements.put("%sellwand%", sellwand.getName());
        replacements.put("%player%", player.getName());

        MESSAGEUTILS.sendLang(sender, "sellwand-give", replacements);
        MESSAGEUTILS.sendLang(player, "sellwand-got", replacements);
    }

    @Subcommand("reload")
    public void reload(@NotNull CommandSender sender) {
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