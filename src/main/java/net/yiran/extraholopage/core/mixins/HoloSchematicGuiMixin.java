package net.yiran.extraholopage.core.mixins;

import net.yiran.extraholopage.Config;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloSchematicGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloVariantDetailGui;
import se.mickelus.tetra.module.schematic.OutcomePreview;

import java.util.function.Consumer;

@Mixin(value = HoloSchematicGui.class, remap = false)
public abstract class HoloSchematicGuiMixin extends GuiElement {
    @Mutable
    @Shadow
    @Final
    private HoloVariantDetailGui detail;

    public HoloSchematicGuiMixin(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(int x, int y, int width, int height, Consumer<OutcomePreview> onVariantOpen, CallbackInfo ci) {
        if (Config.HOLO_MODULE_LINE.get() != 2) {
            elements.remove(detail);
            detail = new HoloVariantDetailGui(0, 68 + 15 * (Config.HOLO_MODULE_LINE.get() - 2), width, onVariantOpen);
            addChild(detail);
        }
    }
}
