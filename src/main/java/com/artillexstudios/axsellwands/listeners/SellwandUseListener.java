package com.artillexstudios.axsellwands.listeners;

import com.artillexstudios.axapi.utils.ActionBar;
import com.artillexstudios.axapi.utils.ItemBuilder;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.Title;
import com.artillexstudios.axsellwands.api.events.AxSellwandsSellEvent;
import com.artillexstudios.axsellwands.hooks.HookManager;
import com.artillexstudios.axsellwands.sellwands.Sellwand;
import com.artillexstudios.axsellwands.sellwands.Sellwands;
import com.artillexstudios.axsellwands.utils.HistoryUtils;
import com.artillexstudios.axsellwands.utils.HologramUtils;
import com.artillexstudios.axsellwands.utils.NBTUtils;
import com.artillexstudios.axsellwands.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.artillexstudios.axsellwands.AxSellwands.CONFIG;
import static com.artillexstudios.axsellwands.AxSellwands.LANG;
import static com.artillexstudios.axsellwands.AxSellwands.MESSAGEUTILS;

public class SellwandUseListener implements Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInteract(@NotNull PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        final Block block = event.getClickedBlock();
        if (block == null) return;
        final String type = NBTUtils.readStringFromNBT(event.getItem(), "axsellwands-type");
        if (type == null) return;
        final Sellwand sellwand = Sellwands.getSellwands().get(type);
        event.setCancelled(true);
        if (sellwand == null) return;
        final Player player = event.getPlayer();

        final ItemStack[] contents;
        if (block.getState() instanceof Container)
            contents = ((Container) block.getState()).getInventory().getContents();
        else if (block.getType() == Material.ENDER_CHEST)
            contents = player.getEnderChest().getContents();
        else return;

        final boolean hasBypass = player.hasPermission("axsellwands.admin");

        if (!hasBypass && !HookManager.canBuildAt(player, block.getLocation())) {
            MESSAGEUTILS.sendLang(player, "no-permission");
            return;
        }

        if (sellwand.getDisallowed().contains(block.getType()) || (!sellwand.getAllowed().isEmpty() && !sellwand.getAllowed().contains(block.getType()))) {
            MESSAGEUTILS.sendLang(player, "disallowed-container");
            return;
        }

        long lastUsed = NBTUtils.readLongFromNBT(event.getItem(), "axsellwands-lastused");

        if (System.currentTimeMillis() - lastUsed < sellwand.getCooldown() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            MESSAGEUTILS.sendLang(player, "cooldown", Collections.singletonMap("%time%", Long.toString(Math.round((sellwand.getCooldown() - System.currentTimeMillis() + lastUsed) / 1000D))));
            return;
        }

        final UUID uuid = NBTUtils.readUUIDFromNBT(event.getItem(), "axsellwands-uuid");
        final float multiplier = NBTUtils.readFloatFromNBT(event.getItem(), "axsellwands-multiplier");
        int uses = NBTUtils.readIntegerFromNBT(event.getItem(), "axsellwands-uses");
        final int maxUses = NBTUtils.readIntegerFromNBT(event.getItem(), "axsellwands-max-uses");
        final int soldAmount = NBTUtils.readIntegerFromNBT(event.getItem(), "axsellwands-sold-amount");
        final double soldPrice = NBTUtils.readDoubleFromNBT(event.getItem(), "axsellwands-sold-price");

        int newSoldAmount = 0;
        double newSoldPrice = 0;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Map<Material, Integer> items = new HashMap<>();
            for (ItemStack it : contents) {
                if (it == null) continue;
                double price = HookManager.getShopPrices().getPrice(player, it);
                if (price <= 0) continue;
                price *= multiplier;

                newSoldPrice += price;
                newSoldAmount += it.getAmount();

                if (items.containsKey(it.getType()))
                    items.put(it.getType(), items.get(it.getType()) + it.getAmount());
                else
                    items.put(it.getType(), it.getAmount());

                it.setAmount(0);
            }

            if (newSoldAmount == 0 || newSoldPrice == 0) {
                MESSAGEUTILS.sendLang(player, "nothing-sold");
                return;
            }

            final AxSellwandsSellEvent apiEvent = new AxSellwandsSellEvent(player, newSoldPrice, newSoldAmount);
            Bukkit.getPluginManager().callEvent(apiEvent);
            if (apiEvent.isCancelled()) return;
            newSoldPrice = apiEvent.getMoneyMade();

            StringBuilder str = new StringBuilder("[");
            boolean first = true;
            for (Map.Entry<Material, Integer> e : items.entrySet()) {
                if (!first) str.append(", ");
                first = false;
                str.append(e.getValue()).append("x ").append(e.getKey().name());
            }
            str.append("]");
            HistoryUtils.writeToHistory(String.format("%s sold %dx items %s and earned %s (multiplier: %s, uses: %d)", player.getName(), newSoldAmount, str, newSoldPrice, multiplier, uses - 1));

            final HashMap<String, String> replacements = new HashMap<>();
            replacements.put("%amount%", "" + newSoldAmount);
            replacements.put("%price%", NumberUtils.formatNumber(newSoldPrice));

            HookManager.getCurrency().giveBalance(player, newSoldPrice);

            if (CONFIG.getBoolean("hologram.enabled", true))
                HologramUtils.spawnHologram(player, block.getLocation().add(0.5, 0.5, 0.5), replacements);

            MESSAGEUTILS.sendLang(player, "sell.chat", replacements);

            if (!LANG.getString("sell.actionbar", "").isBlank()) {
                ActionBar.create(StringUtils.format(LANG.getString("sell.actionbar"), replacements)).send(player);
            }

            if (LANG.getSection("sell.title") != null && !LANG.getString("sell.title.title").isBlank())
                Title.create(StringUtils.format(LANG.getString("sell.title.title"), replacements),
                        StringUtils.format(LANG.getString("sell.title.subtitle"), replacements), 10, 40, 10).send(player);


            if (!LANG.getString("sounds.sell").isEmpty()) {
                player.playSound(player.getLocation(), Sound.valueOf(LANG.getString("sounds.sell")), 1f, 1f);
            }

            if (!LANG.getString("particles.sell").isEmpty()) {
                player.spawnParticle(Particle.valueOf(LANG.getString("particles.sell")), block.getLocation().add(0.5, 0.5, 0.5), 30, 0.5, 0.5, 0.5);
            }

            if (uses != -1) {
                uses--;

                if (uses < 0) {
                    event.getItem().setAmount(0);
                    return;
                }
            }

            replacements.clear();
            replacements.put("%multiplier%", "" + multiplier);
            replacements.put("%uses%", "" + (uses == -1 ? LANG.getString("unlimited", "∞") : uses));
            replacements.put("%max-uses%", "" + (maxUses == -1 ? LANG.getString("unlimited", "∞") : maxUses));
            replacements.put("%sold-amount%", "" + (soldAmount + newSoldAmount));
            replacements.put("%sold-price%", NumberUtils.formatNumber(soldPrice + newSoldPrice));

            final Sellwand wand = Sellwands.getSellwands().get(type);
            final ItemBuilder builder = new ItemBuilder(wand.getItemSection(), replacements);

            event.getItem().setItemMeta(builder.get().getItemMeta());

            NBTUtils.writeToNBT(event.getItem(), "axsellwands-uuid", uuid);
            NBTUtils.writeToNBT(event.getItem(), "axsellwands-uses", uses);
            NBTUtils.writeToNBT(event.getItem(), "axsellwands-lastused", System.currentTimeMillis());
            NBTUtils.writeToNBT(event.getItem(), "axsellwands-sold-amount", soldAmount + newSoldAmount);
            NBTUtils.writeToNBT(event.getItem(), "axsellwands-sold-price", soldPrice + newSoldPrice);
            NBTUtils.writeToNBT(event.getItem(), "axsellwands-type", type);
            NBTUtils.writeToNBT(event.getItem(), "axsellwands-multiplier", multiplier);
            NBTUtils.writeToNBT(event.getItem(), "axsellwands-max-uses", maxUses);
        } else {
            for (ItemStack it : contents) {
                if (it == null) continue;
                double price = HookManager.getShopPrices().getPrice(player, it);
                if (price == -1.0D) continue;
                price *= multiplier;

                newSoldPrice += price;
                newSoldAmount += it.getAmount();
            }

            final HashMap<String, String> replacements = new HashMap<>();
            replacements.put("%amount%", "" + newSoldAmount);
            replacements.put("%price%", NumberUtils.formatNumber(newSoldPrice));

            MESSAGEUTILS.sendLang(player, "inspect.chat", replacements);

            if (!LANG.getString("inspect.actionbar", "").isBlank()) {
                ActionBar.create(StringUtils.format(LANG.getString("inspect.actionbar"), replacements)).send(player);
            }

            if (LANG.getSection("inspect.title") != null && !LANG.getString("inspect.title.title").isBlank())
                Title.create(StringUtils.format(LANG.getString("inspect.title.title"), replacements),
                        StringUtils.format(LANG.getString("inspect.title.subtitle"), replacements), 10, 40, 10).send(player);


            if (!LANG.getString("sounds.inspect").isEmpty()) {
                player.playSound(player.getLocation(), Sound.valueOf(LANG.getString("sounds.inspect")), 1f, 1f);
            }

            if (!LANG.getString("particles.inspect").isEmpty()) {
                player.spawnParticle(Particle.valueOf(LANG.getString("particles.inspect")), block.getLocation().add(0.5, 0.5, 0.5), 30, 0.5, 0.5, 0.5);
            }
        }
    }
}
