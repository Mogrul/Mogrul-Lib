package uk.mogrul.lib.api;

import java.time.Instant;
import java.util.UUID;

import uk.mogrul.lib.Memory;

public class PlayerData {
    public UUID uuid;
    public String username;
    public Instant firstJoined;
    public Instant lastJoined;
    private int playtimeMinutes;
    private int currency;
    private int bounty;

    public PlayerData(
            UUID uuid,
            String username,
            Instant firstJoined,
            Instant lastJoined,
            int playtimeMinutes,
            int currency,
            int bounty
    ) {
        this.uuid = uuid;
        this.username = username;
        this.firstJoined = firstJoined;
        this.lastJoined = lastJoined;
        this.playtimeMinutes = playtimeMinutes;
        this.currency = currency;
        this.bounty = bounty;
    }

    public void updateCurrency(int newCurrency) {
        this.currency = newCurrency;
        Memory.players.put(this.uuid, this);
    }

    public void updateBounty(int newBounty) {
        this.bounty = newBounty;
        Memory.players.put(this.uuid, this);
    }

    public void updatePlaytimeMinutes(int newPlaytimeMinutes) {
        this.playtimeMinutes = newPlaytimeMinutes;
        Memory.players.put(this.uuid, this);
    }

    public int getCurrency() {
        return this.currency;
    }

    public int getBounty() {
        return this.bounty;
    }

    public int getPlaytimeMinutes() {
        return this.playtimeMinutes;
    }
}
