package com.artillexstudios.axsellwands.commands;

import com.artillexstudios.axsellwands.AxSellwands;
import com.artillexstudios.axsellwands.commands.parameters.SellwandParameter;
import com.artillexstudios.axsellwands.sellwands.Sellwand;
import com.artillexstudios.axsellwands.utils.CommandMessages;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public class CommandManager {
    private static Lamp<BukkitCommandActor> handler = null;

    public static void load() {
        Lamp.Builder<BukkitCommandActor> builder = BukkitLamp.builder(AxSellwands.getInstance());

        builder.parameterTypes(parameterTypes -> {
            parameterTypes.addParameterType(Sellwand.class, new SellwandParameter());
        });

        builder.exceptionHandler(new CommandMessages());

        handler = builder.build();

        reload();
    }

    public static void reload() {
        handler.unregisterAllCommands();

        handler.register(new Commands());
    }
}
