package uk.mogrul.lib.builders;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

import static uk.mogrul.lib.MogrulLib.*;
import static uk.mogrul.lib.builders.PathBuilder.*;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@EventBusSubscriber(modid = MODID)
public class SQLBuilder {
    public static Connection SQL_CONNECTION;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("[{}] Building / Initialising SQL Database", LOGNAME);

        // Create the SQL file.
        Path SQLFile = mogrulFolder.resolve("mogrul.db");
        if (!SQLFile.toFile().exists()) {
            try {
                SQLFile.toFile().createNewFile();
            } catch (IOException e) {
                LOGGER.error("[{}] Failed to create SQL Database file", LOGNAME, e.getMessage());

                return;
            }
        }

        // Create a connection.
        Connection sqlConnection = connect(SQLFile);

        // Create the default tables.
        if (sqlConnection != null) {
            SQL_CONNECTION = sqlConnection;
            createDefaultTables(sqlConnection);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerStopping(ServerStoppingEvent event) {
        LOGGER.info("[{}] Shutting down SQL Database", LOGNAME);

        disconnect();
    }

    private static Connection connect(Path SQLFile) {
        Connection SQLConnection;

        try {
            SQLConnection = DriverManager.getConnection(
                "jdbc:sqlite:" + SQLFile.toAbsolutePath()
            );

            LOGGER.info("[{}] Successfully connected to SQL Database!", LOGNAME);
        } catch (SQLException e) {
            LOGGER.error("[{}] Failed to connect to SQL Database", LOGNAME, e.getMessage());
            return null;
        }

        return SQLConnection;
    }

    private static void disconnect() {
        if (SQL_CONNECTION != null) {
            try {
                SQL_CONNECTION.close();
            } catch (SQLException e) {
                LOGGER.error("[{}] Failed to close SQL Database connection", LOGNAME, e.getMessage());
                return;
            }
        }
    }

    private static void createDefaultTables(Connection sqlConnection) {
        createPlayerTable(sqlConnection);
    }

    private static void createPlayerTable(Connection sqlConnection) {
        String sql = "CREATE TABLE IF NOT EXISTS players (" +
            "uuid TEXT PRIMARY KEY, " +
            "username TEXT NOT NULL, " +
            "first_joined INTEGER NOT NULL, " +
            "last_joined INTEGER NOT NULL, " +
            "playtime_minutes INTEGER DEFAULT 0, " +
            "currency INTEGER DEFAULT 0, " +
            "bounty INTEGER DEFAULT 0" +
        ");";

        try (PreparedStatement stmt = sqlConnection.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            LOGGER.error("[{}] Failed to create players SQL table", LOGNAME, e.getMessage());
            return;
        }
    }
}
