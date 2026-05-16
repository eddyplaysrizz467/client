package falcon.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class FalconScreen extends Screen {
    public FalconScreen() {
        super(Text.translatable("screen.falcon.title"));
    }

    @Override
    protected void init() {
        int center = width / 2;
        int y = height / 2 - 12;

        addDrawableChild(ButtonWidget.builder(Text.literal("Status"), button -> runCommand("falcon status"))
                .dimensions(center - 155, y, 96, 20)
                .build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Enable"), button -> runCommand("falcon on"))
                .dimensions(center - 48, y, 96, 20)
                .build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Disable"), button -> runCommand("falcon off"))
                .dimensions(center + 59, y, 96, 20)
                .build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> close())
                .dimensions(center - 48, y + 54, 96, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int panelWidth = Math.min(360, width - 32);
        int panelX = (width - panelWidth) / 2;
        int panelY = height / 2 - 92;

        context.fill(panelX, panelY, panelX + panelWidth, panelY + 150, 0xCC101820);
        context.fill(panelX, panelY, panelX + panelWidth, panelY + 2, 0xFF4CC9F0);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, panelY + 14, 0xFFFFFF);
        context.drawTextWithShadow(textRenderer, "Movement alerts only. No punishments.", panelX + 22, panelY + 40, 0xCFE8EE);
        context.drawTextWithShadow(textRenderer, "Open with Right Shift.", panelX + 22, panelY + 58, 0xAAB8C2);
        context.drawTextWithShadow(textRenderer, "Buttons run server commands. Operator permission required.", panelX + 22, panelY + 122, 0xAAB8C2);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void runCommand(String command) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.player.networkHandler != null) {
            client.player.networkHandler.sendChatCommand(command);
        }
    }
}
