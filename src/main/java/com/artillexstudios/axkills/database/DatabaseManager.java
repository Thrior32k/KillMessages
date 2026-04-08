package com.artillexstudios.axkills.database;

import com.artillexstudios.axkills.AxKills;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseManager {
    private static final String DB_FILE_NAME = "playerdata.db";
    private final Set<UUID> disabledPlayers = ConcurrentHashMap.newKeySet();
    private Connection connection;

    public void init() {
        try {
            final File dbFile = new File(AxKills.getInstance().getDataFolder(), DB_FILE_NAME);
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());

            try (PreparedStatement stmt = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS player_settings (" +
                            "uuid TEXT PRIMARY KEY, " +
                            "death_messages_disabled INTEGER NOT NULL DEFAULT 0" +
                            ")")) {
                stmt.execute();
            }

            loadAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAll() throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT uuid FROM player_settings WHERE death_messages_disabled = 1")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                disabledPlayers.add(UUID.fromString(rs.getString("uuid")));
            }
        }
    }

    public boolean toggle(UUID uuid) {
        boolean nowEnabled;
        if (disabledPlayers.remove(uuid)) {
            nowEnabled = true;
        } else {
            disabledPlayers.add(uuid);
            nowEnabled = false;
        }

        final boolean disabled = !nowEnabled;
        AxKills.getInstance().getServer().getScheduler().runTaskAsynchronously(AxKills.getInstance(), () -> {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO player_settings (uuid, death_messages_disabled) VALUES (?, ?) " +
                            "ON CONFLICT(uuid) DO UPDATE SET death_messages_disabled = ?")) {
                stmt.setString(1, uuid.toString());
                stmt.setInt(2, disabled ? 1 : 0);
                stmt.setInt(3, disabled ? 1 : 0);
                stmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return nowEnabled;
    }

    public boolean isDisabled(UUID uuid) {
        return disabledPlayers.contains(uuid);
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
