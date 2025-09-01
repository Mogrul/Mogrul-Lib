package uk.mogrul.lib;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@Mod(MogrulLib.MODID)
public class MogrulLib {
    public static final String MODID = "mogrullib";
    public static final String LOGNAME = "MogrulLib";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MogrulLib(IEventBus bus, ModContainer container) {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("[{}] Mogrul Lib has loaded successfully!", LOGNAME);
    }
}
