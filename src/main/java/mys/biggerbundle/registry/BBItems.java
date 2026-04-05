package mys.biggerbundle.registry;

import mys.biggerbundle.Biggerbundle;
import mys.biggerbundle.item.StorageBagItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class BBItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Biggerbundle.MODID);
    public static final Supplier<Item> STORAGE_BAG =
            ITEMS.register(
                    "storage_bag",
                    () -> new StorageBagItem(new Item.Properties().stacksTo(1))
            );
    public static final Supplier<Item> WOOL_BLOCK_ITEM =
            ITEMS.register(
                    "wool_block",
                    () -> new BlockItem(BBBlocks.WOOL_BLOCK.get(), new Item.Properties())
            );

    private BBItems() {
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}