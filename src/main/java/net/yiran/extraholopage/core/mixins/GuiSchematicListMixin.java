package net.yiran.extraholopage.core.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.mutil.gui.GuiButton;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.tetra.blocks.workbench.gui.GuiSchematicList;

import java.util.function.Consumer;

@Mixin(GuiSchematicList.class)
public abstract class GuiSchematicListMixin extends GuiElement {
    @Mutable
    @Shadow @Final private GuiButton buttonForward;

    @Mutable
    @Shadow @Final private GuiButton buttonBack;

    @Shadow protected abstract void setPage(int page);

    @Shadow protected abstract int getPage();

    public GuiSchematicListMixin(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Inject(method = "<init>",at = @At(value = "TAIL"))
    private void init(int x, int y, Consumer schematicSelectionConsumer, CallbackInfo ci){
        this.elements.remove(this.buttonBack);
        this.elements.remove(this.buttonForward);
        Font font = Minecraft.getInstance().font;

        String buttonBackText =I18n.get("tetra.workbench.schematic_list.previous");
        int size = font.width(buttonBackText);
        this.buttonBack = new GuiButton(20-size, this.height + 4, size, 12, buttonBackText, () -> this.setPage(this.getPage() - 1));
        this.addChild(this.buttonBack);

        String buttonForwardText =I18n.get("tetra.workbench.schematic_list.next");
        this.buttonForward = new GuiButton(this.width - 20, this.height + 4, font.width(buttonForwardText), 12, buttonForwardText, () -> this.setPage(this.getPage() + 1));
        this.addChild(this.buttonForward);
    }
}
