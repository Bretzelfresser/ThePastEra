package com.bretzelfresser.the_past_era.core.datagen.server;

import com.bretzelfresser.the_past_era.ThePastEra;
import com.bretzelfresser.the_past_era.core.init.ItemInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator p_125973_) {
        super(p_125973_);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ItemInit.RAW_GOOBAR.get()), ItemInit.COOKED_GOOBAR.get(), 0.5f, 200).unlockedBy("hasItem", has(ItemInit.RAW_GOOBAR.get())).save(consumer, ThePastEra.modLoc("cooked_goobar_smelting").toString());
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(ItemInit.RAW_GOOBAR.get()), ItemInit.COOKED_GOOBAR.get(), 0.5f, 100).unlockedBy("hasItem", has(ItemInit.RAW_GOOBAR.get())).save(consumer, ThePastEra.modLoc("cooked_goobar_smoking").toString());
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(ItemInit.RAW_GOOBAR.get()), ItemInit.COOKED_GOOBAR.get(), 0.5f, 100).unlockedBy("hasItem", has(ItemInit.RAW_GOOBAR.get())).save(consumer, ThePastEra.modLoc("cooked_goobar_campfire").toString());
    }
}
