package net.yiran.extraholopage.core.mixins;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.yiran.extraholopage.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.holo.ModularHolosphereItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloMaterialApplicable;
import se.mickelus.tetra.module.schematic.SchematicRarity;
import se.mickelus.tetra.module.schematic.SchematicType;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

import java.util.List;

@Mixin(HoloMaterialApplicable.class)
public class HoloMaterialApplicableMixin {
    @Shadow private List<Component> tooltip;

    @Shadow private IModularItem item;

    @Shadow private ItemStack itemStack;

    @Shadow private String slot;

    @Shadow private UpgradeSchematic schematic;

    @Inject(
            method = "update",
            at = @At(value = "INVOKE", target = "Lse/mickelus/tetra/items/modular/impl/holo/ModularHolosphereItem;findHolosphere(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/item/ItemStack;"),
            cancellable = true)
    public void update(Level level, BlockPos pos, WorkbenchTile blockEntity, ItemStack itemStack, String slot, UpgradeSchematic schematic, Player playerEntity, CallbackInfo ci){

        ItemStack holosphereStack = ModularHolosphereItem.findHolosphere(playerEntity, level, pos);
        if ((schematic.getType() == SchematicType.major || schematic.getType() == SchematicType.minor)
                &&(Config.HOLO_SHOW_ALL_RARITY.get() ||schematic.getRarity() == SchematicRarity.basic)) {
            if ((!holosphereStack.isEmpty()||Config.IGNORE_HOLO_ITEM.get()) && itemStack.getItem() instanceof IModularItem) {
                this.tooltip.add(Component.translatable("tetra.holo.craft.holosphere_shortcut"));
                this.item = (IModularItem)itemStack.getItem();
                this.itemStack = itemStack;
                this.slot = slot;
                this.schematic = schematic;
            } else {
                this.tooltip.add(Component.translatable("tetra.holo.craft.holosphere_shortcut_disabled"));
                this.tooltip.add(Component.translatable("tetra.holo.craft.holosphere_shortcut_missing").withStyle(ChatFormatting.DARK_GRAY));
            }
        } else {
            this.tooltip.add(Component.translatable("tetra.holo.craft.holosphere_shortcut_disabled"));
            this.tooltip.add(Component.translatable("tetra.holo.craft.holosphere_shortcut_unavailable").withStyle(ChatFormatting.DARK_GRAY));
        }
        ci.cancel();
    }
}
