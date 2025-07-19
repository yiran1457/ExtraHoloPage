package net.yiran.extraholopage.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ComponentHelper {

    public static MutableComponent append(MutableComponent component, MutableComponent... components) {
        for (MutableComponent mutableComponent : components) {
            component.append(mutableComponent);
        }
        return component;
    }

    public static MutableComponent literal(String text, int color) {
        return setColor(literal(text), color);
    }

    public static MutableComponent literal(String text) {
        return Component.literal(text);
    }

    public static MutableComponent translatable(String langKey, int color) {
        return setColor(translatable(langKey), color);
    }

    public static MutableComponent translatable(String langKey) {
        return Component.translatable(langKey);
    }

    public static MutableComponent fromStrings(String... string) {
        return fromStrings(false, string);
    }

    public static MutableComponent fromStrings(int color, String... string) {
        return setColor(fromStrings(string), color);
    }

    public static MutableComponent fromStrings(int color, boolean isLangKey, String... string) {
        return setColor(fromStrings(isLangKey, string), color);
    }

    public static MutableComponent fromStrings(boolean isLangKey, String... string) {
        String arg = String.join("", string);
        return isLangKey ? Component.translatable(arg) : Component.literal(arg);
    }

    public static MutableComponent setColor(MutableComponent component, int color) {
        return component.withStyle(style -> style.withColor(color));
    }

}
