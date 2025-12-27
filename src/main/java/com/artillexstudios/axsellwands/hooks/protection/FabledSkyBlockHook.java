package com.artillexstudios.axsellwands.hooks.protection;

import com.songoda.skyblock.api.SkyBlockAPI;
import com.songoda.skyblock.api.island.Island;
import com.songoda.skyblock.permission.BasicPermission;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FabledSkyBlockHook implements ProtectionHook  {

    @Override
    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        Island IS = SkyBlockAPI.getIslandManager().getIslandAtLocation(location);
        if (IS == null) return true;
        BasicPermission destroyPermission = SkyBlockAPI.getImplementation().getPermissionManager().getPermission("Storage");
        return SkyBlockAPI.getImplementation().getPermissionManager().hasPermission(player,IS.getIsland(),destroyPermission);
    }

}
