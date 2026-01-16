package com.artillexstudios.axsellwands.hooks.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kingdoms.constants.land.Land;
import org.kingdoms.constants.player.KingdomPlayer;

public class KingdomsXHook implements ProtectionHook {

    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        final KingdomPlayer localPlayer = KingdomPlayer.getKingdomPlayer(player.getUniqueId());
        final Land land = Land.getLand(location);

        if (land == null) return true;

        Kingdom kingdom = land.getKingdom();
        if (kingdom == null) return true;

        return kingdom.isMember(localPlayer);
    }
}
