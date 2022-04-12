package io.github.theriverelder.housekeeper.server;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import static io.github.theriverelder.housekeeper.networking.Networking.MAIN_HAND_STACK_UPDATE;

//@Environment(EnvType.SERVER)
public class HousekeeperServer implements DedicatedServerModInitializer {

    public static void registerNetworking() {
        ServerPlayNetworking.registerGlobalReceiver(MAIN_HAND_STACK_UPDATE, new MainHandStackUpdatePackHandler());
    }

    @Override
    public void onInitializeServer() {
        registerNetworking();
        System.out.println("THIS_IS_SERVER!");
    }
}
