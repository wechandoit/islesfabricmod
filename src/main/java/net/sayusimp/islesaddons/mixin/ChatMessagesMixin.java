package net.sayusimp.islesaddons.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import net.sayusimp.islesaddons.config.IslesAddonsConfig;
import net.sayusimp.islesaddons.utils.MiscUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(ChatHud.class)
public class ChatMessagesMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;I)V", at = @At("TAIL"))
    public void onChatMessage(Text text, int messageId, CallbackInfo ci)
    {

        if (IslesAddonsConfig.CONFIG.get("enable-rare-fishing-title", Boolean.class)) {
            String message = text.getString();

            List<String> rareFishingItems = Arrays.asList("Old Boots", "Fishing Casket", "Pufferfish Mask");

            if (message.contains("[ITEM]")) {
                // check for rare item
                if (MiscUtils.isWordFromListInString(message, rareFishingItems)) {
                    MinecraftClient.getInstance().inGameHud.setTitle(text);
                    MinecraftClient.getInstance().inGameHud.setDefaultTitleFade();
                }
            }
        }
    }
}
