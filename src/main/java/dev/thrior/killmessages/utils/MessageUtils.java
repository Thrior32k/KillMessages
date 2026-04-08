package dev.thrior.killmessages.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static dev.thrior.killmessages.KillMessages.CONFIG;

public class MessageUtils {

    public static void sendMsgP(@NotNull CommandSender p, String path) {
        p.sendMessage(ColorUtils.format(CONFIG.getString("prefix") + CONFIG.getString(path)));
    }

    public static void sendMsgP(@NotNull Player p, String path) {
        p.sendMessage(ColorUtils.format(CONFIG.getString("prefix") + CONFIG.getString(path)));
    }

}