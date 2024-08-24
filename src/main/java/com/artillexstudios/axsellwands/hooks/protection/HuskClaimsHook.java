package com.artillexstudios.axsellwands.hooks.protection;

import net.william278.huskclaims.api.HuskClaimsAPI;
import net.william278.huskclaims.libraries.cloplib.operation.OperationType;
import net.william278.huskclaims.position.Position;
import net.william278.huskclaims.user.OnlineUser;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HuskClaimsHook implements ProtectionHook {

    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        var huskClaims = HuskClaimsAPI.getInstance();
        final OnlineUser user = huskClaims.getOnlineUser(player.getUniqueId());
        final Position position = huskClaims.getPosition(location.getX(), location.getY(), location.getZ(), location.getWorld().getName());
        return huskClaims.isOperationAllowed(user, OperationType.BLOCK_BREAK, position);
    }
}
