package dev.thrior.killmessages.listeners;

import dev.thrior.killmessages.KillMessages;
import dev.thrior.killmessages.utils.ColorUtils;
import dev.thrior.killmessages.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

import static dev.thrior.killmessages.KillMessages.CONFIG;


public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(@NotNull PlayerDeathEvent event) {
        final Player player = event.getEntity();

        String msg;

        if (player.getKiller() != null) {
            final Player killer = player.getKiller();

            msg = CONFIG.getString("death-messages.KILLED");
            msg = msg.replace("%attacker%", killer.getName());
            msg = msg.replace("%victim%", player.getName());
            msg = msg.replace("%item%", Utils.setItem(killer));

            StringBuilder finalTxt = new StringBuilder();
            String[] message = msg.split("");
            String tempPlaceholder = "";
            boolean canbePlaceholder = true;
            for (String str : message) {

                if (str.equals("%") && canbePlaceholder) {
                    if (tempPlaceholder.isEmpty()) {
                        tempPlaceholder += str;
                    } else {
                        tempPlaceholder += str;
                        if (tempPlaceholder.contains("[ATTACKER]")) {
                            tempPlaceholder = tempPlaceholder.replace("[ATTACKER] ", "");
                            tempPlaceholder = tempPlaceholder.replace("[ATTACKER]", "");
                            finalTxt.append(Utils.setPlaceholders(killer, tempPlaceholder));
                        } else {
                            finalTxt.append(Utils.setPlaceholders(player, tempPlaceholder));
                        }
                        tempPlaceholder = "";
                    }
                    continue;
                }

                if (!tempPlaceholder.isEmpty()) {
                    tempPlaceholder += str;
                    continue;
                }

                canbePlaceholder = !str.equals("\\");
                finalTxt.append(str);
            }

            msg = finalTxt.toString();

        } else if (event.getEntity().getLastDamageCause() != null && CONFIG.isString("death-messages." + event.getEntity().getLastDamageCause().getCause())) {
            msg = CONFIG.getString("death-messages." + event.getEntity().getLastDamageCause().getCause());
        } else {
            msg = "";
        }

        msg = msg.replace("%victim%", player.getName());
        msg = Utils.setPlaceholders(player, msg);

        final String formatted = ColorUtils.format(msg);

        event.setDeathMessage(null);

        final boolean perWorld = CONFIG.getBoolean("per-world-killmessages", false);

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (KillMessages.getDatabaseManager().isDisabled(online.getUniqueId())) continue;
            if (perWorld && !online.getWorld().equals(player.getWorld())) continue;
            online.sendMessage(formatted);
        }
    }
}
