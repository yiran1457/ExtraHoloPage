package net.yiran.extraholopage.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.gui.GuiTextures;

import java.util.function.Supplier;

public class ExtraHoloBuilder {
    public static int[] ListX=new int[]{ 0, 1,-1, 2, 2,-2,-2, 3,-3, 4, 4,-4,-4, 5,-5, 6, 6,-6,-6};
    public static int[] ListY=new int[]{-1, 0, 0,-1, 1,-1, 1, 0, 0,-1, 1,-1, 1, 0, 0,-1, 1,-1, 1};
    public ItemStack itemSupplier;
    public Item item;
    public ResourceLocation texture = GuiTextures.holo;
    public int width=38;
    public int height=38;
    public int textureX=0;
    public int textureY=218;
    public ExtraHoloBuilder(Item pItem) {
        this.item = pItem;
        itemSupplier = new ItemStack(this.item);
    }

    public void setTexture(ResourceLocation texture,int w,int h,int u,int v) {
        this.texture = texture;
        this.width = w;
        this.height = h;
        this.textureX = u;
        this.textureY = v;
    }

    public void setTexture(ResourceLocation texture,int u,int v) {
        this.texture = texture;
        this.textureX = u;
        this.textureY = v;
    }
}
