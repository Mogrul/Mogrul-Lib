package uk.mogrul.lib;

import java.util.Map;
import java.util.UUID;

import uk.mogrul.lib.data.PlayerData;
import uk.mogrul.lib.subs.PlayerMap;

public class Memory {
    public static Map<UUID, PlayerData> players = new PlayerMap<>();
    public static Boolean isLoading = true;
}
