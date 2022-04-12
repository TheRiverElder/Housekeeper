package io.github.theriverelder.housekeeper.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class HousekeeperClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(ConfigScreenHandler.CONFIG_SCREEN_HANDLER_TYPE, ConfigScreen::new);
    }
}
