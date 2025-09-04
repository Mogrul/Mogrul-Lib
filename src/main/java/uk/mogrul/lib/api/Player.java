package uk.mogrul.lib.api;

import java.util.UUID;

import net.minecraft.server.level.ServerPlayer;
import uk.mogrul.lib.Memory;

public class Player {
    public static PlayerData get(ServerPlayer serverPlayer) {
        return get(serverPlayer.getUUID());
    }

    public static PlayerData get(String username) {
        for (PlayerData playerData : Memory.players.values()) {
            if (playerData.username.equalsIgnoreCase(username)) {
                return playerData;
            }
        }

        return null;
    };

    public static PlayerData getByDiscordID(String discordID) {
        for (PlayerData playerData : Memory.players.values()) {
            if (playerData.getDiscordID().equals(discordID)) {
                return playerData;
            }
        }

        return null;
    }

    public static PlayerData get(UUID uuid) {
        return Memory.players.get(uuid);
    }
}
