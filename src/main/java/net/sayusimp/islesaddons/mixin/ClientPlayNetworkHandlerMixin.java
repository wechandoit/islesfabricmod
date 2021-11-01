package net.sayusimp.islesaddons.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TextColor;
import net.sayusimp.islesaddons.utils.FishUtils;
import net.sayusimp.islesaddons.utils.MiscUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    public void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {

        if (MiscUtils.onIsles()) {
            String message = packet.getMessage().getString();
            if (message.contains("[ITEM]")) {
                //MinecraftClient.getInstance().player.sendChatMessage("no");
                // check for rare item
                if (message.contains("Raw") && MiscUtils.isWordFromListInString(message, FishUtils.fishXPMap.keySet().stream().toList())) {
                    ci.cancel();
                    sendMessageToPlayerFromList(message, FishUtils.fishXPMap, MinecraftClient.getInstance().player);
                } else if (MiscUtils.isWordFromListInString(message, FishUtils.seafoodXPMap.keySet().stream().toList())) {
                    ci.cancel();
                    sendMessageToPlayerFromList(message, FishUtils.seafoodXPMap, MinecraftClient.getInstance().player);
                }
            }
        }
    }

    private boolean sendMessageToPlayerFromList(String message, Map<String, Integer> xpmap, ClientPlayerEntity player)
    {
        String type = MiscUtils.getWordFromListInString(message, xpmap.keySet().stream().toList());
        int amount = Integer.parseInt(message.split(" ")[1]);

        if(MinecraftClient.getInstance().player != null)
        {
            MinecraftClient.getInstance().player.sendMessage(new LiteralText("+" + String.valueOf(xpmap.get(type) * amount) + " XP (" + amount + " " + type + ")").styled(s -> s.withColor(TextColor.parse(getColorFromAmount(amount)))), false);
            return true;
        } else
        {
            return false;
        }
    }

    private String getColorFromAmount(int amount)
    {
        if (amount == 1)
        {
            return "#71D9AA";
        } else if (amount > 1 && amount <= 10)
        {
            return "#AC71D9";
        } else
        {
            return "#EBC738";
        }
    }

}
