package dev.thrior.killmessages.commands;

import dev.thrior.killmessages.KillMessages;
import dev.thrior.killmessages.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class MainCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s,
            @NotNull String[] args) {

        if (args.length > 0 && args[0].equalsIgnoreCase("toggle")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be used by players.");
                return true;
            }

            final Player player = (Player) sender;

            if (!player.hasPermission("killmessages.toggle") && !player.hasPermission("killmessages.admin")
                    && !player.hasPermission("killmessages.*")) {
                MessageUtils.sendMsgP(player, "errors.no-permission");
                return true;
            }

            boolean nowEnabled = KillMessages.getDatabaseManager().toggle(player.getUniqueId());
            MessageUtils.sendMsgP(player, nowEnabled ? "toggle.enabled" : "toggle.disabled");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("killmessages.reload") && !sender.hasPermission("killmessages.admin")
                    && !sender.hasPermission("killmessages.*")) {
                MessageUtils.sendMsgP(sender, "errors.no-permission");
                return true;
            }

            KillMessages.getAbstractConfig().reloadConfig();
            MessageUtils.sendMsgP(sender, "reload");
            return true;
        }

        if (sender.hasPermission("killmessages.reload") || sender.hasPermission("killmessages.admin")
                || sender.hasPermission("killmessages.*")) {
            MessageUtils.sendMsgP(sender, "usage");
        } else {
            MessageUtils.sendMsgP(sender, "errors.no-permission");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias,
            @NotNull String[] args) {
        if (args.length == 1) {
            return Stream.of("toggle", "reload")
                    .filter(sub -> sub.startsWith(args[0].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }
}
