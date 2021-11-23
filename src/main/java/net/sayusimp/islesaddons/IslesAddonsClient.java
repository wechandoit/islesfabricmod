package net.sayusimp.islesaddons;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SkullItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Box;
import net.sayusimp.islesaddons.config.IslesAddonsConfig;
import net.sayusimp.islesaddons.util.DiscordUtils;
import net.sayusimp.islesaddons.util.MiscUtils;

import java.time.OffsetDateTime;
import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class IslesAddonsClient implements PreLaunchEntrypoint, ClientModInitializer {
    public static MinecraftClient client = MinecraftClient.getInstance();
    public static IPCClient ipcClient = new IPCClient(904055870483222528L);
    public static boolean isFishing = false;
    public static Entity fishingEntity = null;
    public static Entity fishingHoloEntity = null;
    public static String islesLocation = "";

    private static int discordAppCount = 0;
    private static String previousIP = "";
    private static int clientTick = 1;
    private static boolean justStartedFishing = false;
    private String skullSignature = "ngBEheIaXuWnZaiWkxNB8XPN8Nbuo08mDHPZWNEVs82GnKfsC6lLU/nED3VGeHUT/8pxWxwS1Zjfuh/ty0Yzd7jovVrI8qYNIrHidHoct4twJ1Nch8+NmeIY7aE9yy6EuI81x1MK90vhMmyNHYnalMYMMbZE7TizwvzKKKdpvvrK8xspzNednbyXpTbHsAUV90SjdNH5TQlaI61XT+TCPYjX7nBDBcqPLMWWzO/SVskQfPoufphgdw7uOugZPiULtoQy6TEYGIXOjvFmBcF0HlHUbhHKuxUSSr5wLhb5kMZQaUTkWAJIfH3V/1wU/vSG5T1IU4kcw3LOlFr3uUZHzzU6w+a3mAE+P7aBBsgtB0Qrw8sB/miqArNjEAz4p52Mqly1o+PTFhPvczTNzStWNHg6oDsYlzZ+xtqD/5XAr32YUHwUgFld22b4bOsYWLPd1dvT0GxMVEFDadXVYD5Omf2Qr+6dAbFbIcVN8qe+/Wo+AsYmr49VQxifCxZ3kg6RnomPSwNsIN+xGZzr42bPA4iHSMJ19uvhX1pvrw19tTJ6zvfCKgutQYx/hse5BDOADDc0ci4Og9U/aQGX33Q76SsW61Clg0a5g9rpqxTuTgcLUSMoaPvOp0goW8CetHR0DqqwzqHXIAZJNdD9bL1q3hEbzW7VwTduD5R98ELNb/Q=";


    public void onPreLaunch() {
        IslesAddonsConfig.load();
    }

    @Override
    public void onInitializeClient() {

        System.out.println("IslesAddons - Made by Miyuki_Chan! (V1.0)");

        ClientTickEvents.END_WORLD_TICK.register(clientWorld -> runnableRunner());
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> setupIPC(client));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> closeIPC());
        UseEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> {
            if (IslesAddonsConfig.CONFIG.get("enable-fishing-notifier", Boolean.class)) {
                if (!isFishing && entity.getType() == EntityType.MAGMA_CUBE && !justStartedFishing
                        && client.player.getInventory().getEmptySlot() > -1 && ((MagmaCubeEntity) entity).getSize() > 1
                        && getFishingHoloEntity(entity) != null) {
                    isFishing = true;
                    justStartedFishing = true;
                    fishingEntity = entity;
                    fishingHoloEntity = getFishingHoloEntity(entity);
                }
            }
            return ActionResult.PASS;
        }));
    }

    public void closeIPC() {
        if (MiscUtils.onIsles()) {
            previousIP = "";
            ipcClient.close();
        }
    }

    public void setupIPC(MinecraftClient client) {
        clientTick = 1;
        if (MiscUtils.onIsles()) {
            if (previousIP.equals("")) {
                previousIP = client.getCurrentServerEntry().address;
                try {
                    ipcClient.connect();
                } catch (NoDiscordClientException e) {
                    e.printStackTrace();
                }
                DiscordUtils.lastTimestamp = OffsetDateTime.now().toEpochSecond();
            }
            DiscordUtils.updateRPC("", "");
        }
    }

    private Entity getFishingHoloEntity(Entity e) {
        // Change to store armorstands near magma cube and then check if new armorstands pop up
        List<Entity> nearbyArmorstands = client.world.getOtherEntities(client.player, e.getBoundingBox().expand(0, 1, 0), entity -> entity.getType() == EntityType.ARMOR_STAND);
        if (nearbyArmorstands.isEmpty()) return null;
        for (Entity armorStand : nearbyArmorstands) {
            if (armorStand.getDisplayName().toString().contains("Fishing")) {
                return armorStand;
            }
        }
        return null;
    }

    private boolean isFishingEntityAlive() {
        return fishingEntity != null && fishingEntity.isAlive();
    }

    private boolean isFishingArmorstandNearby() {
        return getFishingHoloEntity(fishingEntity) != null && fishingHoloEntity.getId() != getFishingHoloEntity(fishingEntity).getId();
    }

    public void runnableRunner() {
        clientTick++;
        if (IslesAddonsConfig.CONFIG.get("enable-fishing-notifier", Boolean.class)) {
            if (isFishing && (!isFishingEntityAlive() || isFishingArmorstandNearby() || client.player.isSneaking() || client.player.getInventory().getEmptySlot() == -1) && !justStartedFishing) {
                isFishing = false;
                fishingEntity = null;
                fishingHoloEntity = null;
                client.player.playSound(SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, 1F, 0.8F);
                client.player.sendMessage(new LiteralText("You have stopped fishing...").styled(style -> style.withColor(TextColor.parse("yellow"))), false);
            } else if (justStartedFishing) {
                justStartedFishing = false;
            }
        }
        if (IslesAddonsConfig.CONFIG.get("enable-glowing-parkour-skulls", Boolean.class)) {
            // get closest armorstand to particleLoc
            List<Entity> nearbyArmorStands = client.world.getOtherEntities(client.player, client.player.getVisibilityBoundingBox(), (entity -> entity.getType() == EntityType.ARMOR_STAND && !entity.isGlowing()));
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
        if (clientTick > 20) clientTick = 1;
        else if (clientTick == 20) {
            discordAppCount++;
            if (discordAppCount > 5) discordAppCount = 0;
            else if (discordAppCount == 5) {
                if (MiscUtils.onIsles()) {
                    List<String> scoreboard = MiscUtils.getScoreboard();
                    if (scoreboard.size() > 1) {
                        if (scoreboard.get(1).startsWith("Rank: ")) DiscordUtils.updateRPC(scoreboard.get(2), "In Hub");
                        else {
                            DiscordUtils.updateRPC(scoreboard.get(2), "In Game");
                            islesLocation = scoreboard.get(2);
                        }
                    }
                } else {
                    if (!MinecraftClient.getInstance().isInSingleplayer()) DiscordUtils.updateRPC("In Game Menu", "");
                }
            }
        }
    }
}
