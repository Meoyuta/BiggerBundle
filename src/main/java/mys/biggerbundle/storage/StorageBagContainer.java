package mys.biggerbundle.storage;

import mys.biggerbundle.data.StorageBagSavedData;
import mys.biggerbundle.item.StorageBagItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.UUID;

public final class StorageBagContainer implements Container {
    private final StorageBagSavedData data;
    private final UUID bagId;
    private final Inventory ownerInventory;
    private final int bagSlotInPlayerInventory;

    public StorageBagContainer(StorageBagSavedData data, UUID bagId, Inventory ownerInventory, int bagSlotInPlayerInventory) {
        this.data = data;
        this.bagId = bagId;
        this.ownerInventory = ownerInventory;
        this.bagSlotInPlayerInventory = bagSlotInPlayerInventory;
    }

    private NonNullList<ItemStack> items() {
        return this.data.getOrCreate(this.bagId);
    }

    @Override
    public int getContainerSize() {
        return StorageBagItem.SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        if (slot < 0 || slot >= StorageBagItem.SLOT_COUNT) {
            return ItemStack.EMPTY;
        }
        return items().get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        if (slot < 0 || slot >= StorageBagItem.SLOT_COUNT) {
            return ItemStack.EMPTY;
        }

        ItemStack removed = ContainerHelper.removeItem(items(), slot, amount);
        if (!removed.isEmpty()) {
            setChanged();
        }
        return removed;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        if (slot < 0 || slot >= StorageBagItem.SLOT_COUNT) {
            return ItemStack.EMPTY;
        }

        ItemStack removed = ContainerHelper.takeItem(items(), slot);
        if (!removed.isEmpty()) {
            setChanged();
        }
        return removed;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        if (slot < 0 || slot >= StorageBagItem.SLOT_COUNT) {
            return;
        }
        if (!stack.isEmpty() && !canPlaceItem(slot, stack)) {
            return;
        }

        ItemStack toStore = stack.copy();
        if (!toStore.isEmpty() && toStore.getCount() > toStore.getMaxStackSize()) {
            toStore.setCount(toStore.getMaxStackSize());
        }

        items().set(slot, toStore);
        setChanged();
    }

    @Override
    public void setChanged() {
        this.data.setDirty();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (this.bagSlotInPlayerInventory < 0 || this.bagSlotInPlayerInventory >= this.ownerInventory.getContainerSize()) {
            return false;
        }

        ItemStack current = this.ownerInventory.getItem(this.bagSlotInPlayerInventory);
        return StorageBagItem.hasBagId(current, this.bagId);
    }

    @Override
    public boolean canPlaceItem(int slot, @NotNull ItemStack stack) {
        if (slot < 0 || slot >= StorageBagItem.SLOT_COUNT) {
            return false;
        }
        if (stack.isEmpty()) {
            return true;
        }
        // 只允许可堆叠物品
        if (stack.getMaxStackSize() <= 1) {
            return false;
        }
        // 禁止袋中袋
        return !(stack.getItem() instanceof StorageBagItem);
    }

    @Override
    public void clearContent() {
        NonNullList<ItemStack> items = items();
        Collections.fill(items, ItemStack.EMPTY);
        setChanged();
    }

    @Override
    public void startOpen(@NotNull Player player) {
        // lock 已经在 openBag 时做过了
    }

    @Override
    public void stopOpen(Player player) {
        this.data.unlock(this.bagId, player.getUUID());
    }
}