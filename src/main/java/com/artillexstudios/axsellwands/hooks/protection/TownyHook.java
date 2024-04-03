package com.artillexstudios.axsellwands.hooks.protection;

import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TownyHook implements ProtectionHook {

    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        return PlayerCacheUtil.getCachePermission(player, location, Material.STONE, TownyPermission.ActionType.BUILD);
    }
}
