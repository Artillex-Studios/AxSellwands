package com.artillexstudios.axsellwands.hooks.protection;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.User;
import com.griefdefender.api.claim.Claim;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GriefDefenderHook implements ProtectionHook{
    @Override
    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        return Objects.requireNonNull(GriefDefender.getCore().getUser(player.getUniqueId())).canBreak(location);
    }
}
