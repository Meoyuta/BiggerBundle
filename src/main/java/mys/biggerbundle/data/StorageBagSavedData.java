package mys.biggerbundle.data;

import mys.biggerbundle.Biggerbundle;
import mys.biggerbundle.item.StorageBagItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class StorageBagSavedData extends SavedData {
    private static final String FILE_ID = Biggerbundle.MODID + "_storage_bags";
    private static final String TAG_BAGS = "bags";
    private static final String TAG_ID = "id";
    private static final String TAG_ITEMS = "items";
    private static final String TAG_SLOT = "slot";
    private static final String TAG_ITEM = "item";

    private final Map<UUID, NonNullList<ItemStack>> bags = new HashMap<>();
    private final Map<UUID, UUID> openers = new HashMap<>();
    private final Object lock = new Object(); // 添加同步锁对象

    public StorageBagSavedData() {
    }

    public static StorageBagSavedData create() {
        return new StorageBagSavedData();
    }

    public static StorageBagSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        StorageBagSavedData data = create();

        ListTag bagList = tag.getList(TAG_BAGS, Tag.TAG_COMPOUND);
        for (int i = 0; i < bagList.size(); i++) {
            CompoundTag bagTag = bagList.getCompound(i);
            UUID bagId = bagTag.getUUID(TAG_ID);

            NonNullList<ItemStack> inv = NonNullList.withSize(StorageBagItem.SLOT_COUNT, ItemStack.EMPTY);
            ListTag items = bagTag.getList(TAG_ITEMS, Tag.TAG_COMPOUND);

            for (int j = 0; j < items.size(); j++) {
                CompoundTag stackTag = items.getCompound(j);
                int slot = stackTag.getInt(TAG_SLOT);
                CompoundTag item = (CompoundTag) stackTag.get(TAG_ITEM);
                if (slot >= 0 && slot < StorageBagItem.SLOT_COUNT && item != null) {
                    inv.set(slot, ItemStack.parseOptional(registries, item));
                }
            }

            data.bags.put(bagId, inv);
        }

        return data;
    }

    public static StorageBagSavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(StorageBagSavedData::create, StorageBagSavedData::load),
                FILE_ID
        );
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        ListTag bagList = new ListTag();

        synchronized (lock) {
            for (Map.Entry<UUID, NonNullList<ItemStack>> entry : this.bags.entrySet()) {
                CompoundTag bagTag = new CompoundTag();
                bagTag.putUUID(TAG_ID, entry.getKey());

                ListTag items = new ListTag();
                NonNullList<ItemStack> inv = entry.getValue();

                for (int slot = 0; slot < inv.size(); slot++) {
                    ItemStack stack = inv.get(slot);
                    if (!stack.isEmpty()) {
                        CompoundTag stackTag = new CompoundTag();
                        stackTag.putInt(TAG_SLOT, slot);
                        stackTag.put(TAG_ITEM, stack.save(registries, stackTag));
                        items.add(stackTag);
                    }
                }

                bagTag.put(TAG_ITEMS, items);
                bagList.add(bagTag);
            }
        }

        tag.put(TAG_BAGS, bagList);
        return tag;
    }

    public NonNullList<ItemStack> getOrCreate(UUID bagId) {
        synchronized (lock) {
            return this.bags.computeIfAbsent(
                    bagId,
                    id -> NonNullList.withSize(StorageBagItem.SLOT_COUNT, ItemStack.EMPTY)
            );
        }
    }

    public boolean tryLock(UUID bagId, UUID playerId) {
        synchronized (lock) {
            UUID current = this.openers.get(bagId);
            if (current != null && !current.equals(playerId)) {
                return false;
            }
            this.openers.put(bagId, playerId);
            return true;
        }
    }

    public void unlock(UUID bagId, UUID playerId) {
        synchronized (lock) {
            UUID current = this.openers.get(bagId);
            if (playerId.equals(current)) {
                this.openers.remove(bagId);
            }
        }
    }
}