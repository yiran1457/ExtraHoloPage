package net.yiran.extraholopage.gui;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.dynamic.DynamicModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.HoloRootBaseGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloBreadcrumbsGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HolosphereCraftState;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HolosphereEntryData;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HolosphereEntryStore;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.material.HoloMaterialListGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.schematic.HoloSchematicGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.schematic.HoloSchematicListGui;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.OutcomePreview;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Map;

public class ExtraHoloRootGui  extends HoloRootBaseGui {

    public static final char backBinding = 'q';
    private final HoloBreadcrumbsGui breadcrumbs;
    private final ExtraHoloItemsGui itemsView;
    private final HoloSchematicListGui schematicsView;
    private final HoloSchematicGui schematicView;
    private final HoloMaterialListGui materialsView;
    private final HolosphereCraftState state = new HolosphereCraftState(this::onNavigationChanged);
    private int depth = 0;
    private IModularItem item;
    private ItemStack itemStack;
    private String slot;
    private UpgradeSchematic schematic;
    private OutcomePreview openVariant;
    private boolean showingMaterials = false;

    public ExtraHoloRootGui (int x, int y) {
        super(x, y);
        this.breadcrumbs = new HoloBreadcrumbsGui(0, 0, this.width, this::onBreadcrumbClick);
        this.addChild(this.breadcrumbs);
        this.itemsView = new ExtraHoloItemsGui(0, 70, this.width, this.height, this::onItemSelect, this::onSlotSelect, this::onMaterialsSelect);
        this.addChild(this.itemsView);
        this.schematicsView = new HoloSchematicListGui(0, 13, this.width, this.height, this::onSchematicSelect);
        this.schematicsView.setVisible(false);
        this.addChild(this.schematicsView);
        this.schematicView = new HoloSchematicGui(0, 20, this.width, this.height, this::onVariantSelect);
        this.schematicView.setVisible(false);
        this.addChild(this.schematicView);
        this.materialsView = new HoloMaterialListGui(0, 20, this.width, this.height);
        this.materialsView.setVisible(false);
        this.addChild(this.materialsView);
    }
    private void onNavigationChanged() {
        HolosphereCraftState.ItemState selectedItemState = this.state.getSelectedItemState();
        if (this.state.isShowingMaterials()) {
            this.itemsView.setVisible(false);
            this.schematicsView.setVisible(false);
            this.schematicView.setVisible(false);
            this.materialsView.setVisible(true);
        } else if (this.state.getSelectedVariant() != null) {
            this.schematicView.openVariant(this.state.getSelectedVariant());
            this.schematicView.setVisible(true);
            this.schematicsView.setVisible(false);
            this.itemsView.setVisible(false);
            this.materialsView.setVisible(false);
        } else if (this.state.getSelectedSchematic() != null && selectedItemState != null) {
            this.schematicView.update(selectedItemState.workingStack(), this.state.getSelectedSlot(), this.state.getSelectedSchematic());
            this.schematicView.openVariant(null);
            this.schematicView.setVisible(true);
            this.schematicsView.setVisible(false);
            this.itemsView.setVisible(false);
            this.materialsView.setVisible(false);
        } else if (this.state.getSelectedSlot() != null && selectedItemState != null) {
            this.schematicsView.update(selectedItemState.itemData().getAsModularItem(), this.state.getSelectedSlot());
            this.schematicsView.setVisible(true);
            this.itemsView.setVisible(false);
            this.schematicView.setVisible(false);
            this.materialsView.setVisible(false);
        } else {
            this.itemsView.changeItem(this.state.getSelectedItem());
            this.itemsView.setVisible(true);
            this.schematicsView.setVisible(false);
            this.schematicView.setVisible(false);
            this.materialsView.setVisible(false);
            if (this.state.getDepth() > 1) {
                this.itemsView.animateBack();
            }
        }

        this.updateBreadcrumb();
    }


    private void onItemsLoaded() {
        this.state.setAvailableItems(HolosphereEntryStore.instance.getEntries());
        //this.itemsView.loadEntries(this.state.getSortedItemData());
        this.onItemSelect((String)null);
    }

