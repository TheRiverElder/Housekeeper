package io.github.theriverelder.housekeeper.item;

import io.github.theriverelder.housekeeper.client.ConfigExtendedScreenHandlerFactory;
import io.github.theriverelder.housekeeper.data.HousekeeperConfig;
import io.github.theriverelder.housekeeper.data.HousekeeperController;
import io.github.theriverelder.housekeeper.data.HousekeeperInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigWandItem extends Item {

    public ConfigWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();

        if (player == null || world == null) return super.useOnBlock(context);

        BlockState state = world.getBlockState(pos);
        BlockEntity blockEntity = world.getBlockEntity(pos);

        // add to config
        if (blockEntity instanceof Inventory inventory) {
            HousekeeperConfig config = HousekeeperConfig.fromStack(stack);
            int index = config.getCurrentIndex();
            if (index >= 0 && index < config.entryCount()) {
                HousekeeperConfig.Entry entry = config.get(index);
                entry.pos = pos;
                config.applyOnStack(stack);
                player.sendMessage(new TranslatableText("text.housekeeper.bind_inventory", entry.name, pos), true);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (user.isSneaking()) {
            user.openHandledScreen(new ConfigExtendedScreenHandlerFactory(stack));
        } else {
            HousekeeperController.INSTANCE.manage(world, HousekeeperConfig.fromStack(stack), new HousekeeperInventory());
        }
        return TypedActionResult.success(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        HousekeeperConfig config = HousekeeperConfig.fromStack(stack);
//        System.out.println(stack.getNbt());

        tooltip.add(new LiteralText(config.getEntries().size() + " group(s)"));
        int index = config.getCurrentIndex();
        if (index >= 0 && index < config.getEntries().size()) {
            tooltip.add(new LiteralText("Current group: " + config.getEntries().get(index).name));
        }
    }
}
