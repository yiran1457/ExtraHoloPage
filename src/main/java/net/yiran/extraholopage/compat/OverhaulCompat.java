package net.yiran.extraholopage.compat;

import com.google.common.collect.Multimap;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.yiran.extraholopage.api.TooltipRegistries;
import net.yiran.extraholopage.gui.TooltipHandler;
import net.yiran.tmo.ContextData;
import net.yiran.tmo.core.IMaterialData;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.module.data.EffectData;
import se.mickelus.tetra.module.data.MaterialData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static net.yiran.extraholopage.gui.ComponentHelper.*;
import static net.yiran.extraholopage.gui.MaterialTooltipHelper.*;

public class OverhaulCompat {
    public static void addAttributeTooltip(List<Component> toolTip, MaterialData data) {
        if (getContextData(data).size() == 1)
            TooltipHandler.INSTANCE.get().addAttributeTooltip(toolTip, data.attributes);
        else {
            if (getContextData(data).values().stream().map(contextData -> contextData.attributes).filter(Objects::nonNull).mapToInt(Multimap::size).sum() == 0)
                return;
            toolTip.add(addPrefix(translatable("tetra.holo.craft.materials.stat.attributes", color1)));

            Map<MutableComponent, String> tooltipShowMap = new LinkedHashMap<>();

            for (Map.Entry<String, ContextData> contextDataEntry : getContextData(data).entrySet()) {
                if (contextDataEntry.getValue().attributes != null) {
                    for (Map.Entry<Attribute, AttributeModifier> entry : contextDataEntry.getValue().attributes.entries()) {
                        computeGroup(
                                tooltipShowMap,
                                getValue(translatable(entry.getKey().getDescriptionId(), color1), TooltipHandler.INSTANCE.get().getAttributeModifierValue(entry.getValue())),
                                contextDataEntry.getKey()
                        );
                    }
                }
            }

            toAddTooltip(toolTip, tooltipShowMap);
        }
    }

    public static void addEffectsTooltip(List<Component> toolTip, MaterialData data) {
        if (getContextData(data).size() == 1)
            TooltipHandler.INSTANCE.get().addEffectsTooltip(toolTip, data.effects);
        else {
            if (getContextData(data).values().stream().mapToInt(contextData -> contextData.effects.getValues().size()).sum() == 0)
                return;
            toolTip.add(addPrefix(translatable("tetra.holo.craft.materials.stat.effects", color1)));

            Map<MutableComponent, String> tooltipShowMap = new LinkedHashMap<>();

            for (Map.Entry<String, ContextData> contextDataEntry : getContextData(data).entrySet()) {
                EffectData effectData = contextDataEntry.getValue().effects;
                for (ItemEffect itemEffect : effectData.getValues()) {
                    computeGroup(
                            tooltipShowMap,
                            TooltipRegistries.getTooltip(itemEffect).get(itemEffect, effectData.getLevel(itemEffect), effectData.getEfficiency(itemEffect)),
                            contextDataEntry.getKey()
                    );
                }
            }

            toAddTooltip(toolTip, tooltipShowMap);
        }
    }

    public static void toAddTooltip(List<Component> toolTip,Map<MutableComponent, String> tooltipShowMap){
        int index = tooltipShowMap.size();
        for (Map.Entry<MutableComponent, String> entry : tooltipShowMap.entrySet()) {
            toolTip.add(addHeadTooltip(--index, entry.getKey().append(literal(I18n.get("overhaul.format", entry.getValue()), color2))));
        }

    }

    public static void computeGroup(Map<MutableComponent, String> tooltipShowMap, MutableComponent tooltip, String newGroup) {
        tooltipShowMap.compute(tooltip, (component, string) -> mergeGroup(component, string, newGroup));
    }

    public static String mergeGroup(MutableComponent component, String oldGroup, String newGroup) {
        String group = I18n.exists("overhaul." + newGroup) ? I18n.get("overhaul." + newGroup) : newGroup;
        if (oldGroup == null) {
            return group;
        }
        return I18n.get("overhaul.merge.format", oldGroup, group);
    }

    public static Map<String, ContextData> getContextData(MaterialData data) {
        return ((IMaterialData) data).getContextData();
    }

}
