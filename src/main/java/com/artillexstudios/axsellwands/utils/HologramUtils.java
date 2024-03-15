package com.artillexstudios.axsellwands.utils;

import com.artillexstudios.axapi.hologram.Hologram;
import com.artillexstudios.axapi.hologram.HologramFactory;
import com.artillexstudios.axapi.scheduler.ScheduledTask;
import com.artillexstudios.axapi.scheduler.Scheduler;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.placeholder.StaticPlaceholder;
import org.bukkit.Bukkit;
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
    private static Boolean supported = null;

    public static void spawnHologram(@NotNull Player player, @NotNull Location location, HashMap<String, String> replacements) {
        if (supported == null) {
            try {
                HologramFactory.get();
                supported = true;
            } catch (RuntimeException ex) {
                supported = false;
                Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF5500[AxSellwands] Holograms are only supported in 1.18+, so we will disable the feature!"));
            }
        }

        if (!supported) return;

        final Vector diff = location.toVector().subtract(player.getLocation().toVector());
        diff.subtract(diff.clone().normalize());
        location = player.getLocation().toVector().add(diff).toLocation(location.getWorld());

        final Hologram hologram = HologramFactory.get().spawnHologram(location, location.toString(), 0.3);

        hologram.addPlaceholder(new StaticPlaceholder((string) -> {
            AtomicReference<String> toFormat = new AtomicReference<>(string);
            replacements.forEach((pattern, replacement) -> {
                toFormat.set(toFormat.get().replace(pattern, replacement));
            });
            return toFormat.get();
        }));

        hologram.setLines(StringUtils.formatList(LANG.getStringList("sell-hologram")));

        Scheduler.get().runLaterAt(location, new Consumer<ScheduledTask>() {
            @Override
            public void accept(ScheduledTask scheduledTask) {
                hologram.remove();
            }
        }, CONFIG.getInt("hologram.stay-time-ticks", 30));
    }
}
