package mys.biggerbundle.datagen;

import mys.biggerbundle.registry.BBItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class BBRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public BBRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        generateShapedRecipes(output);
        generateShapelessRecipes(output);
    }

    private void generateShapedRecipes(RecipeOutput output) {
        ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, BBItems.STORAGE_BAG.get(), 1)
                .pattern("www")
                .pattern("wbw")
                .pattern("www")
                .define('w', BBItems.WOOL_BLOCK_ITEM.get())
                .define('b', Items.BUNDLE)
                .unlockedBy("has_bundle", has(Items.BUNDLE))
                .save(output, "shaped_crafting_storage_bag");
    }

    private void generateShapelessRecipes(RecipeOutput output) {
        ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, BBItems.WOOL_BLOCK_ITEM.get(), 1)
                .requires(Ingredient.of(ItemTags.WOOL), 4)
                .requires(Ingredient.of(Items.WHITE_DYE), 1)
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(output, "shapeless_crafting_wool_block");
        ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, Items.WHITE_WOOL, 4)
                .requires(Ingredient.of(BBItems.WOOL_BLOCK_ITEM.get()), 1)
                .unlockedBy("has_wool", has(BBItems.WOOL_BLOCK_ITEM.get()))
                .save(output, "shapeless_crafting_wool");
    }
}