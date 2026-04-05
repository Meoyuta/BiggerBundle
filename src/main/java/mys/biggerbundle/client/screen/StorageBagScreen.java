package mys.biggerbundle.client.screen;

import mys.biggerbundle.item.StorageBagItem;
import mys.biggerbundle.menu.StorageBagMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public final class StorageBagScreen extends AbstractContainerScreen<StorageBagMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/container/generic_54.png");

    public StorageBagScreen(StorageBagMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 114 + StorageBagItem.ROWS * 18; // 168
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        int topHeight = 17 + StorageBagItem.ROWS * 18;

        // 上半部分（容器）
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, topHeight, 256, 256);

        // 下半部分（物品栏）
        guiGraphics.blit(TEXTURE, x, y + topHeight, 0, 127, this.imageWidth, 96, 256, 256);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}