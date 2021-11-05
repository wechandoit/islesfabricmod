package net.sayusimp.islesaddons.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SkullItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.sayusimp.islesaddons.IslesAddonsClient;
import net.sayusimp.islesaddons.config.IslesAddonsConfig;

import java.util.*;

public class MiscUtils {

    private static String cratesSignature = "htUMuy0MbM1YSwEL/xTBC3nSZHY9GtPwiwicjZLjV9aifGMj3t0PUwmhVqwbMXDdp0Yzi4Roepa97cK8lELyjVg/iT2zKzlvISqQYtEcNEan+XBTfFM/H1MUZTV/+N4Hcxf0FEuNT9wJm3Sa+3pZbg2VFTPAqBbQDsTvnFDHHDGz4mR4R5D7w0gbI8i28pIPDp5eGB93RDX8FYEDoEwIiBfqgmT2KASec6pq//xonIw96LTXM7Hg+EZauHhZbvXN+eessLqprYqLRlVEIdzj/tobw+VcuklcjdlQOp8OOp08QmTa2Hz7sseg9NavBAWFz0O0bRnaPZ7EAZ1yFQzXiQgXKK8/sf6IJWoH8hsBdDzg6IDFMrWhHX1qFHAySxSYXC2cFyyNLjy5KGz6DsCaoTpPmwgSOVZqzDePXBiePhQPRN4M3XD6qT8fRJoImWaIvEDgy8MiYWwhC2OidRbXwsj/L6RO6CVilN7N9TdhERqsx0QqMNP/tT52CLpS13KB81A7Zmh+YZ0fgh37CgiU3IbVLbsgICh3J8D8RDgqyvhmngt01wW+yg0W2RF7uxeXUE8LMjvWpYKi0r53uQj9s9K9MRGFv+3oEMwlU2Rl/WT/8+yVfwUihWJ8QNNki8KprXBy4eUz1oG5UM1pFA6KpBTVgds0ZyBIGUI7uvfoNG4=\",Value:\"ewogICJ0aW1lc3RhbXAiIDogMTYwMTU3NDc0ODc4MSwKICAicHJvZmlsZUlkIiA6ICI0ZTMwZjUwZTdiYWU0M2YzYWZkMmE3NDUyY2ViZTI5YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJfdG9tYXRvel8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWE1ODFmMGZhNWE3MDIxOWRhYjkwY2NhZDM3MzA4OTA5NTdhZTExODEzNjQyZjZjZDVkMjM1OWI4OWFkOGY0MyIKICAgIH0KICB9Cn0=";
    private static final int GUI_OVERLAY_WIDTH_THRESH = 16;

    public static boolean isWordFromListInString(String message, List<String> checkList) {
        return getWordFromListInString(message, checkList) != null;
    }

    public static String getWordFromListInString(String message, List<String> checkList) {
        for (String string : checkList) {
            if (message.toUpperCase().contains(string.toUpperCase())) return string;
        }
        return null;
    }

    public static Stack getStackFromItemResourceString(String message) {
        Stack stack = new Stack();
        for (String m : message.split(" ")) {
            m = m.replace(",", "");
            if (!m.contains("and")) {
                if (stack.empty())
                    stack.push(m);
                else {
                    String peek = String.valueOf(stack.peek());
                    if (peek.matches(".*\\d.*") || m.matches(".*\\d.*")) {
                        stack.push(m);
                    } else {
                        stack.push(String.valueOf(stack.pop()) + " " + m);
                    }
                }
            }
        }
        return stack;
    }

    public static List<String> getAmountListFromAmountMap(HashMap<String, Integer> amountMap) {
        List<String> amountList = new ArrayList<>();
        for (String item : amountMap.keySet()) {
            amountList.add(amountMap.get(item) + " " + item);
        }
        return amountList;
    }

    public static boolean onIsles() {
        return !IslesAddonsClient.client.isInSingleplayer() && IslesAddonsClient.client.getCurrentServerEntry().address.contains("skyblockisles.com");
    }


