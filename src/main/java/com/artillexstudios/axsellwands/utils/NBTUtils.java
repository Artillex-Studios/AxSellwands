package com.artillexstudios.axsellwands.utils;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.component.DataComponent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NBTUtils {

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, @NotNull String content) {
        WrappedItemStack.edit(item, wrappedItemStack -> {
            var customData = wrappedItemStack.get(DataComponent.CUSTOM_DATA);
            customData.putString(namespace, content);
            wrappedItemStack.set(DataComponent.CUSTOM_DATA, customData);
            return wrappedItemStack;
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, int content) {
        WrappedItemStack.edit(item, wrappedItemStack -> {
            var customData = wrappedItemStack.get(DataComponent.CUSTOM_DATA);
            customData.putInt(namespace, content);
            wrappedItemStack.set(DataComponent.CUSTOM_DATA, customData);
            return wrappedItemStack;
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, boolean content) {
        WrappedItemStack.edit(item, wrappedItemStack -> {
            var customData = wrappedItemStack.get(DataComponent.CUSTOM_DATA);
            customData.putBoolean(namespace, content);
            wrappedItemStack.set(DataComponent.CUSTOM_DATA, customData);
            return wrappedItemStack;
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, double content) {
        WrappedItemStack.edit(item, wrappedItemStack -> {
            var customData = wrappedItemStack.get(DataComponent.CUSTOM_DATA);
            customData.putDouble(namespace, content);
            wrappedItemStack.set(DataComponent.CUSTOM_DATA, customData);
            return wrappedItemStack;
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, long content) {
        WrappedItemStack.edit(item, wrappedItemStack -> {
            var customData = wrappedItemStack.get(DataComponent.CUSTOM_DATA);
            customData.putLong(namespace, content);
            wrappedItemStack.set(DataComponent.CUSTOM_DATA, customData);
            return wrappedItemStack;
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, float content) {
        WrappedItemStack.edit(item, wrappedItemStack -> {
            var customData = wrappedItemStack.get(DataComponent.CUSTOM_DATA);
            customData.putFloat(namespace, content);
            wrappedItemStack.set(DataComponent.CUSTOM_DATA, customData);
            return wrappedItemStack;
        });
    }

    public static void writeToNBT(@NotNull ItemStack item, @NotNull String namespace, UUID content) {
        WrappedItemStack.edit(item, wrappedItemStack -> {
            var customData = wrappedItemStack.get(DataComponent.CUSTOM_DATA);
            customData.putUUID(namespace, content);
            wrappedItemStack.set(DataComponent.CUSTOM_DATA, customData);
            return wrappedItemStack;
        });
    }

    public static boolean containsNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            return wrappedItemStack.get(DataComponent.CUSTOM_DATA).contains(namespace);
        });
    }

    @Nullable
    public static String readStringFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            final String str = wrappedItemStack.get(DataComponent.CUSTOM_DATA).getString(namespace);
            return str.isEmpty() ? null : str;
        });
    }

    public static int readIntegerFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            return wrappedItemStack.get(DataComponent.CUSTOM_DATA).getInt(namespace);
        });
    }

    public static float readFloatFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            return wrappedItemStack.get(DataComponent.CUSTOM_DATA).getFloat(namespace);
        });
    }

    public static long readLongFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            return wrappedItemStack.get(DataComponent.CUSTOM_DATA).getLong(namespace);
        });
    }

    public static double readDoubleFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            return wrappedItemStack.get(DataComponent.CUSTOM_DATA).getDouble(namespace);
        });
    }

    @Nullable
    public static UUID readUUIDFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            return wrappedItemStack.get(DataComponent.CUSTOM_DATA).getUUID(namespace);
        });
    }

    public static boolean readBooleanFromNBT(@NotNull ItemStack item, @NotNull String namespace) {
        return WrappedItemStack.edit(item, wrappedItemStack -> {
            return wrappedItemStack.get(DataComponent.CUSTOM_DATA).getBoolean(namespace);
        });
    }
}
