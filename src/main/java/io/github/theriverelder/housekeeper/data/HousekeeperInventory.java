package io.github.theriverelder.housekeeper.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class HousekeeperInventory implements BufferInventory {

    private final DefaultedList<ItemStack> data = DefaultedList.ofSize(27, ItemStack.EMPTY);


    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty() || data.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return data.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (amount <= 0 || slot < 0 || slot >= data.size()) return ItemStack.EMPTY;
        ItemStack stack = data.get(slot);
        return stack.split(amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        if (slot < 0 || slot >= data.size()) return ItemStack.EMPTY;
        ItemStack stack = data.get(slot);
        data.set(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        data.set(slot, stack.isEmpty() ? ItemStack.EMPTY : stack);
    }

    @Override
    public void markDirty() {
        // TODO
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        data.clear();
    }
}
