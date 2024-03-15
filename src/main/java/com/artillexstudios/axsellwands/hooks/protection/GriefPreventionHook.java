package com.artillexstudios.axsellwands.hooks.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GriefPreventionHook implements ProtectionHook {

    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        return me.ryanhamshire.GriefPrevention.GriefPrevention.instance.allowBuild(player, location) == null;
    }
}
