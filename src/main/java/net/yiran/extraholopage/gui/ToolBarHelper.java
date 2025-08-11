package net.yiran.extraholopage.gui;

import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.bar.GuiStatBarTool;
import se.mickelus.tetra.module.data.GlyphData;

import java.io.IOException;
import java.util.Map;

public class ToolBarHelper {
    public static Codec<ToolAction> T_CODEC = Codec.STRING.xmap(ToolAction::get, ToolAction::name).fieldOf("toolAction").codec();
    public static Codec<GlyphData> G_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC.optionalFieldOf("textureLocation", GuiTextures.toolActions).forGetter(i -> i.textureLocation),
                    Codec.INT.optionalFieldOf("textureX", 240).forGetter(i -> i.textureX),
                    Codec.INT.optionalFieldOf("textureY", 0).forGetter(i -> i.textureY),
                    Codec.INT.optionalFieldOf("tint", -1).forGetter(i -> i.textureY)
            ).apply(instance, GlyphData::new)
    );

    public static Map<ToolAction, GlyphData> parseG(Resource resource) {
        try (var reader = resource.openAsReader()) {
            var json = JsonParser.parseReader(reader);
            var resultG = G_CODEC.parse(JsonOps.INSTANCE, json).result();
            var resultT = T_CODEC.parse(JsonOps.INSTANCE, json).result();
            if (resultG.isPresent() && resultT.isPresent()) {
                return Map.of(resultT.get(), resultG.get());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static GuiStatBar parseT(Resource resource) {
        try (var reader = resource.openAsReader()) {
            var json = JsonParser.parseReader(reader);
            var result = T_CODEC.parse(JsonOps.INSTANCE, json).result();
            if (result.isPresent()) {
                var toolAction = result.get();
                return new GuiStatBarTool(0, 0, 59, toolAction).setContexts("tetra:holosphere", "tetra:workbench");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
