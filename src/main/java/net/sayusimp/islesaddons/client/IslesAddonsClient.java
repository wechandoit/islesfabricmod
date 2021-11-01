package net.sayusimp.islesaddons.client;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.sayusimp.islesaddons.utils.DiscordUtils;
import net.sayusimp.islesaddons.utils.MiscUtils;

import java.time.OffsetDateTime;
import java.util.List;

@Environment(EnvType.CLIENT)
public class IslesAddonsClient implements ClientModInitializer {
    public static MinecraftClient client = MinecraftClient.getInstance();
    public static IPCClient ipcClient = new IPCClient(904055870483222528L);

    private static int discordAppCount = 0;
    private static String previousIP = "";
    private static int clientTick = 1;

    @Override
    public void onInitializeClient() {

        System.out.println("IslesAddons - Made by Miyuki_Chan! (V1.0)");

        ClientTickEvents.END_WORLD_TICK.register(clientWorld -> runnableRunner());
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> setupIPC(client));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> closeIPC());
    }

    public void closeIPC()
    {
        if(MiscUtils.onIsles()) {
            previousIP = "";
            ipcClient.close();
        }
    }

    public void setupIPC(MinecraftClient client)
    {
        clientTick = 1;
        if (MiscUtils.onIsles())
        {
            if(previousIP.equals(""))
            {
                previousIP = client.getCurrentServerEntry().address;
                try {
                    ipcClient.connect();
                } catch (NoDiscordClientException e)
                {
                    e.printStackTrace();
                }
                DiscordUtils.lastTimestamp = OffsetDateTime.now().toEpochSecond();
            }
            DiscordUtils.updateRPC("", "");
        }
    }

    public void runnableRunner()
    {
        clientTick++;
        if(clientTick > 20) clientTick = 1;
        else if(clientTick==20)
        {
            discordAppCount++;
            if(discordAppCount > 5) discordAppCount = 0;
            else if(discordAppCount == 5)
            {
                if(MiscUtils.onIsles())
                {
                    List<String> scoreboard = MiscUtils.getScoreboard();
                    if(scoreboard.size() > 1)
                    {
                        if (scoreboard.get(1).startsWith("Rank: ")) DiscordUtils.updateRPC(scoreboard.get(2), "In Hub");
                        else  DiscordUtils.updateRPC(scoreboard.get(2), "In Game");
                    }
                } else DiscordUtils.updateRPC("In Game Menu", "");
            }
        }
    }
}
