package net.yiran.extraholopage.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.gui.GuiTextures;

public class ExtraHoloBuilder {
    public static int[] pattern = {-1, 1, -1, 1, 0, 0};
    public ItemStack itemSupplier;
    public Item item;
    public ResourceLocation texture = GuiTextures.holo;
    public boolean alwaysShowName = false;
    public int width = 38;
    public int height = 38;
    public int textureX = 0;
    public int textureY = 218;

    public static int getX(int index) {
        if (index == 0) {
            return 0;
        }
        int block = (index - 1) / 6;
        int offset = (index - 1) % 6;
        if (offset < 2) {
            int n = 2 * block + 1;
            return (offset == 0) ? n : -n;
        } else {
            int n = 2 * block + 2;
            return (offset < 4) ? n : -n;
        }
    }

    public static int getY(int index) {
        if (index == 0) return -1;
        if (index == 1) return 0;
        if (index == 2) return 0;
        int offset = (index - 3) % 6;
        return pattern[offset];
    }

    public ExtraHoloBuilder(Item pItem) {
        this.item = pItem;
    }

    public void alwaysShowName() {
        this.alwaysShowName = true;
    }

    public ItemStack getItemSupplier() {
        return itemSupplier != null ? itemSupplier : this.item.getDefaultInstance();
    }

    public void setItemSupplier(ItemStack itemSupplier) {
        this.itemSupplier = itemSupplier;
    }

    public void setTexture(ResourceLocation texture, int w, int h, int u, int v) {
        this.texture = texture;
        this.width = w;
        this.height = h;
        this.textureX = u;
        this.textureY = v;
    }

    public void setTexture(ResourceLocation texture, int u, int v) {
        this.texture = texture;
        this.textureX = u;
        this.textureY = v;
    }
}
