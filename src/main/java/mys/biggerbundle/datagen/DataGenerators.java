package mys.biggerbundle.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // 注册配方
        generator.addProvider(
                event.includeServer(),
                new BBRecipeProvider(
                        packOutput,
                        lookupProvider
                )
        );

        // 注册方块掉落物
        generator.addProvider(
                event.includeServer(),
                new BBLootTableProvider(
                        packOutput,
                        Set.of(),
                        List.of(
                                new LootTableProvider.SubProviderEntry(
                                        BBBlockLootProvider::new,
                                        LootContextParamSets.BLOCK
                                )
                        ),
                        lookupProvider
                )
        );
    }
}