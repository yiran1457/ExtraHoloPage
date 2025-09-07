package net.yiran.extraholopage;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;
import se.mickelus.tetra.items.modular.impl.holo.ModularHolosphereItem;

public class KeyMappingHandler {
    public static KeyMapping openHolo = new KeyMapping(
            "ehp.open_holo_gui",
            GLFW.GLFW_KEY_H,
            "tetra.binding.group"
    );

    public static KeyMapping showTooltip = new KeyMapping(
            "ehp.show_material_tooltip",
            GLFW.GLFW_KEY_LEFT_CONTROL,
            "tetra.binding.group"
    );

    public static void onRegisterKeyMapping(RegisterKeyMappingsEvent event) {
        event.register(openHolo);
        event.register(showTooltip);
    }

    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (openHolo.consumeClick()) {
            ModularHolosphereItem.showGui();
        }
    }
}
