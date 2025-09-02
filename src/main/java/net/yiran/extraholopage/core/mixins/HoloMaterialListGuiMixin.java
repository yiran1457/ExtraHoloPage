package net.yiran.extraholopage.core.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.yiran.extraholopage.Config;
import net.yiran.extraholopage.util.GuiFilter;
import net.yiran.extraholopage.util.GuiSorter;
import net.yiran.extraholopage.util.MyHoloMaterialGroupGui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloMaterialDetailGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloMaterialListGui;
import se.mickelus.tetra.module.data.MaterialData;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(value = HoloMaterialListGui.class, remap = false)
public abstract class HoloMaterialListGuiMixin extends GuiElement {
    public HoloMaterialListGuiMixin(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Unique
    public GuiSorter guiSorter;
    @Unique
    public GuiFilter guiFilter;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void jjj(int x, int y, int width, int height, CallbackInfo ci) {
        addChild(guiFilter = new GuiFilter(0, y - 36, width, height, this::updateGroups));
        addChild(guiSorter = new GuiSorter(x + 60, y - 36, width, height, this::updateGroups, guiFilter));
    }

    @Shadow
    protected abstract void onHover(MaterialData material);

    @Shadow
    protected abstract void onBlur(MaterialData material);

    @Shadow
    protected abstract void onSelect(MaterialData material);

    @Shadow
    @Final
    private HoloMaterialDetailGui detail;

    @Shadow
    @Final
    private GuiHorizontalLayoutGroup groups;

    @Shadow
    protected abstract void updateGroups();

    @WrapOperation(method = "updateGroups", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;", ordinal = 2))
    private <T extends MaterialData> Stream<T> rrr(Stream<T> instance, Predicate<? super T> predicate, Operation<Stream<T>> original) {

        return original.call(instance, predicate)
                .filter(m -> {
                    if (guiFilter != null) {
                        return guiFilter.forceFiler.shouldShow(m);
                    }
                    return true;
                })
                .sorted((a, b) -> {
                    if (guiSorter != null) {
                        return -Float.compare(guiSorter.forceSorter.getPriority(a), guiSorter.forceSorter.getPriority(b));
                    }
                    return 0;
                });
    }

    @WrapOperation(method = "updateGroups", at = @At(value = "INVOKE", target = "Lse/mickelus/mutil/gui/impl/GuiHorizontalLayoutGroup;addChild(Lse/mickelus/mutil/gui/GuiElement;)V"))
    private void iii(GuiHorizontalLayoutGroup instance, GuiElement child, Operation<Void> original, @Local Map.Entry<String, List<MaterialData>> entry, @Local int offset) {
        //entry.getValue().removeIf(materialData -> Objects.equals(materialData.category, "bone"));
        instance.addChild(new MyHoloMaterialGroupGui(0, 0, guiSorter, entry.getKey(), entry.getValue(), offset, this::onHover, this::onBlur, this::onSelect));
    }

    @Inject(method = "updateGroups", at = @At("HEAD"))
    private void zzz(CallbackInfo ci) {
        detail.setY(Config.HOLO_MATERIAL_LINE.get() < 4 ? 75 : Config.HOLO_MATERIAL_LINE.get() * 20);
    }

    @Inject(method = "onSelect", at = @At(value = "INVOKE_ASSIGN", target = "Lse/mickelus/mutil/gui/impl/GuiHorizontalLayoutGroup;getChildren(Ljava/lang/Class;)Ljava/util/List;"))
    private void ttt(MaterialData material, CallbackInfo ci) {
        this.groups.getChildren(MyHoloMaterialGroupGui.class).forEach((group) -> group.updateSelection(material));
    }

    @Inject(method = "onShow", at = @At(value = "INVOKE_ASSIGN", target = "Lse/mickelus/mutil/gui/impl/GuiHorizontalLayoutGroup;getChildren(Ljava/lang/Class;)Ljava/util/List;"))
    private void ooo(CallbackInfo ci) {
        this.groups.getChildren(MyHoloMaterialGroupGui.class).forEach(MyHoloMaterialGroupGui::animateIn);
    }

    @Inject(method = "reload", at = @At("HEAD"))
    private void hhh(CallbackInfo ci) {
        guiSorter.resetSorter();
    }
}
