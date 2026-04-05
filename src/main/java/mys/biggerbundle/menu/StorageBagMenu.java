package mys.biggerbundle.menu;

import mys.biggerbundle.item.StorageBagItem;
import mys.biggerbundle.menu.slot.LockedSlot;
import mys.biggerbundle.registry.BBMenus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class StorageBagMenu extends AbstractContainerMenu {
    public static final int BAG_SLOT_COUNT = StorageBagItem.SLOT_COUNT;
    private final Container bagContainer;

    public StorageBagMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInventory, new SimpleContainer(BAG_SLOT_COUNT), buf.readVarInt());
    }

    public StorageBagMenu(int containerId, Inventory playerInventory, Container bagContainer, int bagSlotInPlayerInventory) {
        super(BBMenus.STORAGE_BAG_MENU.get(), containerId);
        checkContainerSize(bagContainer, BAG_SLOT_COUNT);

        this.bagContainer = bagContainer;
        this.bagContainer.startOpen(playerInventory.player);

        // 袋子内部：6x9
        int bagStartX = 8;
        int bagStartY = 18;

        int slot = 0;
        for (int row = 0; row < StorageBagItem.ROWS; row++) {
            for (int col = 0; col < StorageBagItem.COLUMNS; col++) {
                this.addSlot(new Slot(bagContainer, slot++, bagStartX + col * 18, bagStartY + row * 18));
            }
        }

        // 玩家背包 3x9
        int invStartX = 8;
        int invStartY = 84;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int playerSlot = col + row * 9 + 9;
                int x = invStartX + col * 18;
                int y = invStartY + row * 18;
                if (playerSlot == bagSlotInPlayerInventory) {
                    this.addSlot(new LockedSlot(playerInventory, playerSlot, x, y));
                } else {
                    this.addSlot(new Slot(playerInventory, playerSlot, x, y));
                }
            }
        }

        // 快捷栏
        int hotbarY = invStartY + 58;
        for (int col = 0; col < 9; col++) {
            int x = invStartX + col * 18;
            if (col == bagSlotInPlayerInventory) {
                this.addSlot(new LockedSlot(playerInventory, col, x, hotbarY));
            } else {
                this.addSlot(new Slot(playerInventory, col, x, hotbarY));
            }
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.bagContainer.stillValid(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack quickMoved;
        Slot source = this.slots.get(index);

        if (!source.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack raw = source.getItem();
        if (!raw.isStackable()) return ItemStack.EMPTY;// 不装不可堆叠的
        quickMoved = raw.copy();

        if (index < BAG_SLOT_COUNT) {
            if (!this.moveItemStackTo(raw, BAG_SLOT_COUNT, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(raw, 0, BAG_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (raw.isEmpty()) {
            source.set(ItemStack.EMPTY);
        } else {
            source.setChanged();
        }

        source.onTake(player, raw);
        return quickMoved;
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.bagContainer.stopOpen(player);
    }
}