    public static List<String> getScoreboard() {
        Scoreboard board = MinecraftClient.getInstance().world.getScoreboard();
        ScoreboardObjective objective = board.getObjectiveForSlot(1);
        List<String> lines = new ArrayList<>();
        for (ScoreboardPlayerScore score : board.getAllPlayerScores(objective)) {
            Team team = board.getPlayerTeam(score.getPlayerName());
            if (team != null) {
                String line = team.getPrefix().getString() + team.getSuffix().getString();
                if (line.trim().length() > 0) {
                    String formatted = Formatting.strip(line);
                    lines.add(formatted);
                }
            }
        }

        if (objective != null) {
            lines.add(objective.getDisplayName().getString());
            Collections.reverse(lines);
        }
        return lines;
    }

    public static int getAmountInCrate(String string) {

        if (string == null || string.trim().equals("")) return 0;

        List<String> temp = List.of(string.replace("[", "").replace("]", "").replace("text", "").replace("}", "").replace("{", "").replace(":", "").replace("color", "").replace("italic", "").replace("false", "").replace(",", "").replace("'", "").split("\""));
        List<String> amountTemp = new ArrayList<>();
        for (String line : temp) {
            if (!line.replaceAll("\\s+", "").equals("")) {
                amountTemp.add(line.replaceAll("\\s+", ""));
            }
        }

        for (String line : temp) {
            if (line.matches(".*\\d.*") && line.substring(0, 1).matches(".*\\d.*")) {
                return Integer.parseInt(line.split(" ")[0]);
            }
        }
        return 0;
    }

    public static boolean isCrate(ItemStack stack) {
        return (stack != null && stack.getName() != null && stack.getName().getString().contains("Crated") && stack.getNbt().get(SkullItem.SKULL_OWNER_KEY) != null && stack.getNbt().get(SkullItem.SKULL_OWNER_KEY).toString().contains(cratesSignature));
    }

    public static void renderAmountText(MatrixStack matrices, ItemStack stack, int x, int y, int z, int amount) {

        Text message = Text.of(String.valueOf(amount));

        RenderSystem.enableBlend();
        float scaleRatio = 16 / 20f;
        if (amount < 100) scaleRatio = 1;
        float messageWidth = MinecraftClient.getInstance().textRenderer.getWidth(message) / scaleRatio;
        float fontHeight = MinecraftClient.getInstance().textRenderer.fontHeight / scaleRatio;
        matrices.push();
        if (messageWidth * scaleRatio > 20) scaleRatio = GUI_OVERLAY_WIDTH_THRESH / messageWidth;

        matrices.scale(scaleRatio, scaleRatio, 1f);
        matrices.translate(0, 0, z + 200);
        if (amount < 100)
            DrawableHelper.drawCenteredText(matrices, IslesAddonsClient.client.textRenderer, Text.of(String.valueOf(amount)), x + 8, y + 8, TextColor.parse("white").getRgb());
        else
            DrawableHelper.drawCenteredText(matrices, IslesAddonsClient.client.textRenderer, Text.of(String.valueOf(amount)), (int) ((x + 8) / scaleRatio), (int) ((y + GUI_OVERLAY_WIDTH_THRESH - (fontHeight / 2)) / scaleRatio), TextColor.parse("white").getRgb());

        matrices.pop();
        RenderSystem.disableBlend();

    }

    public static void renderAmountOnCrates(ItemStack stack, int x, int y, int z) {
        if (IslesAddonsConfig.CONFIG.get("enable-crate-icon-amount", Boolean.class)) {
            if (MiscUtils.isCrate(stack)) {
                NbtCompound nbt = stack.getNbt();
                NbtCompound nbtDisplay = nbt.getCompound(ItemStack.DISPLAY_KEY);
                String rawLore = nbtDisplay.get(ItemStack.LORE_KEY).toString();
                int amount = MiscUtils.getAmountInCrate(rawLore);
                renderAmountText(new MatrixStack(), stack, x, y, z, amount);
            }
        }
    }
}
