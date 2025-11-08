package net.yiran.extraholopage.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.yiran.extraholopage.Config;
import net.yiran.extraholopage.ExtraHoloPage;
import net.yiran.extraholopage.KeyMappingHandler;
import net.yiran.extraholopage.api.MaterialManager;
import net.yiran.extraholopage.core.IHoloMaterialListGui;
import se.mickelus.mutil.gui.*;

import java.util.ArrayList;
import java.util.List;

public class GuiSorter extends GuiElement {
    public static ResourceLocation texture = new ResourceLocation(ExtraHoloPage.MODID, "textures/gui/back.png");
    public MaterialSorter focusSorter;
    public Runnable updateGroups;
    public GuiFilter guiFilter;
    public int focusPage = 0;
    public boolean isSelected = false;
    public IHoloMaterialListGui gui;

    public GuiSorter(int x, int y, int width, int height, Runnable runnable, GuiFilter guiFilter, IHoloMaterialListGui holoMaterialListGui) {
        super(x, y, width, height);
        gui = holoMaterialListGui;
        focusSorter = MaterialSorter.NONE;
        this.guiFilter = guiFilter;
        updateGroups = runnable;
        resetSorter();
    }

    public void resetSorter() {
        clearChildren();
        var focusString = I18n.get("ehp.sorter.desc") + " : " + focusSorter.getName();
        var wight1 = Minecraft.getInstance().font.width(focusString);
        guiFilter.setX(getX() + wight1 + 10);
        guiFilter.resetFilters();
        isSelected = false;
        addChild(new GuiTexture(-2, -2, wight1 + 4, 10 + 4, texture));
        addChild(new GuiButton(0, 0, focusString, () -> {
            gui.resetAllSF();
            isSelected = true;
            setPage(focusPage);
        }));
    }

    public void setPage(int page) {
        focusPage = page;
        int totalPage = (int) Math.ceil(MaterialManager.INSTANCE.getSorters().size() / (float) Config.SHOW_FILTER_AND_SORTER_ELEMENTS.get());
        List<MaterialSorter> list = new ArrayList<>();
        for (int i = page * Config.SHOW_FILTER_AND_SORTER_ELEMENTS.get(); i < (page + 1) * Config.SHOW_FILTER_AND_SORTER_ELEMENTS.get(); i++) {
            if (i >= MaterialManager.INSTANCE.getSorters().size()) break;
            list.add(MaterialManager.INSTANCE.getSorters().get(i));
        }
        clearChildren();
        var index = -10;
        var wight = 0;
        for (MaterialSorter sorter : list) {
            if (sorter == null) break;
            addChild(new GuiButton(0, index += 10, sorter.getName(), () -> {
                setFocusSorter(sorter);
                updateGroups.run();
                resetSorter();
            }));
            wight = Math.max(wight, Minecraft.getInstance().font.width(sorter.getName()));
        }
        String ss = I18n.get("ehp.page_info.format", focusPage + 1, totalPage);
        int ww = Minecraft.getInstance().font.width(ss);
        if (totalPage > 1) {
            addChild(new GuiString(0, index += 10, ss));
            wight = Math.max(wight, ww);
        }
        guiFilter.setX(getX() + wight + 10);
        guiFilter.resetFilters();
        var wight2 = wight;
        getChildren(GuiButton.class).forEach(button -> {
            button.setWidth(wight2);
        });
        getChildren(GuiString.class).forEach(string -> {
            string.setX((wight2 - ww) / 2);
        });
        //addChild(new GuiString(0, -10, "1 - " + focusPage + " - " + totalPage));
        /*
        addChild(new GuiString(-10, 0, "1" ));
        addChild(new GuiString(-10, 10, "▲"));
        addChild(new GuiString(-10, 20, String.valueOf(focusPage+1)));
        addChild(new GuiString(-10, 30, "▼"));
        addChild(new GuiString(-10, 40, String.valueOf(totalPage)));
         */
        addChild(new GuiTexture(-2, -2, wight + 4, index + 10 + 4, texture));
    }

    @Override
    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        if (isSelected) {
            int totalPage = (int) Math.ceil(MaterialManager.INSTANCE.getSorters().size() / (float) Config.SHOW_FILTER_AND_SORTER_ELEMENTS.get());
            InputConstants.Key inputKey = InputConstants.getKey(keyCode, scanCode);
            if (KeyMappingHandler.nextElement.isActiveAndMatches(inputKey)) {
                setPage(((focusPage + 1) % totalPage));
                return true;
            }
            if (KeyMappingHandler.previousElement.isActiveAndMatches(inputKey)) {
                setPage(((focusPage + totalPage - 1) % totalPage));
                return true;
            }
        }
        return super.onKeyPress(keyCode, scanCode, modifiers);
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        graphics.pose().translate(0, 0, 1000);
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        graphics.pose().translate(0, 0, -1000);
    }

    public void setFocusSorter(MaterialSorter focusSorter) {
        this.focusSorter = focusSorter;
    }

    public MaterialSorter getFocusSorter() {
        return focusSorter;
    }
}
