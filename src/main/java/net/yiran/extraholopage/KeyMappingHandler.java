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
            "ehp.binding.group"
    );

    public static KeyMapping showTooltip = new KeyMapping(
            "ehp.show_material_tooltip",
            GLFW.GLFW_KEY_LEFT_CONTROL,
            "ehp.binding.group"
    );
    public static KeyMapping nextElement = new KeyMapping(
            "ehp.next_elements_page",
            GLFW.GLFW_KEY_RIGHT,
            "ehp.binding.group"
    );
    public static KeyMapping previousElement = new KeyMapping(
            "ehp.previous_elements_page",
            GLFW.GLFW_KEY_LEFT,
            "ehp.binding.group"
    );

    public static void onRegisterKeyMapping(RegisterKeyMappingsEvent event) {
        event.register(openHolo);
        event.register(showTooltip);
        event.register(nextElement);
        event.register(previousElement);
    }

    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (openHolo.consumeClick()) {
            ModularHolosphereItem.showGui();
        }
    }
}
