package io.github.theriverelder.housekeeper.client;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ConfigExtendedScreenHandlerFactory implements ExtendedScreenHandlerFactory {

    private final ItemStack stack;

    public ConfigExtendedScreenHandlerFactory(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeItemStack(stack);
    }

    @Override
    public Text getDisplayName() {
        return new LiteralText("Config");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ConfigScreenHandler(syncId, inv, stack);
    }
}
