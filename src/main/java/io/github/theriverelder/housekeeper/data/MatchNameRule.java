package io.github.theriverelder.housekeeper.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class MatchNameRule implements HousekeeperRule {

    private String pattern = "";

    public String getPattern() {
        return pattern;
    }

    @Override
    public String getId() {
        return "match_name";
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        pattern = nbt.getString("pattern");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        HousekeeperRule.super.writeNbt(nbt);
        nbt.putString("pattern", pattern);
    }

    @Override
    public boolean test(ItemStack stack) {
        return stack.getName().asString().matches(pattern);
    }

    @Override
    public String toString() {
        return pattern;
    }
}
