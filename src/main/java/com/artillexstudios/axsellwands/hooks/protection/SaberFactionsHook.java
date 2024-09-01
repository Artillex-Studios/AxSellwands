package com.artillexstudios.axsellwands.hooks.protection;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SaberFactionsHook implements ProtectionHook {

    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        final FLocation loc = FLocation.wrap(location);
        final FPlayer pl = FPlayers.getInstance().getByPlayer(player);
        return Board.getInstance().getFactionAt(loc).getAccess(pl, PermissableAction.BUILD) != Access.DENY;
    }
}
