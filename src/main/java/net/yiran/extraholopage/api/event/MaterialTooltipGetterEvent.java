package net.yiran.extraholopage.api.event;

import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.Event;
import se.mickelus.tetra.module.data.MaterialData;

import java.util.List;

public class MaterialTooltipGetterEvent extends Event {
    public List<Component> toolTip;
    public MaterialData materialData;
    public MaterialTooltipGetterEvent(List<Component> toolTip, MaterialData materialData) {
        this.toolTip = toolTip;
        this.materialData = materialData;
    }
    public List<Component> getToolTip() {
        return toolTip;
    }
    public MaterialData getMaterialData() {
        return materialData;
    }
    public void addTooltip(Component toolTip) {
        this.toolTip.add(toolTip);
    }
}
