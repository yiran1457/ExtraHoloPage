package net.yiran.extraholopage.api;

import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExtraHoloRegister {
    public static Map<Item,ExtraHoloBuilder> MAP = new LinkedHashMap<>();
    public static ExtraHoloBuilder register(Item item) {
        ExtraHoloBuilder builder = new ExtraHoloBuilder(item);
        MAP.put(item, builder);
        return builder;
    }
}
