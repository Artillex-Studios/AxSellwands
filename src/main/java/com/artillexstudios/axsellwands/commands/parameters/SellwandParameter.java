package com.artillexstudios.axsellwands.commands.parameters;

import com.artillexstudios.axsellwands.sellwands.Sellwand;
import com.artillexstudios.axsellwands.sellwands.Sellwands;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

public class SellwandParameter implements ParameterType<BukkitCommandActor, Sellwand>  {

    @Override
    public Sellwand parse(@NotNull MutableStringStream input, @NotNull ExecutionContext<BukkitCommandActor> context) {
        String str = input.readString();
        if (Sellwands.getSellwands().containsKey(str)) return Sellwands.getSellwands().get(str);
        throw new CommandErrorException("Can't find sellwand!");
    }

    @NotNull
    @Override
    public SuggestionProvider<BukkitCommandActor> defaultSuggestions() {
        return context -> Sellwands.getSellwands().keySet();
    }
}
