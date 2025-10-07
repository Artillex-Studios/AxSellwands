package com.artillexstudios.axsellwands.commands.subcommands;

import com.artillexstudios.axapi.utils.StringUtils;
import org.bukkit.command.CommandSender;

import static com.artillexstudios.axsellwands.AxSellwands.LANG;

public enum Help {
    INSTANCE;

    public void execute(CommandSender sender) {
        for (String m : LANG.getStringList("help")) {
            sender.sendMessage(StringUtils.formatToString(m));
        }
    }
}
