package net.yiran.extraholopage.core;

import net.minecraft.client.gui.Gui;
import net.yiran.extraholopage.util.GuiFilter;
import net.yiran.extraholopage.util.GuiSorter;

public interface IHoloMaterialListGui {
    GuiSorter getGuiSorter();

    GuiFilter[] getHoloFilters();

    default void resetAllSF(){
        getGuiSorter().resetSorter();
        for (GuiFilter filter : getHoloFilters()) {
            filter.resetFilters();
        }
    }
}
