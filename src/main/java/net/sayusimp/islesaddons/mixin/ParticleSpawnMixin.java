package net.sayusimp.islesaddons.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.sayusimp.islesaddons.config.IslesAddonsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ParticleManager.class)
public class ParticleSpawnMixin {

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    public void renderParticles(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {
        if ((parameters.getType() == ParticleTypes.HAPPY_VILLAGER && IslesAddonsConfig.CONFIG.get("enable-green-qte-notifier", Boolean.class))
                || (parameters.getType() == ParticleTypes.DRAGON_BREATH && IslesAddonsConfig.CONFIG.get("enable-purple-qte-notifier", Boolean.class))
                || (parameters.getType() == ParticleTypes.FLAME && IslesAddonsConfig.CONFIG.get("enable-gold-qte-notifier", Boolean.class))) {
            Box particleBox = new Box(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5);
            // get closest entity of type ItemEntity to particleLoc
            List<Entity> nearbyDroppedItems = MinecraftClient.getInstance().world.getOtherEntities(MinecraftClient.getInstance().player, particleBox, (entity -> entity.getType() == EntityType.ITEM));
            for (Entity e : nearbyDroppedItems) {
                if (!isBlockTypeNearby(e, 1)) {
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("There is a QTE nearby...").styled(style -> style.withColor(TextColor.parse(getColorFromType(parameters.getType())))), false);
                }
            }
        }
    }

    private boolean isBlockTypeNearby(Entity e, int range) {
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    Block checkBlock = MinecraftClient.getInstance().world.getBlockState(e.getBlockPos().add(x, y, z)).getBlock();
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
