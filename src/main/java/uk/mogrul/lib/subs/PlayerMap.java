package uk.mogrul.lib.subs;

import java.util.HashMap;

import uk.mogrul.lib.Memory;
import uk.mogrul.lib.api.PlayerData;
import uk.mogrul.lib.handlers.PlayerDataHandler;

public class PlayerMap<K, V> extends HashMap<K, V> {
    public PlayerMap() {
        super();
    }

    @Override
    public V put(K key, V value) {
        if (value instanceof PlayerData playerData && Memory.isLoading == false) {
            PlayerDataHandler.updatePlayer(playerData);
        }

        return super.put(key, value);
    }
}