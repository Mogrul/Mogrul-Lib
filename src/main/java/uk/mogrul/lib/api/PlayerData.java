package uk.mogrul.lib.api;

import java.time.Instant;
import java.util.UUID;

import uk.mogrul.lib.Memory;

/**
 * Represents all stored data about a player.
 * Includes identifiers, session info, economy stats, and external links.
 */
public class PlayerData {
    // --- Core identifiers ---
    /** Unique identifier for the player (persistent across sessions). */
    public UUID uuid;

    /** The player's username (may change over time). */
    public String username;

    // --- Session info ---
    /** The timestamp of when the player first joined. */
    public Instant firstJoined;

    /** The timestamp of the player's last join. */
    public Instant lastJoined;

    // --- External links ---
    /** Linked Discord account ID (null if not linked). */
    private String discordID;

    // --- Player stats ---
    /** Total playtime in minutes. */
    private int playtimeMinutes;

    /** In-game currency balance. */
    private int currency;

    /** The player's bounty value (e.g., for gameplay mechanics). */
    private int bounty;

    /**
     * Creates a new PlayerData object with the given details.
     *
     * @param uuid The unique player UUID.
     * @param username The player's username.
     * @param firstJoined When the player first joined.
     * @param lastJoined When the player last joined.
     * @param discordID Linked Discord ID (optional).
     * @param playtimeMinutes Total playtime in minutes.
     * @param currency Current currency balance.
     * @param bounty Current bounty value.
     */
    public PlayerData(
            UUID uuid,
            String username,
            Instant firstJoined,
            Instant lastJoined,
            String discordID,
            int playtimeMinutes,
            int currency,
            int bounty
    ) {
        this.uuid = uuid;
        this.username = username;
        this.firstJoined = firstJoined;
        this.lastJoined = lastJoined;
        this.discordID = discordID;
        this.playtimeMinutes = playtimeMinutes;
        this.currency = currency;
        this.bounty = bounty;
    }

    // --- Update methods (also persist changes to Memory) ---

    /**
     * Updates the player's currency balance and persists it to Memory.
     *
     * @param newCurrency The new currency value.
     */
    public void updateCurrency(int newCurrency) {
        this.currency = newCurrency;
        Memory.players.put(this.uuid, this);
    }

    /**
     * Updates the player's bounty value and persists it to Memory.
     *
     * @param newBounty The new bounty value.
     */
    public void updateBounty(int newBounty) {
        this.bounty = newBounty;
        Memory.players.put(this.uuid, this);
    }

    /**
     * Updates the player's total playtime and persists it to Memory.
     *
     * @param newPlaytimeMinutes The updated playtime in minutes.
     */
    public void updatePlaytimeMinutes(int newPlaytimeMinutes) {
        this.playtimeMinutes = newPlaytimeMinutes;
        Memory.players.put(this.uuid, this);
    }

    /**
     * Updates the player's linked Discord ID and persists it to Memory.
     *
     * @param newDiscordID The new Discord ID.
     */
    public void updateDiscordID(String newDiscordID) {
        this.discordID = newDiscordID;
        Memory.players.put(this.uuid, this);
    }

    // --- Getters ---

    /** @return The player's current currency balance. */
    public int getCurrency() {
        return this.currency;
    }

    /** @return The player's current bounty value. */
    public int getBounty() {
        return this.bounty;
    }

    /** @return The player's total playtime in minutes. */
    public int getPlaytimeMinutes() {
        return this.playtimeMinutes;
    }

    /** @return The player's linked Discord ID, or null if none. */
    public String getDiscordID() {
        return this.discordID;
    }
}

