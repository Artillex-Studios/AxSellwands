package com.artillexstudios.axsellwands.api.events;

import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class AxSellwandsSellEvent extends Event implements Cancellable {
    private static final HandlerList handlerList = new HandlerList();
    private final Player player;
    private double moneyMade;
    private final int itemsSold;
    private boolean isCancelled = false;
    private final Map<Material, Integer> soldItems;

    public AxSellwandsSellEvent(@NotNull Player player, double moneyMade, int itemsSold) {
        this(player, moneyMade, itemsSold, Collections.emptyMap());
    }

    public AxSellwandsSellEvent(@NotNull Player player, double moneyMade, int itemsSold, @NotNull Map<Material, Integer> soldItems) {
        super(!Bukkit.isPrimaryThread());

        this.player = player;
        this.moneyMade = moneyMade;
        this.itemsSold = itemsSold;
        this.soldItems = soldItems;
    }


    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    public double getMoneyMade() {
        return moneyMade;
    }

    public int getItemsSold() {
        return itemsSold;
    }

    @NotNull
    public Map<Material, Integer> getSoldItems() {
        return Collections.unmodifiableMap(soldItems);
    }

    public void setMoneyMade(double moneyMade) {
        this.moneyMade = moneyMade;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
}
