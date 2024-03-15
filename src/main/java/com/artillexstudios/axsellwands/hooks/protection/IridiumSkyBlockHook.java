package com.artillexstudios.axsellwands.hooks.protection;

import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumteams.PermissionType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class IridiumSkyBlockHook implements ProtectionHook {

    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        final @NotNull Optional<Island> is = IridiumSkyblockAPI.getInstance().getIslandViaLocation(location);

        return is.map(island -> IridiumSkyblockAPI.getInstance().getIslandPermission(island, IridiumSkyblockAPI.getInstance().getUser(player), PermissionType.BLOCK_PLACE)).orElse(true);
    }
}
