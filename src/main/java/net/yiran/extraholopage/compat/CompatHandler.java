package net.yiran.extraholopage.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.yiran.extraholopage.api.ExtraHoloRegister;

public class CompatHandler {
    public static ResourceLocation HOLO = new ResourceLocation("extraholopage", "textures/gui/holo.png");
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("art_of_forging"))
            AoF_Compat();
        if (ModList.get().isLoaded("secrets_of_forging_revelations"))
            SoF_Compat();
    }

    public static void AoF_Compat() {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("art_of_forging", "modular_artifact"));
        ExtraHoloRegister.register(item)
                .setTexture(HOLO,0,0);

    }

    public static void SoF_Compat() {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tetra", "modular_polearm"));
        ExtraHoloRegister.register(item)
                .setTexture(HOLO,38,0);
    }
}
