package net.yiran.extraholopage.core.mixins;

import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.mutil.gui.GuiElement;

import java.util.List;

@Mixin(targets = "se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloVariantDetailGui$MaterialWrapper",remap = false)
public abstract class HoloVariantDetailGuiMixin$MaterialWrapper extends GuiElement{
    public HoloVariantDetailGuiMixin$MaterialWrapper(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Inject(method = "getTooltipLines",at=@At("HEAD"),cancellable = true)
    private void ehp$getTooltipLines(CallbackInfoReturnable<List<Component>> cir){
        if (this.hasFocus()) {
            List<Component> tooltip = super.getTooltipLines();
            if (tooltip != null && !tooltip.isEmpty()) {
                cir.setReturnValue(tooltip);
                return;
            }
        }

        cir.setReturnValue(null);
    }

}
