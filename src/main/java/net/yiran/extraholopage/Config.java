package net.yiran.extraholopage;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<Boolean> HOLO_SHOW_ALL_RARITY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IGNORE_HOLO_ITEM;
    public static final ForgeConfigSpec.ConfigValue<Integer> MODIFY_IMPROVEMENT_SPACING;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_MATERIAL_TOOLTIP;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_NEED_ADVANCED_TOOLTIP;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_REQUIRED_TOOL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_DURABILITY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_CATEGORY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_BASIC;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_XP_COST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_EFFECTS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_ATTRIBUTES;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_ASPECTS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_IMPROVEMENTS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_TOOL_LEVEL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_INTEGRITY_INFO;
    public static final ForgeConfigSpec.ConfigValue<Integer> PROGRESS_BAR_SIZE;
    public static final ForgeConfigSpec.ConfigValue<Integer> PROGRESS_SHOW_TICK;
    public static final ForgeConfigSpec.ConfigValue<String> PROGRESS_STYLE;
    public static final ForgeConfigSpec.ConfigValue<Integer> COLOR_1;
    public static final ForgeConfigSpec.ConfigValue<Integer> COLOR_2;
    public static final ForgeConfigSpec.ConfigValue<Integer> COLOR_3;
    public static final ForgeConfigSpec.ConfigValue<Integer> COLOR_GOOD;
    public static final ForgeConfigSpec.ConfigValue<Integer> COLOR_BAD;

    static {
        HOLO_SHOW_ALL_RARITY = BUILDER
                .comment("显示所有稀有度的原理图属性。")
                .comment("Display schematic properties for all rarities.")
                .define("holoShowAllRarity", true);
        IGNORE_HOLO_ITEM = BUILDER
                .comment("忽略全息球物品需求。")
                .comment("ignoring holo item requirements.")
                .define("ignoreHoloItem", true);
        MODIFY_IMPROVEMENT_SPACING = BUILDER
                .comment("修改改进间距。")
                .comment("Modify Improvement spacing.")
                .define("modify_improvement_spacing", 28);

        BUILDER.push("Tooltip");
        ENABLE_MATERIAL_TOOLTIP = BUILDER
                .comment("是否启用添加材料Tooltip-修改此配置需重启生效")
                .define("enableMaterialTooltip", false);
        SHOW_NEED_ADVANCED_TOOLTIP = BUILDER
                .comment("是否需要在打开高级物品提示的时候才显示tooltip-修改此配置需重启生效")
                .define("showNeedAdvancedTooltip", true);
        SHOW_REQUIRED_TOOL = BUILDER
                .comment("是否在tooltip里面提示需求工具等级")
                .define("showRequiredTool", true);
        SHOW_DURABILITY =BUILDER
                .comment("是否在tooltip里面提示材料基础耐久度")
                .define("showDurability", true);
        SHOW_CATEGORY = BUILDER
                .comment("是否在tooltip里面提示材料类型")
                .define("showCategory", true);
        SHOW_BASIC = BUILDER
                .comment("是否在tooltip里提示材料三维")
                .define("showBasic", true);
        SHOW_XP_COST = BUILDER
                .comment("是否在tooltip里提示经验消耗")
                .define("showXpCost", true);
        SHOW_EFFECTS = BUILDER
                .comment("是否在tooltip里面提示材料自带effects")
                .define("showEffects", true);
        SHOW_ATTRIBUTES = BUILDER
                .comment("是否在tooltip里面提示材料自带attributes")
                .define("showAttributes", true);
        SHOW_ASPECTS = BUILDER
                .comment("是否在tooltip里面提示材料自带aspects")
                .define("showAspects", true);
        SHOW_IMPROVEMENTS = BUILDER
                .comment("是否在tooltip里面提示材料自带improvements")
                .define("showImprovements", true);
        SHOW_TOOL_LEVEL = BUILDER
                .comment("是否在tooltip里面提示材料工具等级及其效率")
                .define("showToolLevel", true);
        SHOW_INTEGRITY_INFO = BUILDER
                .comment("是否在tooltip里面提示材料完整度奖励与花费")
                .define("showIntegrityInfo", true);

        BUILDER.push("ProgressBar");
        PROGRESS_BAR_SIZE = BUILDER
                .comment("设置进度条的长度")
                .define("progressBarSize", 30);
        PROGRESS_SHOW_TICK = BUILDER
                .comment("设置进度条的显示时间")
                .define("progressShowTick", 50);
        PROGRESS_STYLE = BUILDER
                .comment("设置进度条的样式,单个字符")
                .define("progressStyle", "|");
        BUILDER.pop();
        BUILDER.push("Color");
        BUILDER.comment("设置颜色，需重启生效，为0则使用预设颜色");
        COLOR_1 = BUILDER.define("color1", 0);
        COLOR_2 = BUILDER.define("color2", 0);
        COLOR_3 = BUILDER.define("color3", 0);
        COLOR_GOOD = BUILDER.define("colorGood", 0);
        COLOR_BAD = BUILDER.define("colorBad", 0);
        BUILDER.pop(2);

        SPEC = BUILDER.build();

    }
}
