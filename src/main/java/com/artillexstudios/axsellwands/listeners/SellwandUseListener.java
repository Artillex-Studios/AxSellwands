package com.artillexstudios.axsellwands.listeners;

import com.artillexstudios.axapi.items.NBTWrapper;
import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axsellwands.AxSellwands;
import com.artillexstudios.axsellwands.actions.ClickType;
import com.artillexstudios.axsellwands.actions.SellwandAction;
import com.artillexstudios.axsellwands.sellwands.Sellwand;
import com.artillexstudios.axsellwands.sellwands.Sellwands;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;

import static com.artillexstudios.axsellwands.AxSellwands.CONFIG;

public class SellwandUseListener implements Listener {

    public static void registerEvent() {
        SellwandUseListener useListener = new SellwandUseListener();
        String priority = CONFIG.getString("interact-listener-priority", "LOW");

        EventPriority eventPriority;
        try {
            eventPriority = EventPriority.valueOf(priority);
        } catch (IllegalArgumentException ex) {
            Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#FF0000[AxSellwands] Invalid event priority: &#FFAAAA%s".formatted(priority)));
            eventPriority = EventPriority.LOW;
        }

        EventExecutor executor = (listener, event) -> {
            if (listener instanceof SellwandUseListener && event instanceof PlayerInteractEvent useEvent) {
                useListener.onInteract(useEvent);
            }
        };

        AxSellwands.getInstance().getServer().getPluginManager().registerEvent(
                PlayerInteractEvent.class,
                useListener,
                eventPriority,
                executor,
                AxSellwands.getInstance(),
                false
        );
    }

    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        WrappedItemStack wrapped = WrappedItemStack.wrap(item);
        NBTWrapper wrapper = new NBTWrapper(wrapped);
        String type = wrapper.getString("axsellwands-type");
        if (type == null) return;
        Sellwand sellwand = Sellwands.getSellwands().get(type);
        event.setCancelled(true);
        if (sellwand == null) return;
        ClickType clickType = ClickType.from(event.getAction());
        if (clickType == null) return;
        Player player = event.getPlayer();

        SellwandAction action = new SellwandAction(
            item, block, player, wrapped, wrapper, sellwand, clickType
        );
        action.build();
    }
}
