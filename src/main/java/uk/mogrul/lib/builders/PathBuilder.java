package uk.mogrul.lib.builders;

import static uk.mogrul.lib.MogrulLib.*;

import java.nio.file.Files;
import java.nio.file.Path;

import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@EventBusSubscriber(modid = MODID)
public class PathBuilder {
    public static Path rootFolder;
    public static Path configFolder;
    public static Path mogrulFolder;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        rootFolder = server.getServerDirectory();
        configFolder = rootFolder.resolve("config");
        mogrulFolder = configFolder.resolve("Mogrul");

        if (!Files.exists(mogrulFolder)) {
            try {
                LOGGER.info("[{}] Building paths...", LOGNAME);
                Files.createDirectories(mogrulFolder);
            } catch (Exception e) {
                LOGGER.error(
                    "[{}] Failed to create Mogrul config folder at {}",
                    LOGNAME,
                    mogrulFolder.toAbsolutePath(),
                    e
                );
            }
        }
    }
}
