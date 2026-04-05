package mys.biggerbundle.client;

import mys.biggerbundle.Biggerbundle;
import mys.biggerbundle.client.screen.StorageBagScreen;
import mys.biggerbundle.menu.StorageBagMenu;
import mys.biggerbundle.registry.BBMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Biggerbundle.MODID, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(BBMenus.STORAGE_BAG_MENU.get(), new MenuScreens.ScreenConstructor<StorageBagMenu, StorageBagScreen>() {
            @Override
            public @NotNull StorageBagScreen create(@NotNull StorageBagMenu menu, @NotNull Inventory inventory, @NotNull Component title) {
                return new StorageBagScreen(menu, inventory, title);
            }
        });
    }
}