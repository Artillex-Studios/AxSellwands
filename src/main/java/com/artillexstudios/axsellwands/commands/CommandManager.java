package com.artillexstudios.axsellwands.commands;

import com.artillexstudios.axsellwands.AxSellwands;
import com.artillexstudios.axsellwands.sellwands.Sellwand;
import com.artillexstudios.axsellwands.sellwands.Sellwands;
import com.artillexstudios.axsellwands.utils.CommandMessages;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import revxrsal.commands.exception.CommandErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandManager {
    private static BukkitCommandHandler handler = null;

    public static void load() {
        handler = BukkitCommandHandler.create(AxSellwands.getInstance());

        handler.getTranslator().add(new CommandMessages());
        handler.setLocale(Locale.of("en", "US"));

        handler.getAutoCompleter().registerParameterSuggestions(Sellwand.class, (args, sender, command) -> {
            List<String> suggestions = new ArrayList<>();
            Sellwands.getSellwands().forEach((id, sellwand) -> suggestions.add(id));
            if (!suggestions.isEmpty()) return suggestions;
            sender.error("There are no sellwands configured! If this is your first install, contact support or copy files from out github resources!");
            return suggestions;
        });

        handler.registerValueResolver(Sellwand.class, resolver -> {
            final String str = resolver.popForParameter();
            if (Sellwands.getSellwands().containsKey(str)) return Sellwands.getSellwands().get(str);
            throw new CommandErrorException("invalid-command", str);
        });

        reload();
    }

    public static void reload() {
        handler.unregisterAllCommands();

        handler.register(new Commands());

        handler.registerBrigadier();
    }
}
