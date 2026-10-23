package com.artillexstudios.axsellwands.api.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.Map;

public class AxSellwandsSellEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private final Player player;
    private double moneyMade;
    private final int itemsSold;
    private final Map<Material, Integer> soldItems;

    public AxSellwandsSellEvent(@NotNull Player player, double moneyMade, int itemsSold, Map<Material, Integer> soldItems) {
        super(!Bukkit.isPrimaryThread());

        this.player = player;
        this.moneyMade = moneyMade;
        this.itemsSold = itemsSold;
        this.soldItems = Collections.unmodifiableMap(soldItems);
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

    public void setMoneyMade(double moneyMade) {
        this.moneyMade = moneyMade;
    }

    @Unmodifiable
    public Map<Material, Integer> getSoldItems() {
        return soldItems;
    }
}
