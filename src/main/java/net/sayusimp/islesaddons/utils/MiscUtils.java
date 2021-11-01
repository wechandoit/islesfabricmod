package net.sayusimp.islesaddons.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;
import net.sayusimp.islesaddons.client.IslesAddonsClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MiscUtils {

    public static boolean isWordFromListInString(String message, List<String> checkList)
    {
        return getWordFromListInString(message, checkList) != null;
    }

    public static String getWordFromListInString(String message, List<String> checkList)
    {
        for (String string : checkList)
        {
            if (message.toUpperCase().contains(string.toUpperCase())) return string;
        }
        return null;
    }

    public static boolean onIsles() {
        return !IslesAddonsClient.client.isInSingleplayer() && IslesAddonsClient.client.getCurrentServerEntry().address.contains("skyblockisles.com");
    }


    public static List<String> getScoreboard() {
        Scoreboard board = MinecraftClient.getInstance().world.getScoreboard();
        ScoreboardObjective objective = board.getObjectiveForSlot(1);
        List<String> lines = new ArrayList<>();
        for(ScoreboardPlayerScore score : board.getAllPlayerScores(objective)) {
            Team team = board.getPlayerTeam(score.getPlayerName());
            String line = team.getPrefix().getString() + team.getSuffix().getString();
            if(line.trim().length() > 0) {
                String formatted = Formatting.strip(line);
                lines.add(formatted);
            }
        }

        if(objective != null) {
            lines.add(objective.getDisplayName().getString());
            Collections.reverse(lines);
        }
        return lines;
    }
}