    public boolean onCharType(char character, int modifiers) {
        if (super.onCharType(character, modifiers)) {
            return true;
        } else if (character == 'q' && this.state.getDepth() > 0) {
            this.onBreadcrumbClick(this.state.getDepth() - 1);
            return true;
        } else {
            return false;
        }
    }

    private void onBreadcrumbClick(int depth) {
        switch (depth) {
            case 0:
                this.onItemSelect(null);
                break;
            case 1:
                if (!this.state.isShowingMaterials()) {
                    this.onItemSelect(this.state.getSelectedItem());
                }
                break;
            case 2:
                this.onSlotSelect(this.state.getSelectedSlot());
                break;
            case 3:
                this.onSchematicSelect(this.state.getSelectedSchematic());
        }

    }

    private void onMaterialsSelect() {
        this.state.onMaterialsSelect();
    }

    private void onItemSelect(@Nullable String item) {
        this.state.onItemSelect(item);
    }

    private void onSlotSelect(String slot) {
        this.state.onSlotSelect(slot);
    }

    private void onSchematicSelect(UpgradeSchematic schematic) {
        this.state.onSchematicSelect(schematic);
    }

    private void onVariantSelect(OutcomePreview variant) {
        this.state.onVariantSelect(variant);
    }

    public void openFromWorkbench(IModularItem item, ItemStack itemStack, @Nullable String slot, @Nullable UpgradeSchematic schematic) {
        String key = HolosphereEntryStore.instance.getEntries().entrySet().stream()
                .filter((entry) -> entry.getValue().item.equals(item))
                .filter((entry) -> entry.getValue().archetype == null || entry.getValue().archetype.equals(DynamicModularItem.getArchetypeKey(itemStack)))
                .map(Map.Entry::getKey).findFirst().orElse(null);
        if (key != null) {
            this.state.openFromWorkbench(key, itemStack, slot, schematic);
            if (slot != null && schematic != null) {
                this.onSchematicSelect(schematic);
            } else {
                this.onItemSelect(null);
            }
        } else {
            this.onItemSelect(null);
        }

        this.breadcrumbs.animateOpen(true);
    }

    private void updateBreadcrumb() {
        LinkedList<String> result = new LinkedList<>();
        if (this.state.getDepth() > 0) {
            if (this.state.getSelectedItem() != null) {
                result.add(I18n.get("tetra.holo.craft.breadcrumb.root", new Object[0]));
                result.add(this.state.getSelectedItemName());
                if (this.state.getSelectedSlot() != null) {
                    result.add(this.state.getSelectedSlotName());
                }

                if (this.state.getSelectedSchematic() != null) {
                    result.add(this.state.getSelectedSchematic().getName());
                }

                if (this.state.getSelectedVariant() != null) {
                    result.add(this.state.getSelectedVariant().variantName);
                }
            } else if (this.state.isShowingMaterials()) {
                result.add(I18n.get("tetra.holo.craft.breadcrumb.root"));
                result.add(I18n.get("tetra.holo.craft.breadcrumb.materials"));
            }

            this.breadcrumbs.setVisible(true);
            this.breadcrumbs.setItems((String[])result.toArray(new String[0]));
        } else {
            this.breadcrumbs.setVisible(false);
        }

    }

    public void animateOpen() {
        switch (this.state.getDepth()) {
            case 0:
                this.itemsView.animateOpenAll();
                break;
            case 1:
                if (this.state.isShowingMaterials()) {
                    this.materialsView.animateOpen();
                } else {
                    this.itemsView.animateOpen();
                }
                break;
            case 2:
                this.schematicsView.animateOpen();
                break;
            case 3:
                this.schematicView.animateOpen();
        }

        this.breadcrumbs.animateOpen(this.state.getDepth() > 1);
    }

    public void onReload() {
        if (this.state.getSelectedSchematic() != null) {
            this.schematicView.setVisible(false);
            UpgradeSchematic newSchematic = SchematicRegistry.getSchematic(this.state.getSelectedSchematic().getKey());
            this.onSchematicSelect(newSchematic);
        } else if (this.state.getSelectedSlot() != null) {
            this.onSlotSelect(this.state.getSelectedSlot());
        }

        this.materialsView.reload();
    }
}
