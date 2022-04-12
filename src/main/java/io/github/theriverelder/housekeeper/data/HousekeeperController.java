package io.github.theriverelder.housekeeper.data;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class HousekeeperController {

    public static final HousekeeperController INSTANCE = new HousekeeperController();

    public void manage(World world, HousekeeperConfig config, BufferInventory buffer) {
        List<BlockEntity> beList = config.getEntries().stream().map(e -> world.getBlockEntity(e.pos)).toList();
        boolean canBreak = false;
        while (!canBreak) {
            canBreak = true;
            for (int i = 0; i < config.getEntries().size(); i++) {
                HousekeeperConfig.Entry entry = config.get(i);
                BlockEntity be = beList.get(i);
                if (!(be instanceof Inventory inventory)) continue;

                for (int j = 0; j < inventory.size(); j++) {
                    ItemStack stack = inventory.getStack(j);
                    if (stack.isEmpty()) {
                        for (int k = 0; k < buffer.size(); k++) {
                            ItemStack bufStack = buffer.getStack(k);
                            if (entry.rule.test(bufStack)) {
                                buffer.removeStack(k);
                                inventory.setStack(j, bufStack);
                                be.markDirty();
                                canBreak = false;
                            }
                        }
                    } else if (!entry.rule.test(stack)) {
                        int emptySlot = buffer.findEmptySlot();
                        if (emptySlot >= 0) {
                            buffer.setStack(emptySlot, stack);
                            inventory.setStack(j, ItemStack.EMPTY);
                            be.markDirty();
                            canBreak = false;
                        }
                    }
                }
            }
        }
    }


}
