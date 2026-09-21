package mys.biggerbundle.recipe;

import mys.biggerbundle.Biggerbundle;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BBRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Biggerbundle.MODID);

    public static final Supplier<RecipeSerializer<StorageBagCloneRecipe>> STORAGE_BAG_CLONE =
            RECIPE_SERIALIZERS.register(
                    "storage_bag_clone",
                    () -> new net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<>(StorageBagCloneRecipe::new)
            );
}