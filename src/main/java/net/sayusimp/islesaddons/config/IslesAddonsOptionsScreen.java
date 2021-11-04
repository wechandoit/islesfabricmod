package net.sayusimp.islesaddons.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.Objects;

public class IslesAddonsOptionsScreen extends GameOptionsScreen {
    private ButtonListWidget buttons;

    public IslesAddonsOptionsScreen(Screen parent) {
        super(parent, (MinecraftClient.getInstance()).options, new TranslatableText("isles-addons.options"));
    }

    @Override
    protected void init() {
        this.buttons = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.buttons.addAll(IslesAddonsConfig.getOptions());
        addSelectableChild(this.buttons);
        addDrawableChild(new ButtonWidget(this.width / 2 - 200, this.height - 27, 200, 20, ScreenTexts.DONE , __ -> {
            IslesAddonsConfig.save();
            Objects.requireNonNull(this.client).setScreen(this.parent);
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        this.buttons.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
        List<OrderedText> tooltip = getHoveredButtonTooltip(this.buttons, mouseX, mouseY);
        if (tooltip != null)
            renderOrderedTooltip(matrices, tooltip, mouseX, mouseY);
    }

    @Override
    public void removed() {
        IslesAddonsConfig.save();
    }
}
