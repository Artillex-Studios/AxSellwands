package com.artillexstudios.axsellwands.hooks.protection;

import mk.g360.realmcore.api.Realm;
import mk.g360.realmcore.api.RealmCoreAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RealmCoreHook implements ProtectionHook {
    
    private final RealmCoreAPI api = RealmCoreAPI.getInstance();
    
    @Override
    public boolean canPlayerBuildAt(@NotNull Player player, @NotNull Location location) {
        if (api == null) return true;
        
        Realm realm = api.getRealm(location);
        if (realm == null) return true;
        
        return realm.isMember(player);
    }
}
