package mys.biggerbundle.registry;

import mys.biggerbundle.Biggerbundle;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BBBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Biggerbundle.MODID);
    public static final DeferredBlock<Block> WOOL_BLOCK =
            BLOCKS.registerBlock(
                    "wool_block",
                    properties -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL))
            );

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
