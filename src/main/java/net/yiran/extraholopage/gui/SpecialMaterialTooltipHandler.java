package net.yiran.extraholopage.gui;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.module.schematic.OutcomeDefinition;
import se.mickelus.tetra.module.schematic.SchematicDefinition;

import java.util.*;
import java.util.stream.Collectors;

import static net.yiran.extraholopage.gui.ComponentHelper.literal;
import static net.yiran.extraholopage.gui.ComponentHelper.translatable;

public class SpecialMaterialTooltipHandler {
    public static Supplier<SpecialMaterialTooltipHandler> INSTANCE = Suppliers.memoize(SpecialMaterialTooltipHandler::new);
    public boolean needLoad = true;
    public Map<ItemPredicate, SchematicDefinition> SpecialMaterialPredicatesMap = new HashMap<>();

    public void init() {
        DataManager.instance.schematicData.onReload(() -> {
            needLoad = true;
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onScrollTooltip(ItemTooltipEvent event) {
        if (needLoad) {
            SpecialMaterialPredicatesMap.clear();
            Set<ItemPredicate> materialPredicates = DataManager.instance.materialData.getData().values()
                    .stream()
                    .map(materialData -> materialData.material.getPredicate())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            for (SchematicDefinition definition : DataManager.instance.schematicData.getData().values()) {
                for (OutcomeDefinition outcome : definition.outcomes) {
                    ItemPredicate outcomePredicate = outcome.material.getPredicate();
                    if (outcomePredicate != null && !materialPredicates.contains(outcomePredicate)) {
                        SpecialMaterialPredicatesMap.put(outcomePredicate, definition);
                    }
                }
            }
            needLoad = false;
        }
        Set<SchematicDefinition> schematics = new HashSet<>();
        SpecialMaterialPredicatesMap.forEach((itemPredicate, schematicDefinition) -> {
            if (itemPredicate.matches(event.getItemStack()))
                schematics.add(schematicDefinition);
        });
        if (schematics.isEmpty()) return;
        ArrayList<Component> tooltip = new ArrayList<>();
        if (Screen.hasShiftDown()) {
            for (SchematicDefinition schematic : schematics) {
                String schematicName = I18n.get("tetra/schematic/" + (schematic.localizationKey == null ? schematic.key : schematic.localizationKey) + ".name");
                String slots = String.join(I18n.get("ehp.special.material.tooltip.slots.merge.format"), Arrays.stream(schematic.slots).map(string -> I18n.get("tetra.slot." + string)).collect(Collectors.toSet()));
                tooltip.add(literal(I18n.get("ehp.special.material.tooltip.total.format", schematicName, slots)));
            }
            tooltip.sort(Comparator.comparing(Component::getString));
            tooltip.add(0,translatable("ehp.special.material.shift.press.tooltip"));
        } else {
            tooltip.add(translatable("ehp.special.material.shift.release.tooltip"));
        }

        event.getToolTip().addAll(1, tooltip);
    }
}
