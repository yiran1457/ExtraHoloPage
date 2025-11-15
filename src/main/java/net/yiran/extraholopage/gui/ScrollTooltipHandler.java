package net.yiran.extraholopage.gui;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.scroll.ScrollData;
import se.mickelus.tetra.blocks.scroll.ScrollItem;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.module.schematic.SchematicDefinition;
import se.mickelus.tetra.module.schematic.requirement.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ScrollTooltipHandler {
    public static Supplier<ScrollTooltipHandler> INSTANCE = Suppliers.memoize(ScrollTooltipHandler::new);
    public Multimap<String, SchematicDefinition> locked2Schematic = HashMultimap.create();
    public Multimap<SchematicDefinition, String> schematic2module = HashMultimap.create();

    public static Field and;
    public static Field or;
    public static Field module;

    static {
        try {
            and = AndRequirement.class.getDeclaredField("requirements");
            and.setAccessible(true);
            or = OrRequirement.class.getDeclaredField("requirements");
            or.setAccessible(true);
            module = ModuleRequirement.class.getDeclaredField("moduleKey");
            module.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        DataManager.instance.schematicData.onReload(INSTANCE.get()::onSchematicReload);
    }

    public static CraftingRequirement[] getRequirements(CraftingRequirement requirement) {
        try {
            if (requirement instanceof AndRequirement) {
                return (CraftingRequirement[]) and.get(requirement);
            }
            return (CraftingRequirement[]) or.get(requirement);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getModuleKey(CraftingRequirement schematic, String[] keySuffixes) {
        try {
            String result = (String) module.get(schematic);
            if (result != null) {
                result = result.replace("_right", "");
                result = result.replace("_left", "");
                for (String keySuffix : keySuffixes) {
                    result = result.replace(keySuffix, "");
                }
            }
            return result;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getLocked(CraftingRequirement requirement) {
        List<String> locked = new ArrayList<>();
        if (requirement instanceof AndRequirement || requirement instanceof OrRequirement) {
            for (CraftingRequirement craftingRequirement : getRequirements(requirement)) {
                locked.addAll(getLocked(craftingRequirement));
            }
        } else if (requirement instanceof LockedRequirement) {
            return List.of(((LockedRequirement) requirement).key.toString());
        }
        return locked;
    }

    public static List<String> getModule(CraftingRequirement requirement, String[] keySuffixes) {
        List<String> locked = new ArrayList<>();
        if (requirement instanceof AndRequirement || requirement instanceof OrRequirement) {
            for (CraftingRequirement craftingRequirement : getRequirements(requirement)) {
                locked.addAll(getModule(craftingRequirement, keySuffixes));
            }
        } else if (requirement instanceof ModuleRequirement) {
            String moduleKey = getModuleKey(requirement, keySuffixes);
            if (moduleKey != null) {
                return List.of(getModuleKey(requirement, keySuffixes));
            }
            return List.of();
        }
        return locked;
    }

    public void onSchematicReload() {
        LogUtils.getLogger().debug("onSchematicReload-开始解析全部原理图");
        locked2Schematic.clear();
        for (SchematicDefinition value : DataManager.instance.schematicData.getData().values()) {
            for (String string : getLocked(value.requirement)) {
                locked2Schematic.put(string, value);
            }
            for (String string : getModule(value.requirement, value.keySuffixes)) {
                schematic2module.put(value, string);
            }
        }
        LogUtils.getLogger().debug("onSchematicReload-locked数量：{}", schematic2module.size());
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.is(ScrollItem.instance)) return;
        Set<String> strings = new HashSet<>();
        ScrollData.read(stack).schematics
                .stream()
                .map(ResourceLocation::toString)
                .map(locked2Schematic::get)
                .flatMap(Collection::stream)
                .forEach(schematicDefinition -> {
                    Set<String> setSlots = Arrays.stream(schematicDefinition.slots).map(string -> I18n.get("tetra.slot." + string)).collect(Collectors.toSet());
                    String slots;
                    if (setSlots.size() > 3 && !Screen.hasShiftDown()) {
                        slots = String.join(I18n.get("ehp.scroll.tooltip.slots.merge.format"), setSlots.stream().limit(3).toList()) + I18n.get("ehp.scroll.tooltip.slots.extra.format", setSlots.size() - 3);
                    } else {
                        slots = String.join(I18n.get("ehp.scroll.tooltip.slots.merge.format"), setSlots) + "§r";
                    }
                    String sl = schematicDefinition.localizationKey == null ? schematicDefinition.key : schematicDefinition.localizationKey;
                    String name = I18n.get("tetra/schematic/" + sl + ".name") + "§r";
                    String module = String.join(I18n.get("ehp.scroll.tooltip.slots.merge.format"), schematic2module.get(schematicDefinition).stream().map(string -> I18n.get("tetra.module." + string + ".name")).collect(Collectors.toSet()));
                    if (module.isEmpty()) {
                        module = I18n.get("ehp.scroll.tooltip.module.empty");
                    }
                    event.getToolTip().add(1, Component.translatable("ehp.scroll.tooltip.total.format",name ,  slots , module));
                });
    }
}
