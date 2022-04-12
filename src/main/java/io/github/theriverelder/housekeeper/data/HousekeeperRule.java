package io.github.theriverelder.housekeeper.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface HousekeeperRule extends Predicate<ItemStack> {

    public static final Map<String, Supplier<HousekeeperRule>> FACTORIES = new HashMap<>();

    public static HousekeeperRule fromNbt(NbtCompound nbt) {
        String id = nbt.getString("id");
        Supplier<HousekeeperRule> factory = FACTORIES.getOrDefault(id, MatchAllRule::getInstance);
        if (factory == null) return null;
        HousekeeperRule rule = factory.get();
        rule.readNbt(nbt);
        return rule;
    }

    String getId();

    default void writeNbt(NbtCompound nbt) {
        nbt.putString("id", getId());
    }

    void readNbt(NbtCompound nbt);

}
