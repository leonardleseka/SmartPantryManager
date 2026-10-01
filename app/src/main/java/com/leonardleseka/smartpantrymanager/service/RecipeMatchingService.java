package com.leonardleseka.smartpantrymanager.service;
import com.leonardleseka.smartpantrymanager.database.AppDatabase;
import com.leonardleseka.smartpantrymanager.database.entity.PantryItem;
import com.leonardleseka.smartpantrymanager.database.entity.Recipe;
import com.leonardleseka.smartpantrymanager.database.entity.RecipeIngredient;
import java.util.ArrayList;
import java.util.List;
public final class RecipeMatchingService {
    private RecipeMatchingService() {
        // Prevent this utility class from being instantiated.
    }
    public static List<Recipe> findMatchingRecipes(
            AppDatabase database
    ) {
        List<PantryItem> pantryItems =
                database.pantryDao().getAll();
        List<Recipe> recipes =
                database.recipeDao().getAll();
        List<Recipe> matchingRecipes =
                new ArrayList<>();
        for (Recipe recipe : recipes) {
            List<RecipeIngredient> requiredIngredients =
                    database.recipeIngredientDao()
                            .getForRecipe(recipe.getId());
            if (recipeMatchesPantry(
                    pantryItems,
                    requiredIngredients
            )) {
                matchingRecipes.add(recipe);
            }
        }
        return matchingRecipes;
    }
    private static boolean recipeMatchesPantry(
            List<PantryItem> pantryItems,
            List<RecipeIngredient> requiredIngredients
    ) {
        if (requiredIngredients == null
                || requiredIngredients.isEmpty()) {
            return false;
        }
        for (RecipeIngredient requiredIngredient
                : requiredIngredients) {
            if (!pantryContainsIngredient(
                    pantryItems,
                    requiredIngredient
            )) {
                return false;
            }
        }
        return true;
    }
    private static boolean pantryContainsIngredient(
            List<PantryItem> pantryItems,
            RecipeIngredient requiredIngredient
    ) {
        String requiredName =
                IngredientNormaliser.normalize(
                        requiredIngredient.getNormalizedName()
                );
        for (PantryItem pantryItem : pantryItems) {
            String pantryName =
                    IngredientNormaliser.normalize(
                            pantryItem.getNormalizedName()
                    );
            boolean namesMatch =
                    pantryName.equals(requiredName);
            if (!namesMatch) {
                continue;
            }
            boolean quantityIsSufficient =
                    UnitConverter.hasSufficientQuantity(
                            pantryItem.getQuantity(),
                            pantryItem.getUnit(),
                            requiredIngredient.getRequiredQuantity(),
                            requiredIngredient.getUnit()
                    );
            if (quantityIsSufficient) {
                return true;
            }
        }
        return false;
    }
}

