package net.sayusimp.islesaddons.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.sayusimp.islesaddons.config.IslesAddonsConfig;
import net.sayusimp.islesaddons.utils.MiscUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class ItemRenderMixin extends Screen {

    protected ItemRenderMixin(Text text) {
        super(LiteralText.EMPTY);
        throw new RuntimeException("Mixin constructor called");
    }

    // on mouse clicks
    @Inject(method = "drawItem", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
            shift = At.Shift.AFTER))
    public void renderInventoryItem(ItemStack stack, int x, int y, String amountText, CallbackInfo ci) {
        if (IslesAddonsConfig.CONFIG.get("enable-crate-icon-amount", Boolean.class)) {
            if (MiscUtils.isCrate(stack)) {
                NbtCompound nbt = stack.getNbt();
                NbtCompound nbtDisplay = nbt.getCompound(ItemStack.DISPLAY_KEY);
                String rawLore = nbtDisplay.get(ItemStack.LORE_KEY).toString();
                int amount = MiscUtils.getAmountInCrate(rawLore);
                MiscUtils.renderAmountText(new MatrixStack(), stack, x, y, getZOffset(), amount);
            }
        }
    }

    // default click
    @Inject(method = "drawSlot",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
                    shift = At.Shift.AFTER))
    public void renderInventoryItem(MatrixStack matrices, Slot slot, CallbackInfo ci) {
        if (IslesAddonsConfig.CONFIG.get("enable-crate-icon-amount", Boolean.class)) {
            ItemStack stack = slot.getStack();
            if (MiscUtils.isCrate(stack)) {
                NbtCompound nbt = stack.getNbt();
                NbtCompound nbtDisplay = nbt.getCompound(ItemStack.DISPLAY_KEY);
                String rawLore = nbtDisplay.get(ItemStack.LORE_KEY).toString();
                int amount = MiscUtils.getAmountInCrate(rawLore);
                MiscUtils.renderAmountText(new MatrixStack(), stack, slot.x, slot.y, getZOffset(), amount);

            }
        }
    }
}
