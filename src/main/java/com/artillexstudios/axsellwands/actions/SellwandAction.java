package com.artillexstudios.axsellwands.actions;

import com.artillexstudios.axapi.items.NBTWrapper;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.components.DataComponents;
import com.artillexstudios.axapi.libs.boostedyaml.block.implementation.Section;
import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.utils.ActionBar;
import com.artillexstudios.axapi.utils.ItemBuilder;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.Title;
import com.artillexstudios.axintegrations.types.ContainerIntegration;
import com.artillexstudios.axintegrations.types.CurrencyIntegration;
import com.artillexstudios.axintegrations.types.ProtectionIntegration;
import com.artillexstudios.axintegrations.types.ShopIntegration;
import com.artillexstudios.axsellwands.api.events.AxSellwandsSellEvent;
import com.artillexstudios.axsellwands.sellwands.Sellwand;
import com.artillexstudios.axsellwands.utils.HistoryUtils;
import com.artillexstudios.axsellwands.utils.HologramUtils;
import com.artillexstudios.axsellwands.utils.NumberUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.artillexstudios.axsellwands.AxSellwands.CONFIG;
import static com.artillexstudios.axsellwands.AxSellwands.LANG;
import static com.artillexstudios.axsellwands.AxSellwands.MESSAGEUTILS;

public class SellwandAction {
    private final ItemStack item;
    private final WrappedItemStack wrapped;
    private final Block block;
    private final Player player;
    private final NBTWrapper wrapper;
    private final Sellwand sellwand;
    private final ClickType clickType;
    private ShopIntegration shopIntegration;
    private CurrencyIntegration currencyIntegration;
    private ItemStack[] contents;
    private final Set<ActionType> actions = new HashSet<>();
    private UUID uuid;
    private float multiplier;
    private int uses;
    private int maxUses;
    private int soldAmount;
    private double soldPrice;
    private int processedAmount = 0;
    private double processedPrice = 0D;
    private final Map<Material, Integer> soldItems = new HashMap<>();

    public SellwandAction(ItemStack item, Block block, Player player, WrappedItemStack wrapped, NBTWrapper wrapper, Sellwand sellwand, ClickType clickType) {
        this.item = item;
        this.block = block;
        this.player = player;
        this.wrapped = wrapped;
        this.wrapper = wrapper;
        this.sellwand = sellwand;
        this.clickType = clickType;
    }

    public void build() {
        if (!hasPermission()) return;
        if (!checkShop()) return;
        if (!getContents()) return;
        if (!isAllowed()) return;
        if (!canUse()) return;
        readData();
        checkActions();

        boolean inspect = false;
        if (actions.contains(ActionType.INSPECT)) {
            inspect();
            inspect = true;
        }
        if (actions.contains(ActionType.SELL)) {
            if (inspect) {
                processedAmount = 0;
                processedPrice = 0D;
            }
            if (!checkCurrency()) return;
            sell();
        }
    }

    private void inspect() {
        for (ItemStack it : contents) {
            Double price = getPrice(it);
            if (price == null) continue;
            processedAmount += it.getAmount();
            processedPrice += price;
        }
        if (!anyProcessed()) return;
        print(ActionType.INSPECT);
    }

    private void sell() {
        for (ItemStack it : contents) {
            Double price = getPrice(it);
            if (price == null) continue;
            processedAmount += it.getAmount();
            processedPrice += price;

            int amount = soldItems.computeIfAbsent(it.getType(), k -> 0);
            soldItems.put(it.getType(), amount + it.getAmount());
            it.setAmount(0);
        }
        if (!anyProcessed()) return;
        callSellEvent();
        logSell();
        payPlayer();
        print(ActionType.SELL);
        if (updateUses()) {
            updateItem();
        }
        updateContainer();
    }

    private boolean hasPermission() {
        boolean hasBypass = player.hasPermission("axsellwands.admin");
        if (hasBypass) return true;
        if (!ProtectionIntegration.hasPermission(player, block.getLocation(), ProtectionIntegration.Permission.BREAK)) {
            MESSAGEUTILS.sendLang(player, "no-permission");
            return false;
        }
        return true;
    }

    private boolean checkShop() {
        shopIntegration = ShopIntegration.one();
        if (shopIntegration == null) {
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF0000[AxSellwands] No shop plugin selected in the hooks.yml!"));
            return false;
        }
        return true;
    }

    private boolean checkCurrency() {
        currencyIntegration = CurrencyIntegration.one();
        if (currencyIntegration == null) {
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF0000[AxSellwands] No currency plugin selected in the hooks.yml!"));
            return false;
        }
        return true;
    }

