package net.yiran.extraholopage.core.mixins;

import net.minecraft.client.Minecraft;
import net.yiran.extraholopage.Config;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloImprovementVariantGui;
import se.mickelus.tetra.module.schematic.OutcomePreview;

import java.util.function.Consumer;

@Mixin(HoloImprovementVariantGui.class)
public class HoloImprovementVariantGuiMixin extends GuiElement {
    @Shadow @Final private GuiString label;

    public HoloImprovementVariantGuiMixin(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Inject(method = "<init>",at=@At(value = "TAIL"))
    private void init(int x, int y, String name, int labelStart, OutcomePreview preview, boolean isConnected, Consumer onVariantHover, Consumer onVariantBlur, Consumer onVariantSelect, CallbackInfo ci){
        int i = Config.MODIFY_IMPROVEMENT_SPACING.get();
        this.label.setString(name);
        this.setWidth(Minecraft.getInstance().font.width(name));
        this.setX(this.getX()/28*i);
        if(isConnected&&i!=28){
            for (GuiElement child : this.getChildren()) {
                if(child.getX()==-2){
                    child.setVisible(false);
                }
            }
        }
    }
}
