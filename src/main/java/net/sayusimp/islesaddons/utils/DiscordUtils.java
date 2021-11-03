package net.sayusimp.islesaddons.utils;

import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.RichPresenceButton;
import net.sayusimp.islesaddons.client.IslesAddonsClient;

public class DiscordUtils {

    public static long lastTimestamp = 0L;

    public static void updateRPC(String firstline, String secondline)
    {
        RichPresence.Builder builder = new RichPresence.Builder();
        RichPresenceButton[] button = new RichPresenceButton[0];
        builder.setDetails(firstline)
                .setState(secondline)
                .setButtons(button)
                .setLargeImage("logo", "play.skyblockisles.com - Mod made by miyuki_chan");
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
