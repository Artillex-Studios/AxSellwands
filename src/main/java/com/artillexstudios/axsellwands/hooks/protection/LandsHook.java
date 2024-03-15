package com.artillexstudios.axsellwands.hooks.protection;

import com.artillexstudios.axsellwands.AxSellwands;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.flags.type.Flags;
import me.angeschossen.lands.api.land.LandWorld;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LandsHook implements ProtectionHook {
    private final LandsIntegration api;

    public LandsHook() {
        api = LandsIntegration.of(AxSellwands.getInstance());
    }

    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        final LandWorld world = api.getWorld(location.getWorld());

        if (world == null) return true;

        return world.hasRoleFlag(player.getUniqueId(), location, Flags.BLOCK_PLACE);
    }
}
