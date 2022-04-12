package io.github.theriverelder.housekeeper;

import io.github.theriverelder.housekeeper.registry.Items;
import io.github.theriverelder.housekeeper.server.HousekeeperServer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Housekeeper implements ModInitializer {

    public static final String ID = "housekeeper";

    public static Identifier idOf(String path) {
        return new Identifier(ID, path);
    }

    @Override
    public void onInitialize() {
        Items.init();
        HousekeeperServer.registerNetworking();
    }
}
