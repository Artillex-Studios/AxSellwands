package com.artillexstudios.axsellwands.hooks.protection;

import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlotSquaredHook implements ProtectionHook {

    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        final com.plotsquared.core.location.Location plotLoc = com.plotsquared.core.location.Location.at(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        final PlotArea plotArea = PlotSquared.get().getPlotAreaManager().getPlotArea(plotLoc);
        if (plotArea == null) return true;
        final Plot plot = plotArea.getPlot(plotLoc);
        if (plot == null) return true;

        return plot.getMembers().contains(player.getUniqueId()) || plot.getOwners().contains(player.getUniqueId());
    }
}
