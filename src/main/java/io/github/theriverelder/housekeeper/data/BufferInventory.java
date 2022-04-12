package io.github.theriverelder.housekeeper.data;

import net.minecraft.inventory.Inventory;

public interface BufferInventory extends Inventory {
    default int findEmptySlot() {
        for (int i = 0; i < this.size(); i++) {
            if (this.getStack(i).isEmpty()) return i;
        }
        return -1;
    }
}
