package io.github.theriverelder.housekeeper.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class MatchAllRule implements HousekeeperRule {

    public static final MatchAllRule INSTANCE = new MatchAllRule();

    public static MatchAllRule getInstance() {
        return INSTANCE;
    }

    @Override
    public String getId() {
        return "match_all";
    }

    @Override
    public void readNbt(NbtCompound nbt) {
    }

    @Override
    public boolean test(ItemStack stack) {
        return true;
    }

    @Override
    public String toString() {
        return "*";
    }
}

