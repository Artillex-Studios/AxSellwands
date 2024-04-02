package com.artillexstudios.axsellwands.hooks.protection;

import net.william278.husktowns.api.BukkitHuskTownsAPI;
import net.william278.husktowns.claim.Position;
import net.william278.husktowns.libraries.cloplib.operation.OperationType;
import net.william278.husktowns.user.OnlineUser;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HuskTownsHook implements ProtectionHook {

    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        var huskTowns = BukkitHuskTownsAPI.getInstance();
        final OnlineUser user = huskTowns.getOnlineUser(player);
        final Position position = huskTowns.getPosition(location);
        return huskTowns.isOperationAllowed(user, OperationType.BLOCK_BREAK, position);
    }
}
