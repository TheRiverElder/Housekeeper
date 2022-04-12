package io.github.theriverelder.housekeeper.client;

import io.github.theriverelder.housekeeper.Housekeeper;
import io.github.theriverelder.housekeeper.data.HousekeeperConfig;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class ConfigScreenHandler extends ScreenHandler {

    public static final ScreenHandlerType<ConfigScreenHandler> CONFIG_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(Housekeeper.idOf("config"), ConfigScreenHandler::new);


    private PacketByteBuf buf;
    public ItemStack stack;
    public HousekeeperConfig config;

    public ConfigScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, buf.readItemStack());
        this.buf = buf;
    }

    public ConfigScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack stack) {
        super(CONFIG_SCREEN_HANDLER_TYPE, syncId);
        this.stack = stack;
        this.config = HousekeeperConfig.fromStack(stack);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
