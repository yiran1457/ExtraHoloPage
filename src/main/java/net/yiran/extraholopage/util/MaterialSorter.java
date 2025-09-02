package net.yiran.extraholopage.util;

import net.minecraft.client.resources.language.I18n;
import se.mickelus.tetra.module.data.MaterialData;

import java.util.function.Function;

public class MaterialSorter {
    public static MaterialSorter NONE = new MaterialSorter(materialData -> (float) 0, "ehp.sorter.NONE", 999);
    public Function<MaterialData, Number> priorityFn;
    public String name;
    public int index;

    public MaterialSorter(Function<MaterialData, Number> priorityFn, String nameKey) {
        this(priorityFn, nameKey, 0);
    }

    public MaterialSorter(Function<MaterialData, Number> priorityFn, String nameKey, int index) {
        this.priorityFn = priorityFn;
        this.name = nameKey;
        this.index = index;
    }

    public float getPriority(MaterialData data) {
        return priorityFn.apply(data).floatValue();
    }

    public String getName() {
        return I18n.get(name);
    }
}
