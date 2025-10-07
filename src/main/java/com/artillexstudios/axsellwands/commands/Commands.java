package com.artillexstudios.axsellwands.commands;

import com.artillexstudios.axsellwands.commands.subcommands.Give;
import com.artillexstudios.axsellwands.commands.subcommands.Help;
import com.artillexstudios.axsellwands.commands.subcommands.Reload;
import com.artillexstudios.axsellwands.sellwands.Sellwand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command({"axsellwands", "axsellwand", "sellwands", "sellwand"})
@CommandPermission("axsellwands.admin")
public class Commands {

    @DefaultFor({"~", "~ help"})
    public void help(@NotNull CommandSender sender) {
        Help.INSTANCE.execute(sender);
    }

    @Subcommand("give")
    public void give(@NotNull CommandSender sender, Player player, @NotNull Sellwand sellwand, @Optional @Range(min = 1, max = 64) Integer amount) {
        Give.INSTANCE.execute(sender, player, sellwand, amount);
    }

    @Subcommand("reload")
    public void reload(@NotNull CommandSender sender) {
        Reload.INSTANCE.execute(sender);
    }
}