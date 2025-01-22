package com.artillexstudios.axsellwands.hooks.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.lists.Flags;

import java.util.Optional;

public class BentoBoxHook implements ProtectionHook {

    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        final Optional<Island> is = BentoBox.getInstance().getIslands().getIslandAt(location);

        return is.map(island -> island.isAllowed(BentoBox.getInstance().getPlayers().getUser(player.getUniqueId()), Flags.CHEST)).orElse(false);
    }
}
