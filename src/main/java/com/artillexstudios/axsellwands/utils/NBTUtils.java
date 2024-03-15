package com.artillexstudios.axsellwands.utils;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NBTUtils {

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, @NotNull String content) {
        NBT.modify(item, nbt -> {
            nbt.setString(namespace, content);
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, int content) {
        NBT.modify(item, nbt -> {
            nbt.setInteger(namespace, content);
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, boolean content) {
        NBT.modify(item, nbt -> {
            nbt.setBoolean(namespace, content);
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, double content) {
        NBT.modify(item, nbt -> {
            nbt.setDouble(namespace, content);
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, long content) {
        NBT.modify(item, nbt -> {
            nbt.setLong(namespace, content);
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, float content) {
        NBT.modify(item, nbt -> {
            nbt.setFloat(namespace, content);
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, UUID content) {
        NBT.modify(item, nbt -> {
            nbt.setUUID(namespace, content);
        });
    }

    @Nullable
    public static String readStringFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        final String str = NBT.get(item, nbti -> (String) nbti.getString(namespace));
        return str.isEmpty() ? null : str;
    }

    public static int readIntegerFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return NBT.get(item, nbti -> (Integer) nbti.getInteger(namespace));
    }

    public static float readFloatFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return NBT.get(item, nbti -> (Float) nbti.getFloat(namespace));
    }

    public static long readLongFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return NBT.get(item, nbti -> (Long) nbti.getLong(namespace));
    }

    public static double readDoubleFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return NBT.get(item, nbti -> (Double) nbti.getDouble(namespace));
    }

    @Nullable
    public static UUID readUUIDFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return NBT.get(item, nbti -> (UUID) nbti.getUUID(namespace));
    }

    public static boolean readBooleanFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return NBT.get(item, nbti -> (Boolean) nbti.getBoolean(namespace));
    }
}
