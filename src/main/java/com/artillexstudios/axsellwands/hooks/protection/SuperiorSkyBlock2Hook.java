package com.artillexstudios.axsellwands.hooks.protection;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SuperiorSkyBlock2Hook implements ProtectionHook {

    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        final SuperiorPlayer localPlayer = SuperiorSkyblockAPI.getPlayer(player.getUniqueId());

        final Island is = SuperiorSkyblockAPI.getIslandAt(location);
        if (is == null) return true;

        return is.isMember(localPlayer);
    }
}
