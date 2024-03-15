package com.artillexstudios.axsellwands.sellwands;

import com.artillexstudios.axapi.config.Config;
import com.artillexstudios.axsellwands.AxSellwands;

import java.io.File;
import java.util.HashMap;

public class Sellwands {
    private static final HashMap<String, Sellwand> sellwands = new HashMap<>();

    public static void reload() {
        sellwands.clear();

        final File path = new File(AxSellwands.getInstance().getDataFolder(), "sellwands");
        if (path.exists()) {
            for (File file : path.listFiles()) {
                if (!file.getName().endsWith(".yml") && !file.getName().endsWith(".yaml")) continue;
                final String id = file.getName().replace(".yml", "").replace("%yaml%", "");

                final Config cfg = new Config(file);
                final Sellwand sellwand = new Sellwand(id, cfg);

                sellwands.put(id, sellwand);
            }
        }
    }

    public static HashMap<String, Sellwand> getSellwands() {
        return sellwands;
    }
}
