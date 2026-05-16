package falcon.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class FalconClient implements ClientModInitializer {
    private KeyBinding openUi;

    @Override
    public void onInitializeClient() {
        openUi = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.falcon.open_ui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.create(Identifier.of("falcon", "main"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openUi.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new FalconScreen());
            }
        });
    }
}
