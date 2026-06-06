package net.yiran.extraholopage.core.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.yiran.extraholopage.core.IHoloItemGui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.GuiAnimation;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloItemGui;

import java.util.List;
import java.util.function.Consumer;

@Mixin(value = HoloItemGui.class, remap = false)
public class HoloItemGuiMixin extends GuiClickable implements IHoloItemGui {
    public HoloItemGuiMixin(int x, int y, int width, int height, Runnable onClickHandler) {
        super(x, y, width, height, onClickHandler);
    }

    @Mutable
    @Shadow
    @Final
    private GuiTexture icon;

    @Shadow
    @Final
    private List<GuiAnimation> hoverAnimations;
    @Shadow
    @Final
    private List<GuiAnimation> blurAnimations;
    private GuiElement labelGroup;

    @Inject(method = "<init>(IILse/mickelus/tetra/items/modular/IModularItem;Lnet/minecraft/world/item/ItemStack;ILjava/lang/Runnable;Ljava/util/function/Consumer;)V", at = @At("RETURN"))
    private void ehp$mixin(int x, int y, IModularItem item, ItemStack itemStack, int textureIndex, Runnable onSelect, Consumer onSlotSelect, CallbackInfo ci, @Local(name = "labelGroup") GuiElement labelGroup) {
        this.labelGroup = labelGroup;
    }

    @Override
    public GuiElement getLabelGroup() {
        return labelGroup;
    }

    @Override
    public List<GuiAnimation> getHoverAnimations() {
        return hoverAnimations;
    }

    @Override
    public List<GuiAnimation> getBlurAnimations() {
        return blurAnimations;
    }

    public void setIcon(GuiTexture icon) {
        this.elements.remove(this.icon);
        this.icon = icon;
        this.icon.setAttachment(GuiAttachment.middleCenter);
        this.addChild(icon);
    }
}
