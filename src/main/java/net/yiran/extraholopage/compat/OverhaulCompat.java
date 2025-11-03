package net.yiran.extraholopage.compat;

import com.google.common.collect.Multimap;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.yiran.extraholopage.api.TooltipRegistries;
import net.yiran.extraholopage.gui.TooltipHandler;
import net.yiran.tmo.ContextData;
import net.yiran.tmo.core.IMaterialData;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.module.data.EffectData;
import se.mickelus.tetra.module.data.MaterialData;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static net.yiran.extraholopage.gui.ComponentHelper.literal;
import static net.yiran.extraholopage.gui.ComponentHelper.translatable;
import static net.yiran.extraholopage.gui.MaterialTooltipHelper.*;

public class OverhaulCompat {
    public static void addAttributeTooltip(List<Component> toolTip, MaterialData data) {
        if (getContextData(data).size() == 1)
            TooltipHandler.INSTANCE.get().addAttributeTooltip(toolTip, data.attributes);
        else {
            int index = getContextData(data).values().stream().map(contextData -> contextData.attributes).filter(Objects::nonNull).mapToInt(Multimap::size).sum();
            if (index == 0)return;
            toolTip.add(addPrefix(translatable("tetra.holo.craft.materials.stat.attributes", color1)));
            for (Map.Entry<String, ContextData> contextDataEntry : getContextData(data).entrySet()) {
                if (contextDataEntry.getValue().attributes != null) {
                    String group = getDisplayGroup(contextDataEntry.getKey());
                    for (Map.Entry<Attribute, AttributeModifier> entry : contextDataEntry.getValue().attributes.entries()) {
                        toolTip.add(addHeadTooltip(--index, translatable(entry.getKey().getDescriptionId(), color1), TooltipHandler.INSTANCE.get().getAttributeModifierValue(entry.getValue())).append(literal(group, color2)));
                    }
                }
            }
        }
    }

    public static void addEffectsTooltip(List<Component> toolTip, MaterialData data) {
        if (getContextData(data).size() == 1)
            TooltipHandler.INSTANCE.get().addEffectsTooltip(toolTip, data.effects);
        else {
            int index = getContextData(data).values().stream().filter(Objects::nonNull).mapToInt(contextData -> contextData.effects.getValues().size()).sum();
            if (index == 0)return;
            toolTip.add(addPrefix(translatable("tetra.holo.craft.materials.stat.effects", color1)));
            for (Map.Entry<String, ContextData> contextDataEntry : getContextData(data).entrySet()) {
                EffectData effectData = contextDataEntry.getValue().effects;
                String group = getDisplayGroup(contextDataEntry.getKey());
                for (ItemEffect itemEffect : effectData.getValues()) {
                    toolTip.add(addHeadTooltip(--index, TooltipRegistries.getTooltip(itemEffect).get(itemEffect, effectData.getLevel(itemEffect), effectData.getEfficiency(itemEffect)).append(literal(group, color2))));
                }
            }
        }
    }

    public static String getDisplayGroup(String group) {
        String groupKey = "overhaul." + group;
        return I18n.get("overhaul.format", I18n.exists(groupKey) ? I18n.get(groupKey) : group);
    }

    public static Map<String, ContextData> getContextData(MaterialData data) {
        return ((IMaterialData) data).getContextData();
    }

}
