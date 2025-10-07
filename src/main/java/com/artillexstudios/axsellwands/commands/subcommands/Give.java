package com.artillexstudios.axsellwands.commands.subcommands;

import com.artillexstudios.axapi.items.NBTWrapper;
import com.artillexstudios.axapi.utils.ContainerUtils;
import com.artillexstudios.axapi.utils.ItemBuilder;
import com.artillexstudios.axsellwands.sellwands.Sellwand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.artillexstudios.axsellwands.AxSellwands.CONFIG;
import static com.artillexstudios.axsellwands.AxSellwands.LANG;
import static com.artillexstudios.axsellwands.AxSellwands.MESSAGEUTILS;

public enum Give {
    INSTANCE;

    public void execute(CommandSender sender, Player player, @NotNull Sellwand sellwand, @Nullable Integer amount) {
        float multiplier = sellwand.getMultiplier();
        int uses = sellwand.getUses();

        Map<String, String> replacements = new HashMap<>();
        replacements.put("%multiplier%", "" + multiplier);
        replacements.put("%uses%", "" + (uses == -1 ? LANG.getString("unlimited", "∞") : uses));
        replacements.put("%max-uses%", "" + (uses == -1 ? LANG.getString("unlimited", "∞") : uses));
        replacements.put("%sold-amount%", "" + 0);
        replacements.put("%sold-price%", "" + 0);

        ItemBuilder builder = ItemBuilder.create(sellwand.getItemSection(), replacements);
        ItemStack it = builder.get();

        NBTWrapper wrapper = new NBTWrapper(it);
        wrapper.set("axsellwands-type", sellwand.getId());
        wrapper.set("axsellwands-multiplier", multiplier);
        wrapper.set("axsellwands-lastused", 0L);
        wrapper.set("axsellwands-uses", uses);
        wrapper.set("axsellwands-max-uses", uses);
        wrapper.set("axsellwands-sold-amount", 0);
        wrapper.set("axsellwands-sold-price", 0D);

        int am = 1;
        if (amount != null) am = amount;

        for (int i = 0; i < am; i++) {
            if (CONFIG.getInt("stacking-mode", 0) != 2) wrapper.set("axsellwands-uuid", UUID.randomUUID());
            wrapper.build();
            ContainerUtils.INSTANCE.addOrDrop(player.getInventory(), List.of(it.clone()), player.getLocation());
        }

        replacements.put("%amount%", "" + am);
        replacements.put("%sellwand%", sellwand.getName());
        replacements.put("%player%", player.getName());

        MESSAGEUTILS.sendLang(sender, "sellwand-give", replacements);
        MESSAGEUTILS.sendLang(player, "sellwand-got", replacements);
    }
}
