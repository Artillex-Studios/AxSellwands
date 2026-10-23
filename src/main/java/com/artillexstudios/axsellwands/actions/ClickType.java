package com.artillexstudios.axsellwands.actions;

import org.bukkit.event.block.Action;
import org.jetbrains.annotations.Nullable;

public enum ClickType {
    LEFT,
    RIGHT;

    @Nullable
    public static ClickType from(Action action) {
        return switch (action) {
            case LEFT_CLICK_BLOCK -> LEFT;
            case RIGHT_CLICK_BLOCK -> RIGHT;
            default -> null;
        };
    }
}
