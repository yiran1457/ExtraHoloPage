package net.yiran.extraholopage.api;

import net.minecraftforge.common.ToolActions;
import net.yiran.extraholopage.util.MaterialFilter;
import net.yiran.extraholopage.util.MaterialSorter;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.module.data.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MaterialManager {
    public static final MaterialManager INSTANCE = new MaterialManager();
    public List<MaterialSorter> sorters = new ArrayList<>();
    public boolean needReSortSorters = true;
    public List<MaterialFilter> filters = new ArrayList<>();
    public boolean needReSortFilters = true;

    public MaterialManager() {
        registerSorter(MaterialSorter.NONE);
        registerSorter(materialData -> materialData.primary, "ehp.sorter.primary");
        registerSorter(materialData -> materialData.secondary, "ehp.sorter.secondary");
        registerSorter(materialData -> materialData.tertiary, "ehp.sorter.tertiary");
        registerSorter(materialData -> materialData.magicCapacity, "ehp.sorter.magicCapacity");
        registerSorter(materialData -> materialData.durability, "ehp.sorter.durability");
        registerSorter(materialData -> materialData.toolLevel, "ehp.sorter.toolLevel");
        registerSorter(materialData -> materialData.experienceCost, "ehp.sorter.experienceCost");
        registerSorter(materialData -> materialData.requiredTools == null ? 0 : materialData.requiredTools.getLevel(TetraToolActions.hammer), "ehp.sorter.requiredTools.hammer");
        registerSorter(materialData -> materialData.requiredTools == null ? 0 : materialData.requiredTools.getLevel(TetraToolActions.cut), "ehp.sorter.requiredTools.cut");
        registerSorter(materialData -> materialData.requiredTools == null ? 0 : materialData.requiredTools.getLevel(ToolActions.AXE_DIG), "ehp.sorter.requiredTools.axe");

        registerFilter(MaterialFilter.NONE);
        registerFilter(materialData -> !materialData.effects.getValues().isEmpty(), "ehp.filter.hasEffect");
        registerFilter(materialData -> materialData.attributes != null, "ehp.filter.hasAttribute");
    }

    public void removeFilter(String name) {
        filters.removeIf(filter -> filter.name.equals(name));
    }

    public void registerFilter(MaterialFilter filter) {
        filters.add(filter);
        needReSortFilters = true;
    }

    public void registerFilter(Function<MaterialData, Boolean> filter, String name, int index) {
        registerFilter(new MaterialFilter(filter, name, index));
    }

    public void registerFilter(Function<MaterialData, Boolean> filter, String name) {
        registerFilter(filter, name, 0);
    }

    public void removeSorter(String name) {
        sorters.removeIf(sorter -> sorter.name.equals(name));
    }

    public void registerSorter(MaterialSorter sorter) {
        sorters.add(sorter);
        needReSortSorters = true;
    }

    public void registerSorter(Function<MaterialData, Number> sorter, String name, int index) {
        registerSorter(new MaterialSorter(sorter, name, index));
    }

    public void registerSorter(Function<MaterialData, Number> sorter, String name) {
        registerSorter(sorter, name, 0);
    }

    public List<MaterialSorter> getSorters() {
        if (needReSortSorters) {
            sorters = sorters.stream().sorted((a, b) -> b.index - a.index).collect(Collectors.toList());
            needReSortSorters = false;
        }
        return sorters;
    }

    public List<MaterialFilter> getFilters() {
        if (needReSortFilters) {
            filters = filters.stream().sorted((a, b) -> b.index - a.index).collect(Collectors.toList());
            needReSortFilters = false;
        }
        return filters;
    }
}
