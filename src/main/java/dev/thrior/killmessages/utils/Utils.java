package dev.thrior.killmessages.utils;

import dev.thrior.killmessages.hooks.PlaceholderAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import static dev.thrior.killmessages.KillMessages.CONFIG;

public class Utils {
    public static String setPlaceholders(@NotNull Player player, @NotNull String msg) {

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPIHook.setPlaceholders(player, msg);
        } else {
            return msg;
        }
    }
    public static String setItem(@NotNull Player player) {

        final ItemStack it = player.getInventory().getItemInMainHand();
        if (it.getType().isAir()) return CONFIG.getString("empty-hand-text");
        String typeStr = it.getType().toString().replace("_", " ").toLowerCase();

        if (it.getItemMeta() == null) return CONFIG.getString("item-format").replace("%item%", typeStr);
        final ItemMeta meta = it.getItemMeta();

        return CONFIG.getString("item-format").replace("%item%", meta.hasDisplayName() ? meta.getDisplayName() : typeStr);
    }
}
