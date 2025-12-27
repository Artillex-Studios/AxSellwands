package com.artillexstudios.axsellwands.hooks.protection;

import nl.rutgerkok.blocklocker.BlockLockerAPIv2;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BlockLockerHook implements ProtectionHook {

    @Override
    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        Block block = location.getBlock();
        Optional<OfflinePlayer> offlinePlayer = BlockLockerAPIv2.getOwner(block);
        return offlinePlayer.isPresent() && offlinePlayer.get().getUniqueId().equals(player.getUniqueId());
    }
}
