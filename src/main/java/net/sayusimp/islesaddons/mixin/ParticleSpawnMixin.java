package net.sayusimp.islesaddons.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SkullItem;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.sayusimp.islesaddons.config.IslesAddonsConfig;
import net.sayusimp.islesaddons.util.MiscUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(ParticleManager.class)
public class ParticleSpawnMixin {

    MinecraftClient client = MinecraftClient.getInstance();
    List<EntityType> qtetypes = List.of(EntityType.MAGMA_CUBE, EntityType.ITEM);
    private String skullSignature = "ngBEheIaXuWnZaiWkxNB8XPN8Nbuo08mDHPZWNEVs82GnKfsC6lLU/nED3VGeHUT/8pxWxwS1Zjfuh/ty0Yzd7jovVrI8qYNIrHidHoct4twJ1Nch8+NmeIY7aE9yy6EuI81x1MK90vhMmyNHYnalMYMMbZE7TizwvzKKKdpvvrK8xspzNednbyXpTbHsAUV90SjdNH5TQlaI61XT+TCPYjX7nBDBcqPLMWWzO/SVskQfPoufphgdw7uOugZPiULtoQy6TEYGIXOjvFmBcF0HlHUbhHKuxUSSr5wLhb5kMZQaUTkWAJIfH3V/1wU/vSG5T1IU4kcw3LOlFr3uUZHzzU6w+a3mAE+P7aBBsgtB0Qrw8sB/miqArNjEAz4p52Mqly1o+PTFhPvczTNzStWNHg6oDsYlzZ+xtqD/5XAr32YUHwUgFld22b4bOsYWLPd1dvT0GxMVEFDadXVYD5Omf2Qr+6dAbFbIcVN8qe+/Wo+AsYmr49VQxifCxZ3kg6RnomPSwNsIN+xGZzr42bPA4iHSMJ19uvhX1pvrw19tTJ6zvfCKgutQYx/hse5BDOADDc0ci4Og9U/aQGX33Q76SsW61Clg0a5g9rpqxTuTgcLUSMoaPvOp0goW8CetHR0DqqwzqHXIAZJNdD9bL1q3hEbzW7VwTduD5R98ELNb/Q=";

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    public void addParticles(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {

        if (parameters.getType() == ParticleTypes.WHITE_ASH) {
            Box particleBox = new Box(x - 0.5, (y - 1) - 0.5, z - 0.5, x + 0.5, (y - 1) + 0.5, z + 0.5);
            // get closest armorstand to particleLoc
            List<Entity> nearbyArmorStands = client.world.getOtherEntities(client.player, particleBox, (entity -> entity.getType() == EntityType.ARMOR_STAND && !entity.isGlowing()));
            for (Entity e : nearbyArmorStands) {
                if (!e.isGlowing()) {
                    Iterator<ItemStack> armorItems = e.getArmorItems().iterator();
                    while (armorItems.hasNext()) {
                        ItemStack stack = armorItems.next();
                        if (stack != null && !stack.toString().toUpperCase().contains("AIR") && stack.getNbt().get(SkullItem.SKULL_OWNER_KEY).toString().contains(skullSignature)) {
                            e.setGlowing(true);
                        }
                    }
                }
            }
        }

        if ((parameters.getType() == ParticleTypes.HAPPY_VILLAGER && IslesAddonsConfig.CONFIG.get("enable-green-qte-notifier", Boolean.class))
                || (parameters.getType() == ParticleTypes.DRAGON_BREATH && IslesAddonsConfig.CONFIG.get("enable-purple-qte-notifier", Boolean.class))
                || (parameters.getType() == ParticleTypes.FLAME && IslesAddonsConfig.CONFIG.get("enable-gold-qte-notifier", Boolean.class))) {
            Box particleBox = new Box(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5);
            // get closest entity of type ItemEntity to particleLoc
            List<Entity> nearbyClickBoxes = client.world.getOtherEntities(client.player, particleBox, (entity -> qtetypes.contains(entity.getType())));
            List<EntityType> nearbyEntityTypes = new ArrayList<>();
            nearbyClickBoxes.forEach(entity -> {
                if (!nearbyEntityTypes.contains(entity.getType())) nearbyEntityTypes.add(entity.getType());
            });
            if (nearbyEntityTypes.containsAll(qtetypes)) {
                for (Entity e : nearbyClickBoxes) {
                    if (!isBlockTypeNearby(e, 1)) {
                        client.player.sendMessage(new LiteralText("There is a QTE nearby...").styled(style -> style.withColor(TextColor.parse(getColorFromType(parameters.getType())))), false);
                    }
                }
            }
        }
    }

    private boolean isBlockTypeNearby(Entity e, int range) {
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    Block checkBlock = client.world.getBlockState(e.getBlockPos().add(x, y, z)).getBlock();
                    if (checkBlock.equals(Blocks.CAMPFIRE) || checkBlock.equals(Blocks.IRON_TRAPDOOR)) return true;
                }
            }
        }
        return false;
    }

    private String getColorFromType(ParticleType<?> type) {
        if (type == ParticleTypes.HAPPY_VILLAGER) {
            return "#80FF80";
        } else if (type == ParticleTypes.DRAGON_BREATH) {
            return "#AC71D9";
        } else {
            return "#EBC738";
        }
    }
}
