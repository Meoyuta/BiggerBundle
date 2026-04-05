package mys.biggerbundle.registry;

import mys.biggerbundle.Biggerbundle;
import mys.biggerbundle.component.BagIdComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BBDataComponents {
    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Biggerbundle.MODID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BagIdComponent>> BAG_ID =
            COMPONENTS.registerComponentType(
                    "bag_id",
                    builder -> builder
                            .persistent(BagIdComponent.CODEC)
                            .networkSynchronized(BagIdComponent.STREAM_CODEC)
            );

    private BBDataComponents() {
    }

    public static void register(IEventBus modBus) {
        COMPONENTS.register(modBus);
    }
}