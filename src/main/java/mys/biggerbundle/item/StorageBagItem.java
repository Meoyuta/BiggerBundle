package mys.biggerbundle.item;

import mys.biggerbundle.component.BagIdComponent;
import mys.biggerbundle.data.StorageBagSavedData;
import mys.biggerbundle.menu.StorageBagMenu;
import mys.biggerbundle.registry.BBDataComponents;
import mys.biggerbundle.storage.StorageBagContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class StorageBagItem extends Item {
    public static final int ROWS = 3;
    public static final int COLUMNS = 9;
    public static final int SLOT_COUNT = ROWS * COLUMNS;

    public StorageBagItem(Properties properties) {
        super(properties);
    }

    private static void openBag(ServerPlayer player, ItemStack stack, int bagSlot) {
        UUID bagId = getOrCreateBagId(stack, player.getServer());
        StorageBagSavedData data = StorageBagSavedData.get(Objects.requireNonNull(player.getServer()));

        if (!data.tryLock(bagId, player.getUUID())) {
            player.displayClientMessage(Component.translatable("tooltip.biggerbundle.storage_bag.already_open"), true);
            return;
        }

        Container container = new StorageBagContainer(
                data,
                bagId,
                player.getInventory(),
                bagSlot
        );

        player.openMenu(
                new SimpleMenuProvider(
                        (containerId, playerInventory, ignoredPlayer) ->
                                new StorageBagMenu(containerId, playerInventory, container, bagSlot),
                        stack.getHoverName()
                ),
                buf -> buf.writeVarInt(bagSlot)
        );
    }

    public static UUID getOrCreateBagId(ItemStack stack, MinecraftServer server) {
        BagIdComponent component = stack.get(BBDataComponents.BAG_ID.get());
        if (component == null) {
            UUID uuid;
            do {
                uuid = UUID.randomUUID();
            } while (StorageBagSavedData.get(server).getUUIDSet().contains(uuid));
            component = new BagIdComponent(uuid);
            stack.set(BBDataComponents.BAG_ID.get(), component);
        }
        return component.id();
    }

    @Nullable
    public static UUID getBagId(ItemStack stack) {
        BagIdComponent component = stack.get(BBDataComponents.BAG_ID.get());
        return component == null ? null : component.id();
    }

    public static boolean hasBagId(ItemStack stack, UUID expected) {
        UUID actual = getBagId(stack);
        return expected.equals(actual);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            int slot = hand == InteractionHand.MAIN_HAND ? serverPlayer.getInventory().selected : 40;
            openBag(serverPlayer, stack, slot);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public boolean overrideOtherStackedOnMe(@NotNull ItemStack stack, @NotNull ItemStack other, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player, @NotNull SlotAccess access) {
        if (action == ClickAction.SECONDARY
                && other.isEmpty()
                && player instanceof ServerPlayer serverPlayer
                && slot.container == player.getInventory()) {
            openBag(serverPlayer, stack, slot.getContainerSlot());
            return true;
        }
        return false;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.biggerbundle.storage_bag.open").withStyle(ChatFormatting.GRAY));
    }
}