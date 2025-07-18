package net.yiran.extraholopage.core.mixins;

import net.yiran.extraholopage.core.IHoloItemGui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloItemGui;

@Mixin(HoloItemGui.class)
public abstract class HoloItemGuiMixin extends GuiClickable implements IHoloItemGui {
    public HoloItemGuiMixin(int x, int y, int width, int height, Runnable onClickHandler) {
        super(x, y, width, height, onClickHandler);
    }

    @Mutable
    @Shadow
    @Final
    private GuiTexture icon;

    public void setIcon(GuiTexture icon){
        this.elements.remove(this.icon);
        this.icon = icon;
        this.icon.setAttachment(GuiAttachment.middleCenter);
        this.addChild(icon);
    }
}
