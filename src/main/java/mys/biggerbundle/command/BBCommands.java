package mys.biggerbundle.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import mys.biggerbundle.Biggerbundle;
import mys.biggerbundle.component.BagIdComponent;
import mys.biggerbundle.registry.BBDataComponents;
import mys.biggerbundle.registry.BBItems;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.UUID;

@EventBusSubscriber(modid = Biggerbundle.MODID)
public class BBCommands {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(
                Commands.literal("biggerbundle")
                        .then(
                                Commands.literal("give")
                                        .then(
                                                Commands.argument("uuid", StringArgumentType.word())
                                                        .suggests(UUIDSuggestions.INSTANCE)
                                                        .executes(context -> {
                                                            String uuid = StringArgumentType.getString(context, "uuid");
                                                            if (context.getSource().getEntity() instanceof ServerPlayer player) {
                                                                ItemStack item = new ItemStack(BBItems.STORAGE_BAG.get());
                                                                item.set(BBDataComponents.BAG_ID.get(), new BagIdComponent(UUID.fromString(uuid)));
                                                                boolean isGiven;
                                                                if (!player.addItem(item)) {
                                                                    isGiven = false;
                                                                    ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), item);
                                                                    itemEntity.setNoPickUpDelay();
                                                                    itemEntity.setInvulnerable(true);
                                                                } else {
                                                                    isGiven = true;
                                                                }
                                                                context.getSource().sendSuccess(() -> Component.literal("success " + (isGiven ? "gave " : "summoned ") + "new " + "Storage Bag"), true);
                                                                return 0;
                                                            } else {
                                                                context.getSource().sendFailure(Component.literal("this command must be called by player."));
                                                                return -1;
                                                            }
                                                        })
                                        )
                        )
        );
    }
}
