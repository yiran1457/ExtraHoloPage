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
import se.mickelus.mutil.gui.GuiButton;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiTexture;

import java.util.ArrayList;
import java.util.List;

public class GuiFilter extends GuiElement {
    public static ResourceLocation texture = new ResourceLocation(ExtraHoloPage.MODID, "textures/gui/back.png");
    public MaterialFilter focusFiler;
    public GuiFilter afterFilter;
    public Runnable updateGroups;
    public int focusPage = 0;
    public boolean isSelected = false;
    public IHoloMaterialListGui gui;

    public GuiFilter(int x, int y, int width, int height, Runnable runnable, GuiFilter beforeFilter, IHoloMaterialListGui holoMaterialListGui) {
        super(x, y, width, height);
        gui = holoMaterialListGui;
        focusFiler = MaterialFilter.NONE;
        this.afterFilter = beforeFilter;
        updateGroups = runnable;
        resetFilters();
    }

    public void resetFilters() {
        clearChildren();
        var focusString = I18n.get("ehp.filter.desc") + " : " + focusFiler.getName();
        var wight1 = Minecraft.getInstance().font.width(focusString);
        if (this.afterFilter != null) {
            this.afterFilter.setX(getX() + wight1 + 10);
            this.afterFilter.resetFilters();
        }
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
        int totalPage = (int) Math.ceil(MaterialManager.INSTANCE.getFilters().size() / (float) Config.SHOW_FILTER_AND_SORTER_ELEMENTS.get());
        List<MaterialFilter> list = new ArrayList<>();
        for (int i = page * Config.SHOW_FILTER_AND_SORTER_ELEMENTS.get(); i < (page + 1) * Config.SHOW_FILTER_AND_SORTER_ELEMENTS.get(); i++) {
            if (i >= MaterialManager.INSTANCE.getFilters().size()) break;
            list.add(MaterialManager.INSTANCE.getFilters().get(i));
        }
        clearChildren();
        var index = -10;
        var wight = 0;
        for (MaterialFilter filter : list) {
            if (filter == null) break;
            addChild(new GuiButton(0, index += 10, filter.getName(), () -> {
                isSelected = false;
                setFocusFiler(filter);
                updateGroups.run();
                resetFilters();
            }));
            wight = Math.max(wight, Minecraft.getInstance().font.width(filter.getName()));
        }
        String ss = I18n.get("ehp.page_info.format", focusPage + 1, totalPage);
        int ww = Minecraft.getInstance().font.width(ss);
        if (totalPage > 1) {
            addChild(new GuiString(0, index += 10, ss));
            wight = Math.max(wight, ww);
        }
        if (this.afterFilter != null) {
            this.afterFilter.setX(getX() + wight + 10);
            this.afterFilter.resetFilters();
        }
        var wight2 = wight;
        getChildren(GuiButton.class).forEach(button -> {
            button.setWidth(wight2);
        });
        getChildren(GuiString.class).forEach(string -> {
            string.setX((wight2 - ww) / 2);
        });
        addChild(new GuiTexture(-2, -2, wight + 4, index + 10 + 4, texture));
    }

    @Override
    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        if (isSelected) {
            int totalPage = (int) Math.ceil(MaterialManager.INSTANCE.getFilters().size() / (float) Config.SHOW_FILTER_AND_SORTER_ELEMENTS.get());
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

    public void setFocusFiler(MaterialFilter focusFiler) {
        this.focusFiler = focusFiler;
    }

    public MaterialFilter getFocusFiler() {
        return focusFiler;
    }
}
