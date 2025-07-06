package net.yiran.extraholopage.api;

import net.minecraft.world.item.Item;

import java.util.HashMap;

public class ExtraHoloRegister {
    public static HashMap<Item,ExtraHoloBuilder> MAP = new HashMap<>();
    public static ExtraHoloBuilder register(Item item) {
        ExtraHoloBuilder builder = new ExtraHoloBuilder(item);
        MAP.put(item, builder);
        return builder;
    }
}