    private boolean getContents() {
        ContainerIntegration integration = ContainerIntegration.getContainerIntegration(block);
        if (integration != null) {
            contents = integration.getContents(block).toArray(new ItemStack[0]);
            return true;
        }

        if (block.getState() instanceof Container) {
            contents = ((Container) block.getState()).getInventory().getContents();
        } else if (block.getType() == Material.ENDER_CHEST) {
            contents = player.getEnderChest().getContents();
        } else {
            return false;
        }
        return true;
    }

    private boolean isAllowed() {
        if (sellwand.getDisallowed().contains(block.getType()) || (!sellwand.getAllowed().isEmpty() && !sellwand.getAllowed().contains(block.getType()))) {
            MESSAGEUTILS.sendLang(player, "disallowed-container");
            return false;
        }
        return true;
    }

    private boolean canUse() {
        Long lastUsed = wrapper.getLong("axsellwands-lastused");
        if (lastUsed == null) return true;
        if (clickType != ClickType.RIGHT) return true;
        if (System.currentTimeMillis() - lastUsed < sellwand.getCooldown()) {
            long cooldown = Duration.ofMillis(sellwand.getCooldown() - System.currentTimeMillis() + lastUsed).toSeconds();
            MESSAGEUTILS.sendLang(player, "cooldown", Map.of("%time%", String.valueOf(cooldown)));
            return false;
        }
        return true;
    }

    private void readData() {
        uuid = wrapper.getUUID("axsellwands-uuid");
        multiplier = wrapper.getFloatOr("axsellwands-multiplier", 1);
        uses = wrapper.getIntOr("axsellwands-uses", -1);
        maxUses = wrapper.getIntOr("axsellwands-max-uses", -1);
        soldAmount = wrapper.getIntOr("axsellwands-sold-amount", 0);
        soldPrice = wrapper.getDoubleOr("axsellwands-sold-price", 0);
    }

    private void checkActions() {
        Section section = CONFIG.getSection("actions");
        switch (clickType) {
            case LEFT -> {
                actions.add(ActionType.from(section.getString("left")));
            }
            case RIGHT -> {
                actions.add(ActionType.from(section.getString("right")));
            }
        }
    }

    private Double getPrice(ItemStack item) {
        if (item == null) return null;
        Double price = shopIntegration.getSellPrice(player.getUniqueId(), item);
        if (price == null || price <= 0) return null;
        price *= multiplier;
        return price;
    }

    private void callSellEvent() {
        AxSellwandsSellEvent apiEvent = new AxSellwandsSellEvent(player, processedPrice, processedAmount, soldItems);
        Bukkit.getPluginManager().callEvent(apiEvent);
        processedPrice = apiEvent.getMoneyMade();
    }

    private void logSell() {
        StringBuilder str = new StringBuilder("[");
        boolean first = true;
        for (Map.Entry<Material, Integer> e : soldItems.entrySet()) {
            if (!first) str.append(", ");
            first = false;
            str.append(e.getValue()).append("x ").append(e.getKey().name());
        }
        str.append("]");
        HistoryUtils.writeToHistory(String.format("%s sold %dx items %s and earned %s (multiplier: %s, uses: %d)", player.getName(), processedAmount, str, processedPrice, multiplier, uses - 1));
    }

    private void payPlayer() {
        currencyIntegration.giveBalance(player.getUniqueId(), processedPrice);
    }

    private boolean updateUses() {
        if (uses != -1) {
            uses--;

            if (uses < CONFIG.getInt("minimum-durability", 1)) {
                item.setAmount(0);
                return false;
            }
        }
        return true;
    }

