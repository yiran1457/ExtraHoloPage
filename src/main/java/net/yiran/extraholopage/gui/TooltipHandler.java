package net.yiran.extraholopage.gui;

import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.yiran.extraholopage.Config;
import net.yiran.extraholopage.api.TooltipRegistries;
import se.mickelus.tetra.aspect.ItemAspect;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.module.data.AspectData;
import se.mickelus.tetra.module.data.EffectData;
import se.mickelus.tetra.module.data.MaterialData;
import se.mickelus.tetra.module.data.ToolData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static net.yiran.extraholopage.gui.ComponentHelper.*;
import static net.yiran.extraholopage.gui.MaterialTooltipHelper.*;

public class TooltipHandler {
    public static Supplier<TooltipHandler> INSTANCE = TooltipHandler::new;

    public TooltipHandler() {
        progressLength = Config.PROGRESS_SHOW_TICK.get();
        bar = Config.PROGRESS_BAR_SIZE.get();
        style = Config.PROGRESS_STYLE.get().substring(0, 1);
        showNeedAdvancedTooltip = Config.SHOW_NEED_ADVANCED_TOOLTIP.get();
    }

    public int size = 0;
    public int index = 0;
    public int scrollDelta = 0;
    public int nowProgress = 0;
    public int progressLength;
    public int bar;
    public String style;
    public boolean isSelected = false;
    public int lastScrolledTick = 0;
    public boolean showNeedAdvancedTooltip;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        lastScrolledTick--;
        if (isSelected) {
            isSelected = false;
        }
        if (size < 2) return;
        if (!isCtrl()) {
            nowProgress = 0;
        } else {
            if (lastScrolledTick > 1)
                updateProgress();
            else {
                nowProgress = reduce(nowProgress);
            }
        }
    }

    public void updateProgress() {
        int progress = scrollDelta / 6;
        scrollDelta -= progress;
        nowProgress += progress;
        if (nowProgress >= progressLength) {
            toNextPage();
        }
    }

    public void toNextPage() {
        nowProgress = 0;
        index++;
        scrollDelta = 0;
    }

    public int reduce(int number) {
        return --number < 0 ? 0 : number;
    }

    @SubscribeEvent
    public void onMouseScrolled(ScreenEvent.MouseScrolled event) {
        if (!isSelected) return;
        lastScrolledTick = 20;
        scrollDelta -= Math.min(0, (int) (event.getScrollDelta() * 8));
        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTooltip(ItemTooltipEvent event) {
        if (showNeedAdvancedTooltip && !event.getFlags().isAdvanced()) return;
        List<Component> originToolTip = event.getToolTip();
        List<Component> toolTip = new ArrayList<>();
        List<MaterialData> pMaterialData = DataManager.instance.materialData.getData().values().stream().filter(d -> d.material.getPredicate() != null && d.material.getPredicate().matches(event.getItemStack())).toList();
        size = pMaterialData.size();
        if (size == 0) return;
        if (!isCtrl()) {
            toolTip.add(Component.literal("§7[§8ctrl§7]§8 +"));
        } else {
            toolTip.add(Component.literal("§8[§fctrl§8]§f +"));
            if (index >= size) {
                index = 0;
            }
            if (size > 1) {
                addProgressBar(toolTip);
                isSelected = true;
            }

            MaterialData materialData = pMaterialData.get(index);
            if (Config.SHOW_CATEGORY.get())
                toolTip.add(addPrefix(translatable("tetra.holo.craft.materials.stat.category", color1), translatable("tetra.variant_category." + materialData.category + ".label", color1)));
            if (Config.SHOW_DURABILITY.get())
                addIfNotZero(toolTip, "durability", materialData.durability);
            if (Config.SHOW_BASIC.get()) {
                addIfNotZero(toolTip, "primary", materialData.primary);
                addIfNotZero(toolTip, "secondary", materialData.secondary);
                addIfNotZero(toolTip, "tertiary", materialData.tertiary);
            }
            if (Config.SHOW_TOOL_LEVEL.get()) {
                addIfNotZero(toolTip, "toolLevel", materialData.toolLevel);
                addIfNotZero(toolTip, "toolEfficiency", materialData.toolEfficiency);
            }
            if (Config.SHOW_XP_COST.get())
                addIfNotZero(toolTip, "experienceCost", materialData.experienceCost);
            if (Config.SHOW_INTEGRITY_INFO.get()) {
                addIfNotZero(toolTip, "integrityGain", materialData.integrityGain);
                addIfNotZero(toolTip, "integrityCost", materialData.integrityCost);
            }
            if (Config.SHOW_REQUIRED_TOOL.get())
                addRequiredToolTooltip(toolTip, materialData.requiredTools);
            if (Config.SHOW_ATTRIBUTES.get())
                addAttributeTooltip(toolTip, materialData.attributes);
            if (Config.SHOW_EFFECTS.get())
                addEffectsTooltip(toolTip, materialData.effects);
            if (Config.SHOW_ASPECTS.get())
                addAspectsTooltip(toolTip, materialData.aspects);
            if (Config.SHOW_IMPROVEMENTS.get())
                addImprovementsTooltip(toolTip, materialData.improvements);
        }
        originToolTip.addAll(1, toolTip);
    }

    public void addImprovementsTooltip(List<Component> toolTip, Map<String, Integer> improvements) {
        if (improvements == null) return;
        if (improvements.isEmpty()) return;
        int index = improvements.size();
        toolTip.add(addPrefix(translatable("tetra.holo.craft.materials.stat.improvements", color1)));
        for (Map.Entry<String, Integer> entry : improvements.entrySet()) {
            toolTip.add(addHeadTooltip(--index, translatable("tetra.improvement." + entry.getKey() + ".name", color1), literal(String.valueOf(entry.getValue()), color1)));
        }
    }

    public void addAspectsTooltip(List<Component> toolTip, AspectData aspects) {
        if (aspects == null) return;
        Map<ItemAspect, Float> levelMap = aspects.levelMap;
        if (levelMap.isEmpty()) return;
        int index = levelMap.size();
        toolTip.add(addPrefix(translatable("tetra.holo.craft.materials.stat.aspects", color1)));
        for (Map.Entry<ItemAspect, Float> entry : levelMap.entrySet()) {
            toolTip.add(addHeadTooltip(--index, setColor(entry.getKey().getLabel(), color1), literal(String.valueOf(entry.getValue().intValue()), color1)));
        }
    }

    public void addAttributeTooltip(List<Component> toolTip, Multimap<Attribute, AttributeModifier> attributes) {
        if (attributes == null) return;
        if (attributes.isEmpty()) return;
        toolTip.add(addPrefix(translatable("tetra.holo.craft.materials.stat.attributes", color1)));
        int index = attributes.values().size();
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            toolTip.add(addHeadTooltip(--index, translatable(entry.getKey().getDescriptionId(), color1), getAttributeModifierValue(entry.getValue())));
        }
    }

    public MutableComponent getAttributeModifierValue(AttributeModifier modifier) {
        int color = modifier.getAmount() > 0 ? color_good : color_bad;
        if (modifier.getOperation().toValue() == 0) {
            return literal(String.valueOf(modifier.getAmount()), color);
        } else {
            return literal(modifier.getAmount() * 100 + "%", color);
        }
    }

    public void addRequiredToolTooltip(List<Component> toolTip, ToolData toolData) {
        if (toolData == null) return;
        Map<ToolAction, Float> levelMap = toolData.levelMap;
        if (levelMap.isEmpty()) return;
        toolTip.add(addPrefix(translatable("tetra.holo.craft.materials.stat.requiredTools", color1)));
        int index = levelMap.size();
        for (Map.Entry<ToolAction, Float> entry : toolData.levelMap.entrySet()) {
            toolTip.add(addHeadTooltip(--index, translatable("tetra.tool." + entry.getKey().name(), color1), literal(entry.getValue().toString(), color1)));
        }
    }

    public void addEffectsTooltip(List<Component> toolTip, EffectData effectData) {
        if (effectData == null) return;
        Set<ItemEffect> effects = effectData.getValues();
        if (effects.isEmpty()) return;
        toolTip.add(addPrefix(translatable("tetra.holo.craft.materials.stat.effects", color1)));
        int index = effects.size();
        for (ItemEffect value : effects) {
            toolTip.add(addHeadTooltip(--index, TooltipRegistries.getTooltip(value).get(value, effectData.getLevel(value), effectData.getEfficiency(value))));
        }
    }

    public void addIfNotZero(List<Component> toolTip, String key, Number value) {
        if (value.intValue() != 0)
            toolTip.add(addPrefix(translatable("tetra.holo.craft.materials.stat." + key, color1), literal(value.toString(), color1)));
    }

    public void addProgressBar(List<Component> tooltips) {
        int arg0 = (nowProgress * bar) / progressLength;
        StringBuilder builder = new StringBuilder();
        builder.append("§7");
        for (int i = 0; i < bar; i++) {
            if (i == arg0)
                builder.append("§8");
            builder.append(style);
        }
        tooltips.add(append(fromStrings(color1, "[", (index + 1) + "/" + size, "]"), literal(builder.toString())));
    }

    public boolean isShift() {
        return Screen.hasShiftDown();
    }

    public boolean isCtrl() {
        return Screen.hasControlDown();
    }

    public boolean isAlt() {
        return Screen.hasAltDown();
    }
}
