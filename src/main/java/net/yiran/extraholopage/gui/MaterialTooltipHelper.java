package net.yiran.extraholopage.gui;

import net.minecraft.network.chat.MutableComponent;

import java.awt.Color;

import static net.yiran.extraholopage.gui.ComponentHelper.*;

public class MaterialTooltipHelper {
    public static int color0 = Color.WHITE.getRGB();
    public static int color1 = Color.LIGHT_GRAY.getRGB();
    public static int color2 = Color.GRAY.getRGB();
    public static int color3 = Color.DARK_GRAY.getRGB();
    public static int color_good = new Color(80, 169, 80).getRGB();
    public static int color_bad = new Color(197, 96, 96).getRGB();

    public static MutableComponent PREFIX = literal("» ", color3);
    public static MutableComponent MIDDLE = literal("  ├ ", color2);
    public static MutableComponent END = literal("  └ ", color2);

    public static MutableComponent getValue(MutableComponent component1, int color, MutableComponent component2) {
        return append(component1, literal(": ", color), component2);
    }

    public static MutableComponent getValue(MutableComponent component1, MutableComponent component2) {
        return getValue(component1, color2, component2);
    }

    public static MutableComponent getHeadTooltip(int index) {
        return index == 0 ? getEnd() : getMiddle();
    }

    public static MutableComponent addHeadTooltip(int index, MutableComponent... components) {
        return append(getHeadTooltip(index), components);
    }

    public static MutableComponent addHeadTooltip(int index, MutableComponent component1, MutableComponent component2) {
        return append(getHeadTooltip(index), getValue(component1, component2));
    }

    public static MutableComponent getMiddle() {
        return MIDDLE.copy();
    }

    public static MutableComponent getEnd() {
        return END.copy();
    }

    public static MutableComponent getPrefix() {
        return PREFIX.copy();
    }

    public static MutableComponent addPrefix(MutableComponent component) {
        return append(getPrefix(), component);
    }

    public static MutableComponent addPrefix(MutableComponent component1, MutableComponent component2) {
        return append(getPrefix(), getValue(component1, component2));
    }
}
