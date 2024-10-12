package com.artillexstudios.axsellwands.listeners;

import com.artillexstudios.axapi.utils.Cooldown;
import com.artillexstudios.axapi.utils.ItemBuilder;
import com.artillexstudios.axsellwands.sellwands.Sellwand;
import com.artillexstudios.axsellwands.sellwands.Sellwands;
import com.artillexstudios.axsellwands.utils.NBTUtils;
import com.artillexstudios.axsellwands.utils.NumberUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

import static com.artillexstudios.axsellwands.AxSellwands.CONFIG;
import static com.artillexstudios.axsellwands.AxSellwands.LANG;
import static com.artillexstudios.axsellwands.AxSellwands.MESSAGEUTILS;

public class InventoryClickListener implements Listener {
    private final Cooldown<Player> cooldown = new Cooldown<>();

    @EventHandler(ignoreCancelled = true)
    public void onClick(@NotNull InventoryClickEvent event) {
        if (CONFIG.getInt("stacking-mode", 1) != 1) return;
        final Player player = (Player) event.getWhoClicked();
        if (player.getGameMode() == GameMode.CREATIVE) return;
        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(player.getInventory())) return;
        if (event.getCursor() == null || event.getCursor().getType() == Material.AIR) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;


        final String type1 = NBTUtils.readStringFromNBT(event.getCurrentItem(), "axsellwands-type");
        final String type2 = NBTUtils.readStringFromNBT(event.getCursor(), "axsellwands-type");
        if (type1 == null || type2 == null) return;
        if (!type1.equals(type2)) {
            MESSAGEUTILS.sendLang(player, "stack.cant-stack");
            return;
        }

        if (event.getCursor().getAmount() > 1 || event.getCurrentItem().getAmount() > 1) {
            MESSAGEUTILS.sendLang(player, "stack.unstack-first");
            return;
        }

        int uses1 = NBTUtils.readIntegerFromNBT(event.getCurrentItem(), "axsellwands-uses");
        if (uses1 == -1) {
            MESSAGEUTILS.sendLang(player, "stack.cant-stack");
            return;
        }
        final UUID uuid1 = NBTUtils.readUUIDFromNBT(event.getCurrentItem(), "axsellwands-uuid");
        final float multiplier1 = NBTUtils.readFloatFromNBT(event.getCurrentItem(), "axsellwands-multiplier");
        final int maxUses1 = NBTUtils.readIntegerFromNBT(event.getCurrentItem(), "axsellwands-max-uses");
        final int soldAmount1 = NBTUtils.readIntegerFromNBT(event.getCurrentItem(), "axsellwands-sold-amount");
        final double soldPrice1 = NBTUtils.readDoubleFromNBT(event.getCurrentItem(), "axsellwands-sold-price");

        int uses2 = NBTUtils.readIntegerFromNBT(event.getCursor(), "axsellwands-uses");
        if (uses2 == -1) {
            MESSAGEUTILS.sendLang(player, "stack.cant-stack");
            return;
        }

        event.setCancelled(true);
        if (!cooldown.hasCooldown(player)) {
            MESSAGEUTILS.sendLang(player, "stack.confirm");
            cooldown.addCooldown(player, 3_000L);
            return;
        }

        final int maxUses2 = NBTUtils.readIntegerFromNBT(event.getCursor(), "axsellwands-max-uses");
        final int soldAmount2 = NBTUtils.readIntegerFromNBT(event.getCursor(), "axsellwands-sold-amount");
        final double soldPrice2 = NBTUtils.readDoubleFromNBT(event.getCursor(), "axsellwands-sold-price");

        int newMax = Math.max(maxUses1, maxUses2);
        int newUses = Math.min(CONFIG.getBoolean("allow-going-over-limit", false) ? Integer.MAX_VALUE : newMax, (uses1 + uses2));
        int newSoldAmount = soldAmount1 + soldAmount2;
        double newSoldPrice = soldPrice1 + soldPrice2;

        final HashMap<String, String> replacements = new HashMap<>();
        replacements.put("%multiplier%", "" + multiplier1);
        replacements.put("%uses%", "" + (newUses == -1 ? LANG.getString("unlimited", "∞") : newUses));
        replacements.put("%max-uses%", "" + (newMax == -1 ? LANG.getString("unlimited", "∞") : newMax));
        replacements.put("%sold-amount%", "" + (newSoldAmount));
        replacements.put("%sold-price%", NumberUtils.formatNumber(newSoldPrice));

        final Sellwand wand = Sellwands.getSellwands().get(type1);
        final ItemBuilder builder = new ItemBuilder(wand.getItemSection(), replacements);

        event.getCurrentItem().setItemMeta(builder.get().getItemMeta());

        NBTUtils.writeToNBT(event.getCurrentItem(), "axsellwands-uuid", uuid1);
        NBTUtils.writeToNBT(event.getCurrentItem(), "axsellwands-uses", newUses);
        NBTUtils.writeToNBT(event.getCurrentItem(), "axsellwands-max-uses", newMax);
        NBTUtils.writeToNBT(event.getCurrentItem(), "axsellwands-sold-amount", newSoldAmount);
        NBTUtils.writeToNBT(event.getCurrentItem(), "axsellwands-sold-price", newSoldPrice);
        NBTUtils.writeToNBT(event.getCurrentItem(), "axsellwands-lastused", System.currentTimeMillis());
        NBTUtils.writeToNBT(event.getCurrentItem(), "axsellwands-type", type1);
        NBTUtils.writeToNBT(event.getCurrentItem(), "axsellwands-multiplier", multiplier1);

        MESSAGEUTILS.sendLang(player, "stack.success");
        event.getCursor().setAmount(0);
    }
}
