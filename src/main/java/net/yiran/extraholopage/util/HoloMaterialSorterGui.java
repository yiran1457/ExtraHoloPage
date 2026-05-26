package net.yiran.extraholopage.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.yiran.extraholopage.Config;
import net.yiran.extraholopage.gui.TooltipHandler;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiItem;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiItemRolling;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.data.MaterialData;

import java.util.List;
import java.util.function.Consumer;

public class HoloMaterialSorterGui extends GuiClickable {
    protected GuiTexture backdrop;
    protected MaterialData material;
    protected Consumer<MaterialData> onHover;
    protected Consumer<MaterialData> onBlur;
    protected boolean isMuted = false;
    GuiItemRolling icon;
    GuiStringOutline stringOutline;

    public HoloMaterialSorterGui(int x, int y, GuiSorter guiSorter, MaterialData material, Consumer<MaterialData> onHover, Consumer<MaterialData> onBlur, Consumer<MaterialData> onSelect) {
        super(x, y, 16, 16, () -> onSelect.accept(material));
        this.material = material;
        this.onHover = onHover;
        this.onBlur = onBlur;
        this.backdrop = new GuiTexture(0, 0, 16, 16, 52, 16, GuiTextures.workbench);
        this.addChild(this.backdrop);
        this.icon = (new GuiItemRolling(0, 0)).setTooltip(false).setCountVisibility(GuiItem.CountMode.never).setItems(material.material.getApplicableItemStacks());
        this.addChild(this.icon);
        if (guiSorter != null && guiSorter.getFocusSorter() != MaterialSorter.NONE) {
            var sting = String.valueOf(guiSorter.getFocusSorter().getPriority(material));
            if (sting.endsWith(".0")) {
                sting = sting.substring(0, sting.length() - 2);
            }
            addChild(stringOutline = new GuiStringOutline(8 - Minecraft.getInstance().font.width(sting) / 2, 4, sting, material.tints.glyph));
        }
    }

    @Override
    protected void drawChildren(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        if (stringOutline == null || Screen.hasShiftDown()) {
            this.icon.setVisible(true);
        } else {
            this.icon.setVisible(false);
        }
        super.drawChildren(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }

    public void updateSelection(MaterialData material) {
        this.isMuted = material != null && !this.material.equals(material);
        this.backdrop.setColor(this.isMuted ? 8355711 : 16777215);
    }

    protected void onFocus() {
        super.onFocus();
        this.onHover.accept(this.material);
        this.backdrop.setColor(16777164);
    }

    protected void onBlur() {
        super.onBlur();
        this.onBlur.accept(this.material);
        this.backdrop.setColor(this.isMuted ? 8355711 : 16777215);
    }

    @Override
    public List<Component> getTooltipLines() {
        return hasFocus() && Config.SHOW_MATERIAL_DATA_HOLO.get() ? TooltipHandler.INSTANCE.get().getMaterialTooltip(material) : null;
    }
}