    private void updateItem() {
        String unlimited = LANG.getString("unlimited", "∞");
        ItemBuilder builder = ItemBuilder.create(
                sellwand.getItemSection(),
                Placeholder.unparsed("multiplier", String.valueOf(multiplier)),
                Placeholder.unparsed("uses", String.valueOf(uses == -1 ? unlimited : uses)),
                Placeholder.unparsed("max-uses", String.valueOf(maxUses == -1 ? unlimited : maxUses)),
                Placeholder.unparsed("sold-amount", NumberUtils.formatNumber(soldAmount + processedAmount)),
                Placeholder.unparsed("sold-price", NumberUtils.formatNumber(soldPrice + processedPrice))
        );
        WrappedItemStack built = builder.wrapped();
        NBTWrapper wrapperNew = new NBTWrapper(built);
        wrapperNew.set("axsellwands-uuid", uuid);
        wrapperNew.set("axsellwands-uses", uses);
        wrapperNew.set("axsellwands-lastused", System.currentTimeMillis());
        wrapperNew.set("axsellwands-sold-amount", soldAmount + processedAmount);
        wrapperNew.set("axsellwands-sold-price", soldPrice + processedPrice);
        wrapperNew.set("axsellwands-type", sellwand.getId());
        wrapperNew.set("axsellwands-multiplier", multiplier);
        wrapperNew.set("axsellwands-max-uses", maxUses);
        wrapperNew.build();

        wrapped.set(DataComponents.CUSTOM_DATA, built.get(DataComponents.CUSTOM_DATA));
        wrapped.set(DataComponents.MATERIAL, built.get(DataComponents.MATERIAL));
        wrapped.set(DataComponents.CUSTOM_NAME, built.get(DataComponents.CUSTOM_NAME));
        wrapped.set(DataComponents.DYED_COLOR, built.get(DataComponents.DYED_COLOR));
        wrapped.set(DataComponents.CUSTOM_MODEL_DATA, built.get(DataComponents.CUSTOM_MODEL_DATA));
        wrapped.set(DataComponents.PROFILE, built.get(DataComponents.PROFILE));
        wrapped.set(DataComponents.LORE, built.get(DataComponents.LORE));
        wrapped.finishEdit();
    }

    private void updateContainer() {
        if (!(block.getState() instanceof Container container)) return;
        container.update();
    }

    private boolean anyProcessed() {
        if (processedAmount <= 0 || processedPrice <= 0) {
            MESSAGEUTILS.sendLang(player, "nothing-sold");
            return false;
        }
        return true;
    }

    private void print(ActionType actionType) {
        if (actionType == ActionType.SELL) {
            if (CONFIG.getBoolean("hologram.enabled", true)) {
                HologramUtils.spawnHologram(player, block.getLocation().clone().add(0.5, 0.5, 0.5), Map.of( // todo: migrate to components
                        "%amount%", NumberUtils.formatNumber(processedAmount),
                        "%price%", NumberUtils.formatNumber(processedPrice)
                ));
            }
        }

        {
            String text = LANG.getString("%s.chat".formatted(actionType.raw()));
            if (notBlank(text)) {
                Component message = StringUtils.format(
                        text,
                        Placeholder.unparsed("amount", NumberUtils.formatNumber(processedAmount)),
                        Placeholder.unparsed("price", NumberUtils.formatNumber(processedPrice))
                );
                ServerPlayerWrapper.wrap(player).message(StringUtils.format(CONFIG.getString("prefix")).append(message));
            }
        }

        {
            String text = LANG.getString("%s.actionbar".formatted(actionType.raw()));
            if (notBlank(text)) {
                Component message = StringUtils.format(
                        text,
                        Placeholder.unparsed("amount", NumberUtils.formatNumber(processedAmount)),
                        Placeholder.unparsed("price", NumberUtils.formatNumber(processedPrice))
                );
                ActionBar actionBar = ActionBar.create(message);
                actionBar.send(player);
            }
        }

        {
            Section titleSection = LANG.getSection("%s.title".formatted(actionType.raw()));
            if (titleSection != null) {
                String title = titleSection.getString("title");
                String subtitle = titleSection.getString("subtitle");
                if (notBlank(title)) {
                    Component titleComponent = StringUtils.format(
                            title,
                            Placeholder.unparsed("amount", NumberUtils.formatNumber(processedAmount)),
                            Placeholder.unparsed("price", NumberUtils.formatNumber(processedPrice))
                    );
                    Component subtitleComponent = notBlank(subtitle) ? StringUtils.format(
                            subtitle,
                            Placeholder.unparsed("amount", NumberUtils.formatNumber(processedAmount)),
                            Placeholder.unparsed("price", NumberUtils.formatNumber(processedPrice))
                    ) : null;
                    Title titleObj = new Title(
                            titleComponent,
                            subtitleComponent,
                            10, 40, 10
                    );
                    titleObj.send(player);
                }
            }
        }

        {
            String text = LANG.getString("sounds.%s".formatted(actionType.raw()));
            if (notBlank(text)) {
                player.playSound(player.getLocation(), Sound.valueOf(text), 1f, 1f);
            }
        }

        {
            String text = LANG.getString("particles.%s".formatted(actionType.raw()));
            if (notBlank(text)) {
                Location location = block.getLocation().clone().add(0.5, 0.5, 0.5);
                player.spawnParticle(Particle.valueOf(text), location, 30, 0.5, 0.5, 0.5);
            }
        }
    }

    private boolean notBlank(String text) {
        return text != null && !text.isBlank();
    }
}
