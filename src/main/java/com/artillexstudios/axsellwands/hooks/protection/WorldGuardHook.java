package com.artillexstudios.axsellwands.hooks.protection;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WorldGuardHook implements ProtectionHook {

    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        final com.sk89q.worldedit.util.Location location1 = BukkitAdapter.adapt(location);
        final LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        final RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
        final RegionQuery query = container.createQuery();
        final World world = BukkitAdapter.adapt(player.getWorld());

        final boolean canBypass = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, world);
        if (canBypass) return true;

        if (query.testState(location1, localPlayer, Flags.BUILD)) {
            return true;
        }
        return query.testState(location1, localPlayer, Flags.CHEST_ACCESS); // AxSellwands
    }
}
