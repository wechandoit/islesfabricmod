package net.sayusimp.islesaddons.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleEffect;
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
        // green: minecraft:happy_villager
        // purple: minecraft:basic_smoke_particle
        // gold: minecraft:basic_flame_particle

        if ((parameters.getType() == ParticleTypes.HAPPY_VILLAGER && IslesAddonsConfig.CONFIG.get("enable-green-qte-notifier", Boolean.class))
                || (parameters.getType() == ParticleTypes.SMOKE && IslesAddonsConfig.CONFIG.get("enable-purple-qte-notifier", Boolean.class))
                || (parameters.getType() == ParticleTypes.FLAME && IslesAddonsConfig.CONFIG.get("enable-gold-qte-notifier", Boolean.class))) {
            Vec3d particleLoc = new Vec3d(x, y, z);
            Box particleBox = new Box(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5);
            // get closest entity of type ItemEntity to particleLoc
            List<Entity> nearbyDroppedItems = MinecraftClient.getInstance().world.getOtherEntities(MinecraftClient.getInstance().player, particleBox, (entity -> entity.getType() == EntityType.ITEM));
            for (Entity e : nearbyDroppedItems) {
                if (!isBlockTypeNearby(e, 1)) {
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("There is a QTE nearby...").styled(style -> style.withColor(TextColor.parse("#80FF80"))), false);
                }
            }
        }

        /*if (MinecraftClient.getInstance().player.getPos().distanceTo(particleLoc) < 5)
            System.out.println(parameters.asString() + ": x: " + x + " y: " + y + " z: " + z);*/
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
}
