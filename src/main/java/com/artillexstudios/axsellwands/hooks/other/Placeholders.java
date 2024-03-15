package com.artillexstudios.axsellwands.hooks.other;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {

    @NotNull
    @Override
    public String getAuthor() {
        return "ArtillexStudios";
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return "axsellwands";
    }

    @NotNull
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        final String[] args = params.split("_");


        return null;
    }
}