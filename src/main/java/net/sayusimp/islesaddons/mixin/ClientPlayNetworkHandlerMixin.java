package net.sayusimp.islesaddons.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
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
                if (message.contains("[ITEM]") && !message.contains("Cornucopia")) {
                    if (message.contains("Raw") && MiscUtils.isWordFromListInString(message, EXPUtils.fishXPMap.keySet().stream().toList())) {
                        ci.cancel();
                        sendMessageToPlayerFromList(message, EXPUtils.fishXPMap);
                    } else if (!message.contains("Hide") && !message.contains("Sugarcane") && MiscUtils.isWordFromListInString(message, EXPUtils.cookingXPMap.keySet().stream().toList())) {
                        ci.cancel();
                        sendMessageToPlayerFromList(message, EXPUtils.cookingXPMap);
                    } else if ((message.contains("Log") || message.contains("Bark")) && MiscUtils.isWordFromListInString(message, EXPUtils.foragingXPMap.keySet().stream().toList())) {
                        ci.cancel();
                        if (message.contains("🪓"))
                            sendMessageToPlayerFromList(message, EXPUtils.foragingXPMap, true);
                        else
                            sendMessageToPlayerFromList(message, EXPUtils.foragingXPMap);
                    } else if (message.contains("Handle") && MiscUtils.isWordFromListInString(message, EXPUtils.foragingXPMap.keySet().stream().toList())) {
                        ci.cancel();
                        sendMessageToPlayerFromList(message, EXPUtils.carvingXPMap);
                    } else if (MiscUtils.isWordFromListInString(message, EXPUtils.farmingXPMap.keySet().stream().toList())) {
                        ci.cancel();
                        sendMessageToPlayerFromList(message, EXPUtils.farmingXPMap);
                    } else if ((message.contains("Ore") || message.contains("Chunk")
                            || message.contains("Coal") || message.contains("Ice")
                            || message.contains("Essence") || message.contains("Slab")
                            || message.contains("Cannonball")) && MiscUtils.isWordFromListInString(message, EXPUtils.miningXPMap.keySet().stream().toList())) {
                        ci.cancel();
                        sendMessageToPlayerFromList(message, EXPUtils.miningXPMap);
                    } else if ((message.contains("Molten") || message.contains("Bar")) && MiscUtils.isWordFromListInString(message, EXPUtils.smeltingXPMap.keySet().stream().toList())) {
                        ci.cancel();
                        sendMessageToPlayerFromList(message, EXPUtils.smeltingXPMap);
                    }
                }
            }
        }
    }

    private void sendMessageToPlayerFromList(String message, Map<String, Integer> xpmap) {
        sendMessageToPlayerFromList(message, xpmap, false, false);
    }

    private void sendMessageToPlayerFromList(String message, Map<String, Integer> xpmap, boolean isLumberBuff) {
        sendMessageToPlayerFromList(message, xpmap, isLumberBuff, false);
    }

    private void sendMessageToPlayerFromList(String message, Map<String,
            Integer> xpmap, boolean isLumberBuff,
                                             boolean isROLProc) {
        float multiplier = isLumberBuff ? 1.5F : 1;
        Stack stack = MiscUtils.getStackFromItemResourceString(message.substring(7));
        HashMap<String, Integer> itemAmountMap = new HashMap<>();
        int maxAmount = 0;

        while (stack.stream().count() >= 2) {
            String type = MiscUtils.getWordFromListInString(String.valueOf(stack.pop()), xpmap.keySet().stream().toList());
            int amount = Integer.parseInt(String.valueOf(stack.pop()));
            if (amount > maxAmount && xpmap.get(type) > 0) {
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
                    totalXP += xpmap.get(type) * itemAmountMap.get(type) * multiplier;
                } else {
                    totalXP += xpmap.get(type) * multiplier;
                    maxAmount = 1;
                }
        }

        if (MinecraftClient.getInstance().player != null) {
            int finalMaxAmount = maxAmount;
            Text msg = new LiteralText("+" + String.valueOf(totalXP) + " XP (" + String.join(", ", MiscUtils.getAmountListFromAmountMap(itemAmountMap)) + ")").styled(s -> s.withColor(TextColor.parse(getColorFromAmount(finalMaxAmount))));
            if (isLumberBuff)
                msg = msg.copy().append(new LiteralText(" +(x1.5 XP 🪓)").styled(style -> style.withColor(TextColor.parse("#C350C7"))));
            if (isROLProc)
                msg = msg.copy().append(new LiteralText(" (☘)").styled(style -> style.withColor(TextColor.parse("#FCF514"))));
            MinecraftClient.getInstance().player.sendMessage(msg, false);
        }
    }

    private String getColorFromAmount(int amount) {
        if (amount == 1) {
            return "#71D9AA";
        } else if (amount > 1 && amount <= 20) {
            return "#AC71D9";
        } else {
            return "#EBC738";
        }
    }

}
