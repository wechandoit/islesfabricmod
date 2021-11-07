package net.sayusimp.islesaddons.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.sayusimp.islesaddons.config.IslesAddonsConfig;
import net.sayusimp.islesaddons.util.MiscUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatMessagesMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private List<ChatHudLine<OrderedText>> visibleMessages;
    @Shadow
    @Final
    private List<ChatHudLine<Text>> messages;

    @Shadow
    public abstract void addMessage(Text message);

    private int amount;
    private boolean sentStackMessage = false;
    private Text lastMessage = null;
    private Text stackMessage = null;
    private int topLine;

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;I)V", at = @At("TAIL"))
    public void onChatMessage(Text text, int messageId, CallbackInfo ci) {
        if (IslesAddonsConfig.CONFIG.get("enable-stack-chat", Boolean.class)) {
            if (!sentStackMessage) {
                Text stackText = null;
                if (stackMessage != null && text.getString().equals(stackMessage.getString())) {
                    if (amount == 1) removeLastSimilarMessage(stackMessage);
                    if (stackMessage != null) removeLastSimilarMessage(stackMessage);
                    if (lastMessage != null) removeLastSimilarMessage(lastMessage);
                    amount++;

                    Text amountString = new LiteralText(" (x" + amount + ")").styled(s -> s.withColor(TextColor.parse("#4DE3E3")));
                    if (text.getSiblings().isEmpty()) {
                        stackText = text.copy().setStyle(text.getStyle()).append(amountString);
                    } else {
                        text.getSiblings().add(amountString);
                        stackText = text;
                    }
                } else {
                    amount = 1;
                    stackMessage = text;
                    lastMessage = null;
                }
                if (amount > 1 && stackText != null && !ci.isCancelled()) {
                    sentStackMessage = true;
                    addMessage(stackText);
                    lastMessage = stackText;
                }
            } else {
                sentStackMessage = false;
            }
        }


        if (IslesAddonsConfig.CONFIG.get("enable-rare-fishing-title", Boolean.class)) {
            String message = text.getString();
            List<String> rareFishingItems = Arrays.asList("Old Boots", "Fishing Casket", "Pufferfish Mask");

            if (message.contains("[ITEM]") && MiscUtils.isWordFromListInString(message, rareFishingItems)) {
                MinecraftClient.getInstance().inGameHud.setTitle(text);
                MinecraftClient.getInstance().inGameHud.setDefaultTitleFade();
            }
        }
    }

    @Inject(method = "clear", at = @At("HEAD"))
    public void onClearMessages(boolean clearHistory, CallbackInfo ci) {
        messages.clear();
    }

    private void removeLastSimilarMessage(Text similar) {
        int line = -1;
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (messages.get(i).getText().getString().equals(similar.getString())) {
                line = i;
                System.out.println(messages.get(i));
            }
        }
        if (line >= 0) removeMessage(line);
    }

    private void removeMessage(int line) {
        if (messages.size() > line) messages.remove(line);
        if (visibleMessages.size() > line) visibleMessages.remove(line);
        System.out.println(messages);
    }
}
