package net.yiran.extraholopage;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<Boolean> HOLO_SHOW_ALL_RARITY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IGNORE_HOLO_ITEM;

    static {
        HOLO_SHOW_ALL_RARITY = BUILDER
                .comment("显示所有稀有度的原理图属性。", "Display schematic properties for all rarities.")
                .define("holoShowAllRarity", true);
        IGNORE_HOLO_ITEM = BUILDER
                .comment("忽略全息球物品需求。", "ignoring holo item requirements.")
                .define("ignoreHoloItem", true);
        SPEC = BUILDER.build();

    }
}
