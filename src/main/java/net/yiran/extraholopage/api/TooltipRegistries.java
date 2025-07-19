package net.yiran.extraholopage.api;

import net.minecraft.network.chat.MutableComponent;
import se.mickelus.tetra.effect.ItemEffect;

import java.util.HashMap;
import java.util.Map;

import static net.yiran.extraholopage.gui.ComponentHelper.*;
import static net.yiran.extraholopage.gui.MaterialTooltipHelper.*;

public class TooltipRegistries {
    public static Map<ItemEffect, TooltipGetter> map = new HashMap<>();

    public static void register(ItemEffect effect, TooltipGetter getter) {
        map.put(effect, getter);
    }

    public static TooltipGetter getTooltip(ItemEffect effect) {
        return map.getOrDefault(effect, TooltipRegistries::defaultGetter);
    }

    public static MutableComponent defaultGetter(ItemEffect itemEffect, float level, float efficiency) {
        return getValue(translatable("tetra.stats." + itemEffect.getKey(),color1),literal("[ " + level + " , " + efficiency + " ]",color1));
    }

    @FunctionalInterface
    public interface TooltipGetter {
        MutableComponent get(ItemEffect itemEffect, float level, float efficiency);
    }
}
