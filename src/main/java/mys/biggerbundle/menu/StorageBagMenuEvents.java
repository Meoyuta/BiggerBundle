package mys.biggerbundle.menu;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;

public final class StorageBagMenuEvents {
    private StorageBagMenuEvents() {
    }

    public static void onContainerClosed(PlayerContainerEvent.Close event) {
        if (event.getContainer() instanceof StorageBagMenu
                && event.getEntity() instanceof ServerPlayer player) {
            player.getInventory().setChanged();
            player.inventoryMenu.broadcastFullState();
        }
    }
}
