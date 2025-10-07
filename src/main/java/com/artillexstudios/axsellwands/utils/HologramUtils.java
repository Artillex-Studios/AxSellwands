package com.artillexstudios.axsellwands.utils;

import com.artillexstudios.axapi.hologram.Hologram;
import com.artillexstudios.axapi.hologram.HologramType;
import com.artillexstudios.axapi.hologram.HologramTypes;
import com.artillexstudios.axapi.hologram.page.HologramPage;
import com.artillexstudios.axapi.libs.boostedyaml.block.implementation.Section;
import com.artillexstudios.axapi.packetentity.meta.entity.DisplayMeta;
import com.artillexstudios.axapi.packetentity.meta.entity.TextDisplayMeta;
import com.artillexstudios.axapi.scheduler.Scheduler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.artillexstudios.axsellwands.AxSellwands.CONFIG;
import static com.artillexstudios.axsellwands.AxSellwands.LANG;

public class HologramUtils {

    public static void spawnHologram(@NotNull Player player, @NotNull Location location, HashMap<String, String> replacements) {
        Vector diff = location.toVector().subtract(player.getLocation().toVector());
        diff.subtract(diff.clone().normalize());
        location = player.getLocation().toVector().add(diff).toLocation(location.getWorld());

        Hologram hologram = new Hologram(location);
        HologramPage<String, HologramType<String>> page = hologram.createPage(HologramTypes.TEXT);

        Section section = CONFIG.getSection("hologram");
        page.setEntityMetaHandler(m -> {
            TextDisplayMeta meta = (TextDisplayMeta) m;
            meta.seeThrough(section.getBoolean("see-through"));
            meta.alignment(TextDisplayMeta.Alignment.valueOf(section.getString("alignment").toUpperCase()));
            meta.backgroundColor(Integer.parseInt(section.getString("background-color"), 16));
            meta.lineWidth(1000);
            meta.billboardConstrain(DisplayMeta.BillboardConstrain.valueOf(section.getString("billboard").toUpperCase()));
        });

        List<String> lines = new ArrayList<>();
        for (String line : LANG.getStringList("sell-hologram")) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                line = line.replace(entry.getKey(), entry.getValue());
            }
            lines.add(line);
        }

        page.setContent(String.join("<reset><br>", lines));
        page.spawn();
        Scheduler.get().runLaterAt(location, scheduledTask -> hologram.remove(), CONFIG.getInt("hologram.stay-time-ticks", 30));
    }
}
