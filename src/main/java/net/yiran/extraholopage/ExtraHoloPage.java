package net.yiran.extraholopage;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.yiran.extraholopage.compat.CompatHandler;
import net.yiran.extraholopage.gui.ComponentHelper;
import net.yiran.extraholopage.gui.MaterialTooltipHelper;
import net.yiran.extraholopage.gui.TooltipHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.yiran.extraholopage.gui.ComponentHelper.literal;

@Mod(ExtraHoloPage.MODID)
@SuppressWarnings({"all","removal"})
public class ExtraHoloPage {
    public static final String MODID = "extraholopage";
    public static final String CLIENT_CONFIG = "extraholopage-client.toml";
    public static Logger LOGGER = LogManager.getLogger();

    public ExtraHoloPage() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(CompatHandler::onClientSetup);
        bus.addListener(this::onCommonSetup);
        bus.addListener(EventPriority.LOWEST,true,this::onConfigReload);
        bus.addListener(EventPriority.HIGHEST,this::onConfigload);
        FMLJavaModLoadingContext.get().registerConfig(
                ModConfig.Type.CLIENT,
                Config.SPEC,
                CLIENT_CONFIG
        );
    }
    public void onConfigload(ModConfigEvent event){

        var config = event.getConfig();
        if(event.getConfig().getFileName().equals(CLIENT_CONFIG)){

            if(event.getConfig().getSpec().equals(Config.SPEC)){
                var a = Config.SPEC.get("Tooltip.Color.color1");
            }
            //var a = Config.SPEC.get("Tooltip.Color.color1");
            var b = event.getConfig().getSpec().get("Tooltip.Color.color1");
            var c = Config.MODIFY_IMPROVEMENT_SPACING.get();
            LOGGER.info("Tooltip.Color.color1");
        }
    }
    public void onConfigReload(ModConfigEvent.Reloading event) {
        var config = event.getConfig();
        if(event.getConfig().getFileName().equals(CLIENT_CONFIG)){

            if(event.getConfig().getSpec().equals(Config.SPEC)){
                var a = Config.SPEC.get("Tooltip.Color.color1");
            }
            //var a = Config.SPEC.get("Tooltip.Color.color1");
            var b = event.getConfig().getSpec().get("Tooltip.Color.color1");
            var c = Config.MODIFY_IMPROVEMENT_SPACING.get();
            if(Minecraft.getInstance().player!=null){
                Minecraft.getInstance().player.sendSystemMessage(ComponentHelper.literal("Tooltip.Color.color1"));
            }
        }
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        if (Config.ENABLE_MATERIAL_TOOLTIP.get()) {
            TooltipHandler tooltipHandler = TooltipHandler.INSTANCE.get();
            MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, tooltipHandler::onTooltip);
            MinecraftForge.EVENT_BUS.addListener(tooltipHandler::onTick);
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
