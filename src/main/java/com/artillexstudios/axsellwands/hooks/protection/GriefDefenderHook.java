package com.artillexstudios.axsellwands.hooks.protection;

import com.griefdefender.api.GriefDefender;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GriefDefenderHook implements ProtectionHook {

    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        return GriefDefender.getCore().getUser(player.getUniqueId()).canBreak(location);
    }

}
