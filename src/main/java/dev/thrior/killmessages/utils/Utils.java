package dev.thrior.killmessages.utils;

import dev.thrior.killmessages.hooks.PlaceholderAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static dev.thrior.killmessages.KillMessages.CONFIG;

public class Utils {

    private static final String UNIQUE_ID_LORE = ChatColor.translateAlternateColorCodes('&', "&6Unique ID");

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

    public static @NotNull String getUniqueIdItemName(@NotNull Player player) {
        final String emptyText = CONFIG.getString("unique-id-empty", "");

        final ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) return emptyText;

        final ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return emptyText;

        final List<String> lore = meta.getLore();
        if (lore == null) return emptyText;

        for (String line : lore) {
            if (line.contains(UNIQUE_ID_LORE)) {
                String itemName = meta.hasDisplayName()
                        ? meta.getDisplayName()
                        : item.getType().toString().replace("_", " ").toLowerCase();
                return CONFIG.getString("unique-id-format", "%item%").replace("%item%", itemName);
            }
        }
        return emptyText;
    }
}
