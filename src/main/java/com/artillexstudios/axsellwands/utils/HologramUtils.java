package com.artillexstudios.axsellwands.utils;

import com.artillexstudios.axapi.hologram.Hologram;
import com.artillexstudios.axapi.hologram.HologramLine;
import com.artillexstudios.axapi.scheduler.ScheduledTask;
import com.artillexstudios.axapi.scheduler.Scheduler;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.placeholder.StaticPlaceholder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.artillexstudios.axsellwands.AxSellwands.CONFIG;
import static com.artillexstudios.axsellwands.AxSellwands.LANG;

public class HologramUtils {

    public static void spawnHologram(@NotNull Player player, @NotNull Location location, HashMap<String, String> replacements) {
        final Vector diff = location.toVector().subtract(player.getLocation().toVector());
        diff.subtract(diff.clone().normalize());
        location = player.getLocation().toVector().add(diff).toLocation(location.getWorld());

        final Hologram hologram = new Hologram(location, location.toString(), 0.3);

        hologram.addPlaceholder(new StaticPlaceholder((string) -> {
            AtomicReference<String> toFormat = new AtomicReference<>(string);
            replacements.forEach((pattern, replacement) -> {
                toFormat.set(toFormat.get().replace(pattern, replacement));
            });
            return toFormat.get();
        }));

        hologram.addLines(StringUtils.formatListToString(LANG.getStringList("sell-hologram")), HologramLine.Type.TEXT);

        Scheduler.get().runLaterAt(location, new Consumer<ScheduledTask>() {
            @Override
            public void accept(ScheduledTask scheduledTask) {
                hologram.remove();
            }
        }, CONFIG.getInt("hologram.stay-time-ticks", 30));
    }
}
