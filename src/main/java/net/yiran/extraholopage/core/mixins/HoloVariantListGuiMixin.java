package net.yiran.extraholopage.core.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.yiran.extraholopage.util.MyHoloVariantGroupGui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.tetra.gui.stats.sorting.IStatSorter;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloVariantListGui;
import se.mickelus.tetra.module.schematic.OutcomePreview;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(value = HoloVariantListGui.class,remap = false)
public class HoloVariantListGuiMixin {
    @Shadow private IStatSorter sorter;

    @Shadow @Final private Consumer<OutcomePreview> onVariantHover;

    @Shadow @Final private Consumer<OutcomePreview> onVariantBlur;

    @Shadow @Final private Consumer<OutcomePreview> onVariantSelect;

    @Shadow @Final private GuiHorizontalLayoutGroup groups;

    @WrapOperation(method = "update()V",at = @At(value = "INVOKE", target = "Lse/mickelus/mutil/gui/impl/GuiHorizontalLayoutGroup;addChild(Lse/mickelus/mutil/gui/GuiElement;)V"))
    private void aaa(GuiHorizontalLayoutGroup instance, GuiElement child, Operation<Void> original, @Local Player player, @Local Map.Entry<String, List<OutcomePreview>> entry, @Local int offset){
        instance.addChild(new MyHoloVariantGroupGui(0, 0, entry.getKey(), entry.getValue(), offset, this.sorter, player, this.onVariantHover, this.onVariantBlur, this.onVariantSelect));
    }

    @Inject(method = "updateSelection",at = @At("HEAD"))
    private void bbb(OutcomePreview outcome, CallbackInfo ci){
        this.groups.getChildren(MyHoloVariantGroupGui.class).forEach((group) -> group.updateSelection(outcome));
    }

    @Inject(method = "onShow",at = @At("HEAD"))
    private void ccc(CallbackInfo ci){
        this.groups.getChildren(MyHoloVariantGroupGui.class).forEach(MyHoloVariantGroupGui::animateIn);
    }

}
