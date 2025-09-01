package net.yiran.extraholopage.core.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.yiran.extraholopage.Config;
import net.yiran.extraholopage.ExtraHoloPage;
import net.yiran.extraholopage.core.MyHoloMaterialGroupGui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

@Mixin(value = HoloMaterialListGui.class, remap = false)
public abstract class HoloMaterialListGuiMixin {
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

    @WrapOperation(method = "updateGroups", at = @At(value = "INVOKE", target = "Lse/mickelus/mutil/gui/impl/GuiHorizontalLayoutGroup;addChild(Lse/mickelus/mutil/gui/GuiElement;)V"))
    private void iii(GuiHorizontalLayoutGroup instance, GuiElement child, Operation<Void> original, @Local Map.Entry<String, List<MaterialData>> entry, @Local int offset) {
        instance.addChild(new MyHoloMaterialGroupGui(0, 0, entry.getKey(), entry.getValue(), offset, this::onHover, this::onBlur, this::onSelect));
    }

    @Inject(method = "updateGroups", at = @At("HEAD"))
    private void zzz(CallbackInfo ci) {
        detail.setY(Config.HOLO_MATERIAL_LINE.get() < 4 ? 75 : Config.HOLO_MATERIAL_LINE.get() * 20);
    }

    @Inject(method = "onSelect", at = @At(value = "INVOKE_ASSIGN", target = "Lse/mickelus/mutil/gui/impl/GuiHorizontalLayoutGroup;getChildren(Ljava/lang/Class;)Ljava/util/List;"))
    private void ttt(MaterialData material, CallbackInfo ci) {
        ExtraHoloPage.LOGGER.info("HoloMaterialListGuiMixin.onSelect");
        this.groups.getChildren(MyHoloMaterialGroupGui.class).forEach((group) -> group.updateSelection(material));
    }

    @Inject(method = "onShow", at = @At(value = "INVOKE_ASSIGN", target = "Lse/mickelus/mutil/gui/impl/GuiHorizontalLayoutGroup;getChildren(Ljava/lang/Class;)Ljava/util/List;"))
    private void ooo(CallbackInfo ci) {
        this.groups.getChildren(MyHoloMaterialGroupGui.class).forEach(MyHoloMaterialGroupGui::animateIn);

    }
}
