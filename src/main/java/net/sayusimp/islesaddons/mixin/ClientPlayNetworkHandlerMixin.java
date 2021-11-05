package net.sayusimp.islesaddons.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TextColor;
import net.sayusimp.islesaddons.config.IslesAddonsConfig;
import net.sayusimp.islesaddons.util.EXPUtils;
import net.sayusimp.islesaddons.util.MiscUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    public void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {

        if (IslesAddonsConfig.CONFIG.get("enable-custom-message", Boolean.class)) {
            if (MiscUtils.onIsles()) {
                String message = packet.getMessage().getString();
                if (message.contains("[ITEM]")) {
                    if (message.contains("Raw") && MiscUtils.isWordFromListInString(message, EXPUtils.fishXPMap.keySet().stream().toList())) {
                        ci.cancel();
                        sendMessageToPlayerFromList(message, EXPUtils.fishXPMap, MinecraftClient.getInstance().player);
                    } else if (!message.contains("Hide") && MiscUtils.isWordFromListInString(message, EXPUtils.cookingXPMap.keySet().stream().toList())) {
                        ci.cancel();
                        sendMessageToPlayerFromList(message, EXPUtils.cookingXPMap, MinecraftClient.getInstance().player);
                    } else if ((message.contains("Log") || message.contains("Bark")) && MiscUtils.isWordFromListInString(message, EXPUtils.foragingXPMap.keySet().stream().toList())) {
                        ci.cancel();
                        sendMessageToPlayerFromList(message, EXPUtils.foragingXPMap, MinecraftClient.getInstance().player);
                    } else if (message.contains("Handle") && MiscUtils.isWordFromListInString(message, EXPUtils.foragingXPMap.keySet().stream().toList())) {
                        ci.cancel();
                        sendMessageToPlayerFromList(message, EXPUtils.carvingXPMap, MinecraftClient.getInstance().player);
                    } else if (MiscUtils.isWordFromListInString(message, EXPUtils.farmingXPMap.keySet().stream().toList())) {
                        ci.cancel();
                        sendMessageToPlayerFromList(message, EXPUtils.farmingXPMap, MinecraftClient.getInstance().player);
                    } else if ((message.contains("Ore") || message.contains("Chunk")
                            || message.contains("Coal") || message.contains("Ice")
                            || message.contains("Essence") || message.contains("Slab")
                            || message.contains("Cannonball")) && MiscUtils.isWordFromListInString(message, EXPUtils.miningXPMap.keySet().stream().toList())) {
                        ci.cancel();
                        sendMessageToPlayerFromList(message, EXPUtils.miningXPMap, MinecraftClient.getInstance().player);
                    }
                }
            }
        }
    }

    private void sendMessageToPlayerFromList(String message, Map<String, Integer> xpmap, ClientPlayerEntity player) {
        Stack stack = MiscUtils.getStackFromItemResourceString(message.substring(7));
        HashMap<String, Integer> itemAmountMap = new HashMap<>();
        int maxAmount = 0;

        while (stack.stream().count() >= 2) {
            String type = MiscUtils.getWordFromListInString(String.valueOf(stack.pop()), xpmap.keySet().stream().toList());
            int amount = Integer.parseInt(String.valueOf(stack.pop()));
            if (amount > maxAmount) {
                maxAmount = amount;
            }
            if (itemAmountMap.containsKey(type)) {
                itemAmountMap.put(type, itemAmountMap.get(type) + amount);
            } else {
                itemAmountMap.put(type, amount);
            }
        }

        int totalXP = 0;
        boolean hasBark = false, hasLog = false, isCooking = false;
        for (String type : itemAmountMap.keySet()) {
            if (type.contains("Bark")) hasBark = true;
            if (type.contains("Log")) hasLog = true;
            if (EXPUtils.cookingXPMap.keySet().contains(type)) isCooking = true;


            if (!(hasBark && hasLog))
                if (!isCooking) {
                    totalXP += xpmap.get(type) * itemAmountMap.get(type);
                } else {
                    totalXP += xpmap.get(type);
                    maxAmount = 1;
                }
        }

        if (MinecraftClient.getInstance().player != null) {
            int finalMaxAmount = maxAmount;
            MinecraftClient.getInstance().player.sendMessage(new LiteralText("+" + String.valueOf(totalXP) + " XP (" + String.join(", ", MiscUtils.getAmountListFromAmountMap(itemAmountMap)) + ")").styled(s -> s.withColor(TextColor.parse(getColorFromAmount(finalMaxAmount)))), false);
        }
    }

    private String getColorFromAmount(int amount) {
        if (amount == 1) {
            return "#71D9AA";
        } else if (amount > 1 && amount <= 10) {
            return "#AC71D9";
        } else {
            return "#EBC738";
        }
    }

}
