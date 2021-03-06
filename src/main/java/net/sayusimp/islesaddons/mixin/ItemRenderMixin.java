package net.sayusimp.islesaddons.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.sayusimp.islesaddons.util.MiscUtils;
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
    @Inject(method = "drawItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", shift = At.Shift.AFTER))
    public void renderInventoryItem(ItemStack stack, int x, int y, String amountText, CallbackInfo ci) {
        MiscUtils.renderAmountOnCrates(stack, x, y, getZOffset());
    }

    // default click
    @Inject(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", shift = At.Shift.AFTER))
    public void renderInventoryItem(MatrixStack matrices, Slot slot, CallbackInfo ci) {
        MiscUtils.renderAmountOnCrates(slot.getStack(), slot.x , slot.y, getZOffset());
    }
}
