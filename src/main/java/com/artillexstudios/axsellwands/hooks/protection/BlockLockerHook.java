package com.artillexstudios.axsellwands.hooks.protection;

import nl.rutgerkok.blocklocker.BlockLockerAPI;
import nl.rutgerkok.blocklocker.BlockLockerAPIv2;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BlockLockerHook implements ProtectionHook {
    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        Block block = location.getBlock();
        return !BlockLockerAPIv2.isProtected(block) || BlockLockerAPI.isOwner(player, block);
    }
}
