package io.github.theriverelder.housekeeper.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;

public class MatchIdRule implements HousekeeperRule {

    public static MatchIdRule matchAllId() {
        return new MatchIdRule(".*");
    }

    private String pattern;

    public MatchIdRule() {
        this(".*");
    }

    public MatchIdRule(String pattern) {
        this.pattern = pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public String getId() {
        return "match_id";
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
        return Registry.ITEM.getId(stack.getItem()).toString().matches(pattern);
    }

    @Override
    public String toString() {
        return pattern;
    }
}
