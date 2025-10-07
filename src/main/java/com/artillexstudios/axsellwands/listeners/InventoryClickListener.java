package com.artillexstudios.axsellwands.listeners;

import com.artillexstudios.axapi.items.NBTWrapper;
import com.artillexstudios.axapi.utils.Cooldown;
import com.artillexstudios.axapi.utils.ItemBuilder;
import com.artillexstudios.axsellwands.sellwands.Sellwand;
import com.artillexstudios.axsellwands.sellwands.Sellwands;
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
    private final Cooldown<Player> cooldown = Cooldown.create();

    @EventHandler(ignoreCancelled = true)
    public void onClick(@NotNull InventoryClickEvent event) {
        if (CONFIG.getInt("stacking-mode", 1) != 1) return;
        Player player = (Player) event.getWhoClicked();
        if (player.getGameMode() == GameMode.CREATIVE) return;
        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(player.getInventory())) return;
        if (event.getCursor() == null || event.getCursor().getType() == Material.AIR) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        NBTWrapper wrapperCurrent = new NBTWrapper(event.getCurrentItem());
        NBTWrapper wrapperCursor = new NBTWrapper(event.getCursor());
        String type1 = wrapperCurrent.getString("axsellwands-type");
        String type2 = wrapperCursor.getString("axsellwands-type");
        if (type1 == null || type2 == null) return;
        if (!type1.equals(type2)) {
            MESSAGEUTILS.sendLang(player, "stack.cant-stack");
            return;
        }

        if (event.getCursor().getAmount() > 1 || event.getCurrentItem().getAmount() > 1) {
            MESSAGEUTILS.sendLang(player, "stack.unstack-first");
            return;
        }

        int uses1 = wrapperCurrent.getIntOr("axsellwands-uses", -1);
        if (uses1 == -1) {
            MESSAGEUTILS.sendLang(player, "stack.cant-stack");
            return;
        }

        UUID uuid1 = wrapperCurrent.getUUID("axsellwands-uuid");
        float multiplier1 = wrapperCurrent.getFloatOr("axsellwands-multiplier", 1);
        int maxUses1 = wrapperCurrent.getIntOr("axsellwands-max-uses", -1);
        int soldAmount1 = wrapperCurrent.getIntOr("axsellwands-sold-amount", 0);
        double soldPrice1 = wrapperCurrent.getDoubleOr("axsellwands-sold-price", 0);

        int uses2 = wrapperCursor.getIntOr("axsellwands-uses", -1);
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

        int maxUses2 = wrapperCursor.getIntOr("axsellwands-max-uses", -1);
        int soldAmount2 = wrapperCursor.getIntOr("axsellwands-sold-amount", 0);
        double soldPrice2 = wrapperCursor.getDoubleOr("axsellwands-sold-price", 0);

        int newMax = Math.max(maxUses1, maxUses2);
        int newUses = Math.min(CONFIG.getBoolean("allow-going-over-limit", false) ? Integer.MAX_VALUE : newMax, (uses1 + uses2));
        int newSoldAmount = soldAmount1 + soldAmount2;
        double newSoldPrice = soldPrice1 + soldPrice2;

        HashMap<String, String> replacements = new HashMap<>();
        replacements.put("%multiplier%", "" + multiplier1);
        replacements.put("%uses%", "" + (newUses == -1 ? LANG.getString("unlimited", "∞") : newUses));
        replacements.put("%max-uses%", "" + (newMax == -1 ? LANG.getString("unlimited", "∞") : newMax));
        replacements.put("%sold-amount%", "" + (newSoldAmount));
        replacements.put("%sold-price%", NumberUtils.formatNumber(newSoldPrice));

        Sellwand wand = Sellwands.getSellwands().get(type1);
        ItemBuilder builder = ItemBuilder.create(wand.getItemSection(), replacements);

        event.getCurrentItem().setItemMeta(builder.get().getItemMeta());

        NBTWrapper wrapper = new NBTWrapper(event.getCurrentItem());
        wrapper.set("axsellwands-uuid", uuid1);
        wrapper.set("axsellwands-uses", newUses);
        wrapper.set("axsellwands-max-uses", newMax);
        wrapper.set("axsellwands-sold-amount", newSoldAmount);
        wrapper.set("axsellwands-sold-price", newSoldPrice);
        wrapper.set("axsellwands-lastused", System.currentTimeMillis());
        wrapper.set("axsellwands-type", type1);
        wrapper.set("axsellwands-multiplier", multiplier1);
        wrapper.build();

        MESSAGEUTILS.sendLang(player, "stack.success");
        event.getCursor().setAmount(0);
    }
}
