package com.artillexstudios.axsellwands.hooks;

import com.artillexstudios.axintegrations.IntegrationManager;
import com.artillexstudios.axintegrations.IntegrationSetup;
import com.artillexstudios.axintegrations.IntegrationType;
import com.artillexstudios.axintegrations.api.AxIntegrationsAPI;

import java.util.Collections;
import java.util.Map;

import static com.artillexstudios.axsellwands.AxSellwands.HOOKS;

public class HookManager {

    public static void setup() {
        IntegrationSetup.builder()
                .enableProtectionIntegrations(name -> {
                    return HOOKS.getBoolean("hooks.protection-plugins.%s".formatted(name), true);
                })
                .enableShopIntegrations(name -> {
                    System.out.println(name);
                    return HOOKS.getString("hooks.price-plugin", "").equalsIgnoreCase(name);
                })
                .enableCurrencyIntegrations(name -> {
                    return HOOKS.getString("hooks.economy-plugin", "").equalsIgnoreCase(name);
                }, name -> {
                    return Collections.singletonList(HOOKS.getString("hook-settings.%s.currency-name".formatted(name)));
                })
                .runAfterLoad(() -> {
                    AxIntegrationsAPI.provideIntegration(BuiltinPrices.class);

                    boolean modified = false;
                    for (String name : IntegrationManager.listAvailableIntegrations(IntegrationType.PROTECTION).keySet()) {
                        String route = "hooks.protection-plugins.%s".formatted(name);
                        if (HOOKS.getString(route) == null) {
                            HOOKS.set(route, true);
                            modified = true;
                        }
                    }

                    for (Map.Entry<String, String> entry : IntegrationManager.listAvailableIntegrations(IntegrationType.CURRENCY).entrySet()) {
                        String name = entry.getKey();
                        if (!entry.getValue().contains("-")) continue;
                        String route = "hook-settings.%s.currency-name".formatted(name);
                        if (HOOKS.getString(route) == null) {
                            HOOKS.set(route, "coins");
                            modified = true;
                        }
                    }
                    if (modified) HOOKS.save();
                })
                .runAfterSetup(() -> {
//                    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
//                        new PlaceholderAPIHook().load();
//                        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#00FF00[AxSellwands] Hooked into PlaceholderAPI!"));
//                    }
                })
                .setup();
    }

    public static void reload() {
        IntegrationManager.reload();
    }
}
