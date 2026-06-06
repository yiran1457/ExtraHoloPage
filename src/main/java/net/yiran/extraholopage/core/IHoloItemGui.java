package net.yiran.extraholopage.core;

import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.GuiAnimation;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloItemGui;

import java.util.List;

public interface IHoloItemGui {
    static IHoloItemGui cast(HoloItemGui holoItemGui){
        return (IHoloItemGui)holoItemGui;
    }
    void setIcon(GuiTexture icon);
    GuiElement getLabelGroup();
    List<GuiAnimation> getHoverAnimations();
   List<GuiAnimation> getBlurAnimations();
}
