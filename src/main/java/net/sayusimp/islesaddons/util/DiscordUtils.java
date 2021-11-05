package net.sayusimp.islesaddons.util;

import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.RichPresenceButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.resource.ResourceReloader;
import net.sayusimp.islesaddons.IslesAddonsClient;

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
