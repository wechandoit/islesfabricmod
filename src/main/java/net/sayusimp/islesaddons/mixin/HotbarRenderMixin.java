package net.sayusimp.islesaddons.mixin;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.sayusimp.islesaddons.config.IslesAddonsConfig;
import net.sayusimp.islesaddons.utils.MiscUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HotbarRenderMixin extends DrawableHelper {

    // for crates
    @Inject(method = "renderHotbarItem", at = @At(value = "TAIL"))
    public void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci)
    {
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
}
