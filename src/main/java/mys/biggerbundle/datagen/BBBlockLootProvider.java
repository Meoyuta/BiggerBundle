package mys.biggerbundle.datagen;

import mys.biggerbundle.registry.BBBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Set;

public class BBBlockLootProvider extends BlockLootSubProvider {

    private final ArrayList<Block> blockList = new ArrayList<>();

    public BBBlockLootProvider(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        this.dropSelf(BBBlocks.WOOL_BLOCK.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        blockList.add(BBBlocks.WOOL_BLOCK.get());
        return blockList;
    }
}
