package com.artillexstudios.axsellwands.utils;

import com.artillexstudios.axapi.items.WrappedItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NBTUtils {

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, @NotNull String content) {
        WrappedItemStack.edit(item, wrappedItemStack -> {
            wrappedItemStack.getCompoundTag().putString(namespace, content);
            return wrappedItemStack;
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, int content) {
        WrappedItemStack.edit(item, wrappedItemStack -> {
            wrappedItemStack.getCompoundTag().putInt(namespace, content);
            return wrappedItemStack;
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, boolean content) {
        WrappedItemStack.edit(item, wrappedItemStack -> {
            wrappedItemStack.getCompoundTag().putBoolean(namespace, content);
            return wrappedItemStack;
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, double content) {
        WrappedItemStack.edit(item, wrappedItemStack -> {
            wrappedItemStack.getCompoundTag().putDouble(namespace, content);
            return wrappedItemStack;
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, long content) {
        WrappedItemStack.edit(item, wrappedItemStack -> {
            wrappedItemStack.getCompoundTag().putLong(namespace, content);
            return wrappedItemStack;
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, float content) {
        WrappedItemStack.edit(item, wrappedItemStack -> {
            wrappedItemStack.getCompoundTag().putFloat(namespace, content);
            return wrappedItemStack;
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, UUID content) {
        WrappedItemStack.edit(item, wrappedItemStack -> {
            wrappedItemStack.getCompoundTag().putUUID(namespace, content);
            return wrappedItemStack;
        });
    }

    @Nullable
    public static String readStringFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            final String str = wrappedItemStack.getCompoundTag().getString(namespace);
            return str.isEmpty() ? null : str;
        });
    }

    public static int readIntegerFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            return wrappedItemStack.getCompoundTag().getInt(namespace);
        });
    }

    public static float readFloatFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            return wrappedItemStack.getCompoundTag().getFloat(namespace);
        });
    }

    public static long readLongFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            return wrappedItemStack.getCompoundTag().getLong(namespace);
        });
    }

    public static double readDoubleFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            return wrappedItemStack.getCompoundTag().getDouble(namespace);
        });
    }

    @Nullable
    public static UUID readUUIDFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            return wrappedItemStack.getCompoundTag().getUUID(namespace);
        });
    }

    public static boolean readBooleanFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            return wrappedItemStack.getCompoundTag().getBoolean(namespace);
        });
    }
}
