package net.yiran.extraholopage;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.yiran.extraholopage.compat.CompatHandler;
import net.yiran.extraholopage.gui.MaterialTooltipHelper;
import net.yiran.extraholopage.gui.TooltipHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.yiran.extraholopage.gui.ComponentHelper.literal;

@Mod(ExtraHoloPage.MODID)
@SuppressWarnings({"all", "removal"})
public class ExtraHoloPage {
    public static final String MODID = "extraholopage";
    public static final String CLIENT_CONFIG = "extraholopage-client.toml";
    public static Logger LOGGER = LogManager.getLogger();

    public ExtraHoloPage() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(CompatHandler::onClientSetup);
        bus.addListener(this::onCommonSetup);

        FMLJavaModLoadingContext.get().registerConfig(
                ModConfig.Type.CLIENT,
                Config.SPEC,
                CLIENT_CONFIG
        );
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        if (Config.ENABLE_MATERIAL_TOOLTIP.get()) {
            TooltipHandler tooltipHandler = TooltipHandler.INSTANCE.get();
            MinecraftForge.EVENT_BUS.register(tooltipHandler);
            if (Config.COLOR_1.get() != 0)
                MaterialTooltipHelper.color1 = Config.COLOR_1.get();
            if (Config.COLOR_2.get() != 0) {
                MaterialTooltipHelper.color2 = Config.COLOR_2.get();
                MaterialTooltipHelper.MIDDLE = literal("  ├ ", MaterialTooltipHelper.color2);
                MaterialTooltipHelper.END = literal("  └ ", MaterialTooltipHelper.color2);
            }
            if (Config.COLOR_3.get() != 0) {
                MaterialTooltipHelper.color3 = Config.COLOR_3.get();
                MaterialTooltipHelper.PREFIX = literal("» ", MaterialTooltipHelper.color3);
            }
            if (Config.COLOR_GOOD.get() != 0)
                MaterialTooltipHelper.color_good = Config.COLOR_GOOD.get();
            if (Config.COLOR_BAD.get() != 0)
                MaterialTooltipHelper.color_bad = Config.COLOR_BAD.get();

        }
    }
}
