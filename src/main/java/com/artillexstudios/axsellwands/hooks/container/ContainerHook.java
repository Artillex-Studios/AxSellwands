package com.artillexstudios.axsellwands.hooks.container;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ContainerHook {

    boolean isContainer(@NotNull Player player, @NotNull Block block);

    @NotNull
    List<ItemStack> getItems(@NotNull Player player, @NotNull Block block);
}
