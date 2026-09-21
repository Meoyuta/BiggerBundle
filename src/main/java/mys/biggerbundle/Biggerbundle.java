package mys.biggerbundle;

import mys.biggerbundle.menu.StorageBagMenuEvents;
import mys.biggerbundle.recipe.BBRecipeSerializers;
import mys.biggerbundle.registry.BBBlocks;
import mys.biggerbundle.registry.BBDataComponents;
import mys.biggerbundle.registry.BBItems;
import mys.biggerbundle.registry.BBMenus;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(Biggerbundle.MODID)
public class Biggerbundle {
    public static final String MODID = "biggerbundle";

    @SuppressWarnings("unused")
    public Biggerbundle(IEventBus bus, ModContainer modContainer) {
        BBDataComponents.register(bus);
        BBBlocks.register(bus);
        BBItems.register(bus);
        BBMenus.register(bus);
        BBRecipeSerializers.RECIPE_SERIALIZERS.register(bus);
        NeoForge.EVENT_BUS.addListener(StorageBagMenuEvents::onContainerClosed);
        bus.addListener(this::addCreative);
    }

    public void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) event.accept(BBItems.STORAGE_BAG.get());
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(BBItems.WOOL_BLOCK_ITEM.get());
    }
}
