package net.yiran.extraholopage.core.mixins;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ToolAction;
import net.yiran.extraholopage.gui.ToolBarHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.client.ToolActionIconStore;
import se.mickelus.tetra.module.data.GlyphData;

import java.util.Map;
import java.util.Objects;

@Mixin(ToolActionIconStore.class)
public class ToolActionIconStoreMixin {
    @Inject(method = "prepareIcons",at=@At(value = "RETURN"))
    public void prepareIcons(CallbackInfoReturnable<Map<ToolAction, GlyphData>> cir) {
        var resourceManager = Minecraft.getInstance().getResourceManager();
        resourceManager.listResources("tool_bars", (rl) -> rl.getPath().endsWith(".json"))
                .entrySet()
                .stream()
                .filter((entry) -> "tetra".equals((entry.getKey()).getNamespace()))
                .map((entry) -> ToolBarHelper.parseG(entry.getValue()))
                .filter(Objects::nonNull)
                .forEach(map->cir.getReturnValue().putAll(map));
    }

}
