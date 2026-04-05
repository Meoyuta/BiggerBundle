package mys.biggerbundle.registry;

import mys.biggerbundle.Biggerbundle;
import mys.biggerbundle.menu.StorageBagMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class BBMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Biggerbundle.MODID);

    private BBMenus() {
    }

    public static void register(IEventBus modBus) {
        MENUS.register(modBus);
    }    public static final Supplier<MenuType<StorageBagMenu>> STORAGE_BAG_MENU =
            MENUS.register("storage_bag",
                    () -> IMenuTypeExtension.create(StorageBagMenu::new));




}