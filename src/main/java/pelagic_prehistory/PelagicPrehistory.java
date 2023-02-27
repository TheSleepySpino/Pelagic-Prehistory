package pelagic_prehistory;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import pelagic_prehistory.client.ClientEvents;

@Mod(PelagicPrehistory.MODID)
public class PelagicPrehistory {

    public static final String MODID = "pelagic_prehistory";

    public static final Logger LOGGER = LogUtils.getLogger();

    public PelagicPrehistory() {
        PPRegistry.register();
        PPTab.register();
        PPEvents.register();
        // client events
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientEvents::register);
    }


}
