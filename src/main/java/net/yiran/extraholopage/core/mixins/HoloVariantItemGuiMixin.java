package net.yiran.extraholopage.core.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloVariantItemGui;

@Mixin(HoloVariantItemGui.class)
public class HoloVariantItemGuiMixin extends GuiClickable {
    public HoloVariantItemGuiMixin(int x, int y, int width, int height, Runnable onClickHandler) {
        super(x, y, width, height, onClickHandler);
    }

    @WrapOperation(method = "drawChildren", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;hasShiftDown()Z"))
    private boolean zzz(Operation<Boolean> original) {
        return !original.call();
    }
}
