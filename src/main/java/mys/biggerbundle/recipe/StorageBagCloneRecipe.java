package mys.biggerbundle.recipe;

import mys.biggerbundle.item.StorageBagItem;
import mys.biggerbundle.registry.BBDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class StorageBagCloneRecipe extends CustomRecipe {
    private static final Ingredient ENDER_PEARL = Ingredient.of(Items.ENDER_PEARL);

    public StorageBagCloneRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, @NotNull Level level) {
        if (input.width() != 3 || input.height() != 3) return false;

        ItemStack center = input.getItem(4);
        if (!(center.getItem() instanceof StorageBagItem)) return false;
        if (!center.has(BBDataComponents.BAG_ID.get())) return false;

        for (int i = 0; i < 9; i++) {
            if (i == 4) continue;
            if (!ENDER_PEARL.test(input.getItem(i))) return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput input, HolderLookup.@NotNull Provider registries) {
        ItemStack result = input.getItem(4).copy();
        result.setCount(2);
        markLinked(result);
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width == 3 && height == 3;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return BBRecipeSerializers.STORAGE_BAG_CLONE.get();
    }

    private static void markLinked(ItemStack stack) {
        StorageBagItem.markLinked(stack);
        stack.set(DataComponents.RARITY, Rarity.EPIC);
    }
}