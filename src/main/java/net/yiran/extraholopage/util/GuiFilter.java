package net.yiran.extraholopage.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.yiran.extraholopage.ExtraHoloPage;
import net.yiran.extraholopage.api.MaterialManager;
import se.mickelus.mutil.gui.GuiButton;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;

public class GuiFilter extends GuiElement {
    public static ResourceLocation texture = new ResourceLocation(ExtraHoloPage.MODID, "textures/gui/back.png");
    public MaterialFilter forceFiler;
    public GuiFilter afterFilter;
    public Runnable updateGroups;

    public GuiFilter(int x, int y, int width, int height, Runnable runnable, GuiFilter beforeFilter) {
        super(x, y, width, height);
        forceFiler = MaterialFilter.NONE;
        this.afterFilter = beforeFilter;
        updateGroups = runnable;
        resetFilters();
    }

    public void resetFilters() {
        clearChildren();
        var forceString = I18n.get("ehp.filter.desc") + " : " + forceFiler.getName();
        var wight1 = Minecraft.getInstance().font.width(forceString);
        if (this.afterFilter != null) {
            this.afterFilter.setX(getX() + wight1 + 10);
            this.afterFilter.resetFilters();
        }
        addChild(new GuiTexture(-2, -2, wight1 + 4, 10 + 4, texture));
        addChild(new GuiButton(0, 0, forceString, () -> {
            clearChildren();
            var index = -10;
            var wight = 0;
            for (MaterialFilter filter : MaterialManager.INSTANCE.getFilters()) {
                addChild(new GuiButton(0, index += 10, filter.getName(), () -> {
                    setForceFiler(filter);
                    updateGroups.run();
                    resetFilters();
                }));
                wight = Math.max(wight, Minecraft.getInstance().font.width(filter.getName()));

            }
            if (this.afterFilter != null) {
                this.afterFilter.setX(getX() + wight1 + 10);
                this.afterFilter.resetFilters();
            }
            var wight2 = wight;
            getChildren(GuiButton.class).forEach(button -> {
                button.setWidth(wight2);
            });
            addChild(new GuiTexture(-2, -2, wight + 4, index + 10 + 4, texture));
        }));
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        graphics.pose().translate(0, 0, 1000);
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        graphics.pose().translate(0, 0, -1000);
    }

    public void setForceFiler(MaterialFilter forceFiler) {
        this.forceFiler = forceFiler;
    }

    public MaterialFilter getForceFiler() {
        return forceFiler;
    }
}
