package dev.thrior.killmessages;

import dev.thrior.killmessages.commands.MainCommand;
import dev.thrior.killmessages.config.AbstractConfig;
import dev.thrior.killmessages.config.impl.Config;
import dev.thrior.killmessages.database.DatabaseManager;
import dev.thrior.killmessages.listeners.DeathListener;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class KillMessages extends JavaPlugin {
    public static YamlDocument CONFIG;
    private static AbstractConfig abstractConfig;
    private static KillMessages instance;
    private static DatabaseManager databaseManager;

    public static KillMessages getInstance() {
        return instance;
    }

    public static AbstractConfig getAbstractConfig() {
        return abstractConfig;
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public void onEnable() {
        instance = this;

        new Metrics(this, 19561);

        abstractConfig = new Config();
        abstractConfig.setup();
        CONFIG = abstractConfig.getConfig();

        databaseManager = new DatabaseManager();
        databaseManager.init();

        MainCommand mainCommand = new MainCommand();
        this.getCommand("killmessages").setExecutor(mainCommand);
        this.getCommand("killmessages").setTabCompleter(mainCommand);

        getServer().getPluginManager().registerEvents(new DeathListener(), this);
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
    }
}
