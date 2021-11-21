package net.sayusimp.islesaddons.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.SkullItem;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockUpdateS2CPacket.class)
public class BlockStateMixin {

    @Shadow
    private BlockState state;
    @Shadow
    private BlockPos pos;

    private String skullSignature = "ngBEheIaXuWnZaiWkxNB8XPN8Nbuo08mDHPZWNEVs82GnKfsC6lLU/nED3VGeHUT/8pxWxwS1Zjfuh/ty0Yzd7jovVrI8qYNIrHidHoct4twJ1Nch8+NmeIY7aE9yy6EuI81x1MK90vhMmyNHYnalMYMMbZE7TizwvzKKKdpvvrK8xspzNednbyXpTbHsAUV90SjdNH5TQlaI61XT+TCPYjX7nBDBcqPLMWWzO/SVskQfPoufphgdw7uOugZPiULtoQy6TEYGIXOjvFmBcF0HlHUbhHKuxUSSr5wLhb5kMZQaUTkWAJIfH3V/1wU/vSG5T1IU4kcw3LOlFr3uUZHzzU6w+a3mAE+P7aBBsgtB0Qrw8sB/miqArNjEAz4p52Mqly1o+PTFhPvczTNzStWNHg6oDsYlzZ+xtqD/5XAr32YUHwUgFld22b4bOsYWLPd1dvT0GxMVEFDadXVYD5Omf2Qr+6dAbFbIcVN8qe+/Wo+AsYmr49VQxifCxZ3kg6RnomPSwNsIN+xGZzr42bPA4iHSMJ19uvhX1pvrw19tTJ6zvfCKgutQYx/hse5BDOADDc0ci4Og9U/aQGX33Q76SsW61Clg0a5g9rpqxTuTgcLUSMoaPvOp0goW8CetHR0DqqwzqHXIAZJNdD9bL1q3hEbzW7VwTduD5R98ELNb/Q=";
    private String normalSkullSignature = "Jww17uVUFA/sdd9L3gwVg2hlCVnfcmtMChQlM5amZ03dvS5XIUOZG1mBHbbnx8BIxDjs+Mq9dxXtLV7aKpDO15vvF7SjZzWvir4Izapb4VJYpLzwgpMsqaRRbM8Y1SR6WvCjP2k99FaBoWPMh/PsA6Df1q/YtxcHcWPvPuvhS+hDh5uhdIiWBK5UyDD+Yo7MFIxzObNwSXLwQ1WLshVRRqe+N/OSVNgJAV4mG6wbj1D+rLyP7JbZdKbM9Jhuu5dXU5trnk+zgkv2ab06vJnnqA1wQigKFjLYVllDtPPTeBNhHaeXtWId3JHzeza669t/R53+ufTqJ2CieHfCsTKOs6McC/h4bqGOXs4bw14I9xr/t7T2biDx7ChLvQ4RZtVRepRVppILUnpIwjLUaW3hF+Bp3Owso36K8cnpGZ/EVElJO9WIQjkIgCRnsnI5/VygDHsY2Rii+XQa86ekXgU/QcJLWlle9CVWdbewRB6gMFUtL7VoiJQKidKw9+ELykysvfh7kMoeuqbGx6m44HnCWnGA6+BpWAhsDJD4Sp6tfyh4bAfU7QfUOEH6ro2A3itq2jAWnBwkspOnIVW8bRU+ox2P5onQjBPaILH9VCImINftrhNVyniXcEHxp7T8spasa4AVauXx1wM0nOMkjA4S5nylDwAHJcv8awn80W1ijls=";
    @Inject(method = "getState", at = @At("TAIL"))
    private void init(CallbackInfoReturnable<BlockState> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!state.isAir() && state.hasBlockEntity() && client.world.getBlockEntity(pos).getType() == BlockEntityType.SKULL) {
            SkullBlockEntity skullBlockEntity = (SkullBlockEntity) client.world.getBlockEntity(pos);
            if (skullBlockEntity.toInitialChunkDataNbt().get(SkullItem.SKULL_OWNER_KEY).toString().contains(skullSignature)) {
                client.world.setBlockState(pos.mutableCopy().add(0,100-pos.getY(), 0), Blocks.REDSTONE_BLOCK.getDefaultState());
            } else if (skullBlockEntity.toInitialChunkDataNbt().get(SkullItem.SKULL_OWNER_KEY).toString().contains(normalSkullSignature)) {
                client.world.setBlockState(pos.mutableCopy().add(0,100-pos.getY(), 0), Blocks.AIR.getDefaultState());
            }
        }
    }

}
