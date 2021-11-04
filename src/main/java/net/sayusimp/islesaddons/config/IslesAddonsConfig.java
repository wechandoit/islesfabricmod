package net.sayusimp.islesaddons.config;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Language;
import net.sayusimp.islesaddons.utils.Config;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class IslesAddonsConfig {

    public static final Config CONFIG;
    public static final String FILE_NAME = "islesaddons.json";

    static {
        ModContainer mod = FabricLoader.getInstance().getModContainer("islesaddons").orElseThrow(java.util.NoSuchElementException::new);
        InputStream def = IslesAddonsConfig.class.getResourceAsStream(mod.getPath("islesaddonsconfig.json").toString());
        CONFIG = new Config(def);
    }

    static Option[] getOptions() {
        return CONFIG.entrySet().stream()
                .map(IslesAddonsConfig::toOption)
                .filter(Objects::nonNull)
                .toArray(x$0 -> new Option[x$0]);
    }

    public static boolean doesNotExist() {
        return Files.notExists(getConfigDir().resolve(FILE_NAME), new java.nio.file.LinkOption[0]);
    }

    public static void load() {
        Path path = getConfigDir().resolve(FILE_NAME);
        if (Files.exists(path, new java.nio.file.LinkOption[0])) {
            CONFIG.load(path);
        } else {
            save();
        }
    }

    public static void save() {
        CONFIG.save(getConfigDir().resolve(FILE_NAME));
    }

    private static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    private static Option toOption(Map.Entry<String, Object> entry) {
        CyclingOption cyclingOption = null;
        String key = entry.getKey();
        if (key.endsWith(".min") || key.endsWith(".max") || key.endsWith(".step"))
            return null;
        String tooltipKey = "option.isles-addons." + key + ".tooltip";
        CyclingButtonWidget.TooltipFactory tooltip = Language.getInstance().hasTranslation(tooltipKey) ? (client -> MinecraftClient.getInstance().textRenderer.wrapLines(new TranslatableText(tooltipKey), 200)) : (__ -> List.of());
        Object value = entry.getValue();
        String translationKey = "option.isles-addons." + key;
        if (value instanceof Number) {
            Number min = CONFIG.get(key + ".min", Number.class);
            Number max = CONFIG.get(key + ".max", Number.class);
            Number step = CONFIG.get(key + ".step", Number.class);
            DoubleOption doubleOption = new DoubleOption(translationKey, min.doubleValue(), max.doubleValue(), step.floatValue(), __ -> CONFIG.get(key, Number.class).doubleValue(), (__, v) -> CONFIG.put(key, v), (__, ___) -> new TranslatableText(translationKey), tooltip);
        } else if (value instanceof Boolean) {
            cyclingOption = CyclingOption.create(translationKey, __ -> CONFIG.get(key, Boolean.class), (__, ___, v) -> CONFIG.put(key, v)).tooltip(client -> tooltip);
        } else {
            throw new IllegalStateException();
        }
        return cyclingOption;
    }
}
