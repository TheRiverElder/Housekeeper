package io.github.theriverelder.housekeeper.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeeperConfig {

    static {
        HousekeeperRule.FACTORIES.put("match_name", MatchNameRule::new);
        HousekeeperRule.FACTORIES.put("match_id", MatchIdRule::new);
        HousekeeperRule.FACTORIES.put("match_all", MatchIdRule::matchAllId);
    }

    public static final String KEY_CONFIG = "config";
    public static final String KEY_CURRENT_INDEX = "current_index";
    public static final String KEY_ENTRIES = "entries";
    public static final String KEY_NAME = "name";
    public static final String KEY_POS = "pos";
    public static final String KEY_TYPE = "type";
    public static final String KEY_RULE = "rule";

    public static class Entry {
        public String name = "Entry";
        public BlockPos pos = BlockPos.ORIGIN;
        public Type type = Type.STORAGE;
        public HousekeeperRule rule = MatchIdRule.matchAllId();
    }

    static final Map<String, Type> TYPE_REMAP = new HashMap<>();

    public enum Type {

        STORAGE("storage"),
        INPUT("input"),
        OUTPUT("output"),
        ;

        final String key;

        Type(String key) {
            this.key = key;
            TYPE_REMAP.put(key, this);
        }
    }

    public static HousekeeperConfig fromStack(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateSubNbt(KEY_CONFIG);

        HousekeeperConfig config = new HousekeeperConfig();

        NbtElement entriesNbtElem = nbt.get(KEY_ENTRIES);
        if (entriesNbtElem instanceof NbtList entriesNbt) {
            for (int i = 0; i < entriesNbt.size(); i++) {
                NbtCompound entryNbt = entriesNbt.getCompound(i);
                Entry entry = new Entry();
                entry.name = entryNbt.getString(KEY_NAME);
                entry.pos = NbtHelper.toBlockPos(entryNbt.getCompound(KEY_POS));
                entry.type = TYPE_REMAP.getOrDefault(entryNbt.getString(KEY_TYPE), Type.STORAGE);
                entry.rule = HousekeeperRule.fromNbt(entryNbt.getCompound(KEY_RULE));
                config.entries.add(entry);
            }
        }

        config.setCurrentIndex(nbt.getInt(KEY_CURRENT_INDEX));

        return config;
    }

    public void applyOnStack(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateSubNbt(KEY_CONFIG);

        NbtList entriesNbt = new NbtList();
        nbt.put(KEY_ENTRIES, entriesNbt);
        for (Entry entry : entries) {
            NbtCompound entryNbt = new NbtCompound();
            entryNbt.putString(KEY_NAME, entry.name);
            entryNbt.put(KEY_POS, NbtHelper.fromBlockPos(entry.pos));
            entryNbt.putString(KEY_TYPE, entry.type.key);
            NbtCompound ruleNbt = new NbtCompound();
            entry.rule.writeNbt(ruleNbt);
            entryNbt.put(KEY_RULE, ruleNbt);
            entriesNbt.add(entryNbt);
        }

        nbt.putInt(KEY_CURRENT_INDEX, getCurrentIndex());
    }

    private int currentIndex = -1;



    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        if (entries.size() <= 0) {
            this.currentIndex = -1;
        } else {
            this.currentIndex = Math.max(0, Math.min(currentIndex, entries.size() - 1));
        }
    }

    private final List<Entry> entries = new ArrayList<>();

    public int entryCount() {
        return entries.size();
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void add(Entry entry) {
        entries.add(entry);
    }

    public Entry remove(int index) {
        Entry entry = entries.remove(index);
        setCurrentIndex(-1);
        return entry;
    }

    public Entry get(int index) {
        return entries.get(index);
    }

    public void move(int oldIndex, int deltaIndex) {
        if (deltaIndex == 0 || oldIndex < 0 || oldIndex >= entries.size()) return;

        deltaIndex = Math.max(-oldIndex, Math.min(deltaIndex, entries.size() - oldIndex - 1));

        int newIndex = oldIndex + deltaIndex;
        if (newIndex < 0 || newIndex >= entries.size()) return;

        Entry entry = entries.get(oldIndex);

        int step = deltaIndex / Math.abs(deltaIndex);
        for (int i = oldIndex; i != newIndex; i += step) {
            entries.set(i, entries.get(i + step));
        }
        entries.set(newIndex, entry);
    }
}
