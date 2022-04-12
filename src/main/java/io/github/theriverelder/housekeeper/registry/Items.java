package io.github.theriverelder.housekeeper.registry;

import io.github.theriverelder.housekeeper.Housekeeper;
import io.github.theriverelder.housekeeper.item.ConfigWandItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class Items {

    public static final Item CONFIG_WAND = register("config_wand", new ConfigWandItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)));


    protected static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, Housekeeper.idOf(id), item);
    }

    public static void init() {}

}
