package net.yiran.extraholopage.core.mixins;

import net.yiran.extraholopage.Config;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloImprovementGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.OutcomeStack;
import se.mickelus.tetra.module.schematic.OutcomePreview;

import java.util.List;

@Mixin(HoloImprovementGui.class)
public class HoloImprovementGuiMixin extends GuiElement {
    @Shadow @Final private GuiElement variants;

    @Shadow @Final private GuiHorizontalLayoutGroup header;

    public HoloImprovementGuiMixin(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Inject(method = "updateVariants",at = @At(value = "RETURN"))
    private void updateVariants(OutcomePreview[] previews, List<OutcomeStack> selectedOutcomes, CallbackInfo ci){
        int i = Config.MODIFY_IMPROVEMENT_SPACING.get();
        if(this.variants.isVisible())
            this.setWidth(Math.max(this.variants.getX() + previews.length * i - 9, this.header.getWidth()));
    }
}
