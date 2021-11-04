package net.sayusimp.islesaddons.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class IslesAddonsModMenu implements ModMenuApi {

    public ConfigScreenFactory getModConfigScreenFactory() {
        return IslesAddonsOptionsScreen::new;
    }
}
