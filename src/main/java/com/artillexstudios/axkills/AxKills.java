package com.artillexstudios.axkills;

import com.artillexstudios.axkills.commands.MainCommand;
import com.artillexstudios.axkills.config.AbstractConfig;
import com.artillexstudios.axkills.config.impl.Config;
import com.artillexstudios.axkills.database.DatabaseManager;
import com.artillexstudios.axkills.listeners.DeathListener;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class AxKills extends JavaPlugin {
    public static YamlDocument CONFIG;
    private static AbstractConfig abstractConfig;
    private static AxKills instance;
    private static DatabaseManager databaseManager;

    public static AxKills getInstance() {
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
        this.getCommand("axkills").setExecutor(mainCommand);
        this.getCommand("axkills").setTabCompleter(mainCommand);

        getServer().getPluginManager().registerEvents(new DeathListener(), this);
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
    }
}
