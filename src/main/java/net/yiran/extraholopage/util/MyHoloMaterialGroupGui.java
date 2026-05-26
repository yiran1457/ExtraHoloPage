package net.yiran.extraholopage.util;

import net.minecraft.client.resources.language.I18n;
import net.yiran.extraholopage.Config;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloMaterialItemGui;
import se.mickelus.tetra.module.data.MaterialData;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class MyHoloMaterialGroupGui extends GuiElement {
    private final GuiElement materialsContainer;
    private final KeyframeAnimation labelAnimation;
    private final KeyframeAnimation[] itemAnimations;

    public MyHoloMaterialGroupGui(int x, int y, GuiSorter guiSorter, String category, List<MaterialData> materials, int offset, Consumer<MaterialData> onVariantHover, Consumer<MaterialData> onVariantBlur, Consumer<MaterialData> onVariantSelect) {
        super(x, y, 0, 50);
        GuiString label = new GuiStringSmall(0, 0, I18n.get("tetra.variant_category." + category + ".label"));
        label.setColor(8355711);
        this.addChild(label);
        this.materialsContainer = new GuiElement(0, 8, this.width, this.height);
        this.addChild(this.materialsContainer);
        this.labelAnimation = (new KeyframeAnimation(100, label))
                .applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX((float) (x - 5), (float) x))
                .withDelay(40 * offset / Config.HOLO_MATERIAL_LINE.get());
        int width = 0;
        this.itemAnimations = new KeyframeAnimation[materials.size()];

        for (int i = 0; i < materials.size(); ++i) {
            MaterialData material = materials.get(i);
            var item = new HoloMaterialSorterGui(i / Config.HOLO_MATERIAL_LINE.get() * 20, i % Config.HOLO_MATERIAL_LINE.get() * 20, guiSorter, material, onVariantHover, onVariantBlur, onVariantSelect);
            this.materialsContainer.addChild(item);
            this.itemAnimations[i] = (new KeyframeAnimation(80, item))
                    .applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY((float) (item.getY() - 5), (float) item.getY()))
                    .withDelay(40 + 40 * ((offset + i) / Config.HOLO_MATERIAL_LINE.get()));
            width = item.getX() + item.getWidth();
        }

        this.setWidth(Math.max(width, label.getWidth()));
    }

    public void updateSelection(MaterialData material) {
        this.materialsContainer.getChildren(HoloMaterialItemGui.class).forEach((variant) -> variant.updateSelection(material));
        this.materialsContainer.getChildren(HoloMaterialSorterGui.class).forEach((variant) -> variant.updateSelection(material));
    }

    public void animateIn() {
        this.labelAnimation.start();
        Arrays.stream(this.itemAnimations).forEach(KeyframeAnimation::start);
    }
}
