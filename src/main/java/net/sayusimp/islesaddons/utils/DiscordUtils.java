package net.sayusimp.islesaddons.utils;

import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.RichPresenceButton;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import net.sayusimp.islesaddons.client.IslesAddonsClient;

import java.io.IOException;

public class DiscordUtils {

    public static long lastTimestamp = 0L;

    public static boolean sendMessageWebhook(String message)
    {
        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/899626375592435712/GuVsIkm5DWp3ltsytiQ1I570K8i8WIEcuLq6PVZ0sVDgAUapRwRqGYm8shpwT44AegRL");
        webhook.setContent(message);

        try {
            webhook.execute();
            return true;
        } catch (IOException exception)
        {
            return false;
        }
    }

    public static void updateRPC(String firstline, String secondline)
    {
        RichPresence.Builder builder = new RichPresence.Builder();
        RichPresenceButton[] button = new RichPresenceButton[0];
        builder.setDetails(firstline)
                .setState(secondline)
                .setButtons(button)
                .setLargeImage("logo", "Skyblock Isles - play.skyblockisles.com - Mod made by miyuki_chan");
        builder.setStartTimestamp(lastTimestamp);
        try
        {
            IslesAddonsClient.ipcClient.sendRichPresence(builder.build());
        } catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
    }
}
