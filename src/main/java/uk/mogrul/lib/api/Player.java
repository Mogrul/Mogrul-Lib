package uk.mogrul.lib.api;

import java.util.UUID;

import net.minecraft.server.level.ServerPlayer;
import uk.mogrul.lib.Memory;

public class Player {

    /**
     * Retrieves a player's data using a ServerPlayer object.
     *
     * @param serverPlayer The server player object (likely from the game API).
     * @return The PlayerData associated with the given player's UUID, or null if not found.
     */
    public static PlayerData get(ServerPlayer serverPlayer) {
        return get(serverPlayer.getUUID());
    }

    /**
     * Retrieves a player's data by their username.
     *
     * @param username The player's in-game username.
     * @return The PlayerData object if a matching username is found (ignoring case),
     *         otherwise null.
     */
    public static PlayerData get(String username) {
        for (PlayerData playerData : Memory.players.values()) {
            if (playerData.username.equalsIgnoreCase(username)) {
                return playerData;
            }
        }
        return null; // No matching player found
    };

    /**
     * Retrieves a player's data by their linked Discord ID.
     *
     * @param discordID The Discord user ID associated with the player.
     * @return The PlayerData object if a matching Discord ID is found,
     *         otherwise null.
     */
    public static PlayerData getByDiscordID(String discordID) {
        for (PlayerData playerData : Memory.players.values()) {
            if (playerData.getDiscordID().equals(discordID)) {
                return playerData;
            }
        }
        return null; // No matching player found
    }

    /**
     * Retrieves a player's data using their UUID.
     *
     * @param uuid The player's unique UUID.
     * @return The PlayerData object mapped to this UUID, or null if not present.
     */
    public static PlayerData get(UUID uuid) {
        return Memory.players.get(uuid);
    }
}

