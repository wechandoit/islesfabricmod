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
import net.minecraft.text.LiteralText;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.sayusimp.islesaddons.config.IslesAddonsConfig;
import net.sayusimp.islesaddons.util.DiscordUtils;
import net.sayusimp.islesaddons.util.MiscUtils;

import java.time.OffsetDateTime;
import java.util.List;

@Environment(EnvType.CLIENT)
public class IslesAddonsClient implements PreLaunchEntrypoint, ClientModInitializer {
    public static MinecraftClient client = MinecraftClient.getInstance();
    public static IPCClient ipcClient = new IPCClient(904055870483222528L);
    public static boolean isFishing = false;
    public static Entity fishingEntity = null;
    public static String islesLocation = "";

    private static int discordAppCount = 0;
    private static String previousIP = "";
    private static int clientTick = 1;
    private static boolean justStartedFishing = false;

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
            if (entity.getType() == EntityType.MAGMA_CUBE && !justStartedFishing && client.player.getInventory().getEmptySlot() > -1) {
                isFishing = true;
                justStartedFishing = true;
                fishingEntity = entity;
                client.player.sendMessage(new LiteralText("You have started fishing...").styled(style -> style.withColor(TextColor.parse("yellow"))), false);
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

    private boolean isFishingArmorstandNearby() {
        List<Entity> nearbyArmorstands = client.world.getOtherEntities(client.player, fishingEntity.getBoundingBox(), entity -> entity.getType() == EntityType.ARMOR_STAND);
        if (nearbyArmorstands.isEmpty()) return false;
        for (Entity armorStand : nearbyArmorstands) {
            if (armorStand.getDisplayName().toString().contains("Fishing")) {
                return true;
            }
        }
        return false;
    }

    private boolean isFishingEntityAlive() {
        return fishingEntity != null && fishingEntity.isAlive();
    }

    public void runnableRunner() {
        clientTick++;
        if (isFishing && (!fishingEntity.isAlive() || isFishingArmorstandNearby() || client.player.isSneaking()) && !justStartedFishing) {
            isFishing = false;
            System.out.println("1: " + !isFishingEntityAlive());
            System.out.println("2: " + isFishingArmorstandNearby());
            System.out.println("3: " + !justStartedFishing);
            client.player.sendMessage(new LiteralText("You have stopped fishing...").styled(style -> style.withColor(TextColor.parse("yellow"))), false);
        } else if (justStartedFishing)
        {
            justStartedFishing = false;
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
