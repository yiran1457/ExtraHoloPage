package net.yiran.extraholopage;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.yiran.extraholopage.compat.CompatHandler;

@Mod(ExtraHoloPage.MODID)
@SuppressWarnings({"all","removal"})
public class ExtraHoloPage {
    public static final String MODID = "extraholopage";

    public ExtraHoloPage() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(CompatHandler::onClientSetup);
    }

}
