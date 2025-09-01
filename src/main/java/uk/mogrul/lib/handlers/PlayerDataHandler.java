package uk.mogrul.lib.handlers;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import uk.mogrul.lib.Memory;
import uk.mogrul.lib.api.PlayerData;

import static uk.mogrul.lib.MogrulLib.*;
import static uk.mogrul.lib.builders.SQLBuilder.SQL_CONNECTION;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = MODID)
public class PlayerDataHandler {
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("[{}] Loading all player data into memory...", LOGNAME);

        List<PlayerData> allPlayerData = loadAllPlayerData();
        if (allPlayerData.isEmpty()) {
            LOGGER.info("[{}] No player data found in the database.", LOGNAME);
            return;
        }

        for (PlayerData playerData : allPlayerData) {
            UUID uuid = playerData.uuid;
            Memory.players.put(uuid, playerData);
        }

        LOGGER.info("[{}] Loaded {} player data entries into memory.", LOGNAME, allPlayerData.size());
        Memory.isLoading = false;
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) {
            return;
        }

        if (Memory.players.containsKey(serverPlayer.getUUID())) {
            return; // Player data already exists in memory.
        }

        // Create new player data.
        PlayerData newPlayerData = new PlayerData(
            serverPlayer.getUUID(),
            serverPlayer.getGameProfile().getName(),
            Instant.now(),
            Instant.now(),
            0,
            0,
            0
        );

        Memory.players.put(serverPlayer.getUUID(), newPlayerData);
        insertPlayer(newPlayerData);
        LOGGER.info("[{}] Created new player data for {}", LOGNAME, serverPlayer.getScoreboardName());
    }

    private static List<PlayerData> loadAllPlayerData() {
        List<PlayerData> playerDataList = new ArrayList<>();

        String sql = "SELECT * FROM players;";
        try (Statement stmt = SQL_CONNECTION.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                String uuidString = rs.getString("uuid");
                String username = rs.getString("username");
                long firstJoined = rs.getLong("first_joined");
                long lastJoined = rs.getLong("last_joined");
                int playtimeMinutes = rs.getInt("playtime_minutes");
                int currency = rs.getInt("currency");
                int bounty = rs.getInt("bounty");

                PlayerData playerData = new PlayerData(
                    UUID.fromString(uuidString),
                    username,
                    Instant.ofEpochMilli(firstJoined),
                    Instant.ofEpochMilli(lastJoined),
                    playtimeMinutes,
                    currency,
                    bounty
                );

                playerDataList.add(playerData);
            }
        } catch (Exception e) {
            LOGGER.error("[{}] Failed to load all player data", LOGNAME, e);
        }

        return playerDataList;
    }

    public static void updatePlayer(PlayerData playerData) {
        String sql = "UPDATE players SET " +
            "username = ?, " +
            "first_joined = ?, " +
            "last_joined = ?, " +
            "playtime_minutes = ?, " +
            "currency = ?, " +
            "bounty = ? " +
            "WHERE uuid = ?;";

        try (var stmt = SQL_CONNECTION.prepareStatement(sql)) {
            stmt.setString(1, playerData.username);
            stmt.setLong(2, playerData.firstJoined.toEpochMilli());
            stmt.setLong(3, playerData.lastJoined.toEpochMilli());
            stmt.setInt(4, playerData.getPlaytimeMinutes());
            stmt.setInt(5, playerData.getCurrency());
            stmt.setInt(6, playerData.getBounty());
            stmt.setString(7, playerData.uuid.toString());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                LOGGER.warn("[{}] No player data found to update for UUID {}", LOGNAME, playerData.uuid);
            } else {
                LOGGER.info("[{}] Updated player data for UUID {}", LOGNAME, playerData.uuid);
            }
        } catch (Exception e) {
            LOGGER.error("[{}] Failed to update player data for UUID {}", LOGNAME, playerData.uuid, e);
        }
    }

    public static void insertPlayer(PlayerData playerData) {
        String sql = "INSERT INTO players (uuid, username, first_joined, last_joined, playtime_minutes, currency, bounty) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (var stmt = SQL_CONNECTION.prepareStatement(sql)) {
            stmt.setString(1, playerData.uuid.toString());
            stmt.setString(2, playerData.username);
            stmt.setLong(3, playerData.firstJoined.toEpochMilli());
            stmt.setLong(4, playerData.lastJoined.toEpochMilli());
            stmt.setInt(5, playerData.getPlaytimeMinutes());
            stmt.setInt(6, playerData.getCurrency());
            stmt.setInt(7, playerData.getBounty());

            stmt.executeUpdate();
            LOGGER.info("[{}] Inserted new player data for UUID {}", LOGNAME, playerData.uuid);
        } catch (Exception e) {
            LOGGER.error("[{}] Failed to insert player data for UUID {}", LOGNAME, playerData.uuid, e.getMessage());
        }
    }
}
