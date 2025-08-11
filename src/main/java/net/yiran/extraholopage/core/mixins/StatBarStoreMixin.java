package net.yiran.extraholopage.core.mixins;

import net.minecraft.client.Minecraft;
import net.yiran.extraholopage.gui.ToolBarHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.gui.stats.bar.GuiStatBase;
import se.mickelus.tetra.gui.stats.data.StatBarStore;

import java.util.Objects;

@Mixin(StatBarStore.class)
public class StatBarStoreMixin {
    @Inject(method = "prepareBars",at=@At(value = "RETURN"),cancellable = true)
    private static void prepareBars(CallbackInfoReturnable<GuiStatBase[]> cir) {
        var resourceManager = Minecraft.getInstance().getResourceManager();
        GuiStatBase[] s = resourceManager.listResources("tool_bars", (rl) -> rl.getPath().endsWith(".json"))
                .entrySet()
                .stream()
                .filter((entry) -> "tetra".equals((entry.getKey()).getNamespace()))
                .map((entry) -> ToolBarHelper.parseT(entry.getValue()))
                .filter(Objects::nonNull)
                .toArray(GuiStatBase[]::new);
        cir.setReturnValue( ArrayUtils.addAll(cir.getReturnValue(),s));
    }
}
