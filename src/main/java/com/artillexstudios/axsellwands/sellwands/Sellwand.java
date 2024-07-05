package com.artillexstudios.axsellwands.sellwands;

import com.artillexstudios.axapi.config.Config;
import com.artillexstudios.axapi.libs.boostedyaml.boostedyaml.block.implementation.Section;
import com.artillexstudios.axapi.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class Sellwand {
    private final String id;
    private final Config file;
    private final String name;
    private final float multiplier;
    private final int uses;
    private final long cooldown;
    private final Section itemSection;
    private final HashSet<Material> disallowed = new HashSet<>();
    private final HashSet<Material> allowed = new HashSet<>();

    public Sellwand(String id, @NotNull Config file) {
        this.id = id;
        this.file = file;

        long configMillis = file.getLong("cooldown-miliseconds", -1);
        if (configMillis != -1) {
            file.getBackingDocument().remove("cooldown-miliseconds");
            file.set("cooldown-milliseconds", configMillis);
            file.save();
        }

        this.name = file.getString("name", "Sellwand");
        this.multiplier = file.getFloat("multiplier", 1f);
        this.uses = file.getInt("uses", -1);
        this.cooldown = file.getLong("cooldown-milliseconds", 0);
        this.itemSection = file.getSection("item");

        for (String str : file.getStringList("disallowed-containers")) {
            final Material material = Material.getMaterial(str);
            if (material == null) {
                Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF5500[AxSellwands] Material " + str + " does not exists, skipping!"));
                continue;
            }
            disallowed.add(material);
        }

        for (String str : file.getStringList("allowed-containers")) {
            final Material material = Material.getMaterial(str);
            if (material == null) {
                Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF5500[AxSellwands] Material " + str + " does not exists, skipping!"));
                continue;
            }
            allowed.add(material);
        }
    }

    public String getId() {
        return id;
    }

    public Config getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public float getMultiplier() {
        return multiplier;
    }

    public int getUses() {
        return uses;
    }

    public long getCooldown() {
        return cooldown;
    }

    public Section getItemSection() {
        return itemSection;
    }

    public HashSet<Material> getDisallowed() {
        return disallowed;
    }

    public HashSet<Material> getAllowed() {
        return allowed;
    }
}
