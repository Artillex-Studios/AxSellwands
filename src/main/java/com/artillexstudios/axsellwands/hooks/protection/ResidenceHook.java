package com.artillexstudios.axsellwands.hooks.protection;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ResidenceHook implements ProtectionHook {

    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        final ResidencePlayer rPlayer = Residence.getInstance().getPlayerManager().getResidencePlayer(player);
        return rPlayer.canBreakBlock(location.getBlock(), false);
    }
}
