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

public class GuiSorter extends GuiElement {
    public static ResourceLocation texture = new ResourceLocation(ExtraHoloPage.MODID, "textures/gui/back.png");
    public MaterialSorter forceSorter;
    public Runnable updateGroups;
    public GuiFilter guiFilter;

    public GuiSorter(int x, int y, int width, int height, Runnable runnable, GuiFilter guiFilter) {
        super(x, y, width, height);
        forceSorter = MaterialSorter.NONE;
        this.guiFilter = guiFilter;
        updateGroups = runnable;
        resetSorter();
    }

    public void resetSorter() {
        clearChildren();
        var forceString = I18n.get("ehp.sorter.desc") + " : " + forceSorter.getName();
        var wight1 = Minecraft.getInstance().font.width(forceString);
        guiFilter.setX(getX() + wight1 + 10);
        guiFilter.resetFilters();
        addChild(new GuiTexture(-2, -2, wight1 + 4, 10 + 4, texture));
        addChild(new GuiButton(0, 0, forceString, () -> {
            clearChildren();
            var index = -10;
            var wight = 0;
            for (MaterialSorter sorter : MaterialManager.INSTANCE.getSorters()) {
                addChild(new GuiButton(0, index += 10, sorter.getName(), () -> {
                    setForceSorter(sorter);
                    updateGroups.run();
                    resetSorter();
                }));
                wight = Math.max(wight, Minecraft.getInstance().font.width(sorter.getName()));

            }
            guiFilter.setX(getX() + wight + 10);
            guiFilter.resetFilters();
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

    public void setForceSorter(MaterialSorter forceSorter) {
        this.forceSorter = forceSorter;
    }

    public MaterialSorter getForceSorter() {
        return forceSorter;
    }


}
