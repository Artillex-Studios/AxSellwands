package com.artillexstudios.axsellwands.actions;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum ActionType {
    INSPECT,
    SELL,
    NONE;

    public String raw() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @NotNull
    public static ActionType from(String name) {
        if (name == null) return NONE;
        if (name.equalsIgnoreCase("inspect")) return INSPECT;
        if (name.equalsIgnoreCase("sell")) return SELL;
        return NONE;
    }
}
