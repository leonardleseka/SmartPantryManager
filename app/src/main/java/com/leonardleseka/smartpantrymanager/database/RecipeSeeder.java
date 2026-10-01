package com.leonardleseka.smartpantrymanager.database;
import android.os.Handler;
import android.os.Looper;
import com.leonardleseka.smartpantrymanager.database.entity.Recipe;
import com.leonardleseka.smartpantrymanager.database.entity.RecipeIngredient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public final class RecipeSeeder {
    private RecipeSeeder() {
        // Prevent this utility class from being instantiated.
    }
    public static void seedIfRequired(
            AppDatabase database,
            Runnable completionAction
    ) {
        ExecutorService executor =
                Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                if (database.recipeDao().getCount() == 0) {
                    database.runInTransaction(
                            () -> seedRecipes(database)
                    );
                }
                if (completionAction != null) {
                    new Handler(Looper.getMainLooper()).post(
                            completionAction
                    );
                }
            } finally {
                executor.shutdown();
            }
        });
    }
    private static void seedRecipes(AppDatabase database) {
        addRecipe(
                database,
                "French Toast",
                "Breakfast",
                "Beat the eggs and milk together. Dip the bread into the mixture. Cook in a heated pan until golden on both sides.",
                new IngredientData("Eggs", "egg", 2, "piece"),
                new IngredientData("Bread", "bread", 4, "piece"),
                new IngredientData("Milk", "milk", 250, "ml")
        );
        addRecipe(
                database,
                "Cheese Omelette",
                "Breakfast",
                "Beat the eggs. Cook them in a heated pan, add the cheese, fold the omelette and cook until set.",
                new IngredientData("Eggs", "egg", 2, "piece"),
                new IngredientData("Cheese", "cheese", 50, "g")
        );
        addRecipe(
                database,
                "Scrambled Eggs",
                "Breakfast",
                "Beat the eggs with milk. Cook slowly in a pan while stirring until soft and fully cooked.",
                new IngredientData("Eggs", "egg", 3, "piece"),
                new IngredientData("Milk", "milk", 60, "ml")
        );
        addRecipe(
                database,
                "Grilled Cheese Sandwich",
                "Lunch",
                "Place cheese between the bread slices. Grill in a pan until the bread is golden and the cheese has melted.",
                new IngredientData("Bread", "bread", 2, "piece"),
                new IngredientData("Cheese", "cheese", 60, "g")
        );
        addRecipe(
                database,
                "Tomato and Cheese Sandwich",
                "Lunch",
                "Slice the tomato and cheese. Place them between the bread slices and serve.",
                new IngredientData("Bread", "bread", 2, "piece"),
                new IngredientData("Tomato", "tomato", 1, "piece"),
                new IngredientData("Cheese", "cheese", 50, "g")
        );
        addRecipe(
                database,
                "Tomato Pasta",
                "Dinner",
                "Cook the pasta. Prepare a sauce using tomato and onion, then combine the sauce with the cooked pasta.",
                new IngredientData("Pasta", "pasta", 200, "g"),
                new IngredientData("Tomato", "tomato", 3, "piece"),
                new IngredientData("Onion", "onion", 1, "piece")
        );
        addRecipe(
                database,
                "Creamy Pasta",
                "Dinner",
                "Cook the pasta. Heat the milk, add the cheese, and stir until smooth. Combine with the cooked pasta.",
                new IngredientData("Pasta", "pasta", 200, "g"),
                new IngredientData("Milk", "milk", 250, "ml"),
                new IngredientData("Cheese", "cheese", 100, "g")
        );
        addRecipe(
                database,
                "Mashed Potatoes",
                "Side Dish",
                "Boil the potatoes until soft. Mash them with milk until smooth.",
                new IngredientData("Potatoes", "potato", 500, "g"),
                new IngredientData("Milk", "milk", 100, "ml")
        );
        addRecipe(
                database,
                "Potato and Egg Hash",
                "Breakfast",
                "Cook the diced potatoes and onion in a pan. Add the eggs and cook until set.",
                new IngredientData("Potatoes", "potato", 300, "g"),
                new IngredientData("Eggs", "egg", 2, "piece"),
                new IngredientData("Onion", "onion", 1, "piece")
        );
        addRecipe(
                database,
                "Rice and Beans",
                "Dinner",
                "Cook the rice and beans separately, then combine and heat thoroughly before serving.",
                new IngredientData("Rice", "rice", 200, "g"),
                new IngredientData("Beans", "bean", 200, "g")
        );
        addRecipe(
                database,
                "Vegetable Rice",
                "Dinner",
                "Cook the rice. Cook the carrot, peas and onion until tender, then combine with the rice.",
                new IngredientData("Rice", "rice", 200, "g"),
                new IngredientData("Carrot", "carrot", 1, "piece"),
                new IngredientData("Peas", "pea", 100, "g"),
                new IngredientData("Onion", "onion", 1, "piece")
        );
        addRecipe(
                database,
                "Tuna Sandwich",
                "Lunch",
                "Drain the tuna, place it between the bread slices and serve.",
                new IngredientData("Bread", "bread", 2, "piece"),
                new IngredientData("Tuna", "tuna", 150, "g")
        );
        addRecipe(
                database,
                "Banana Smoothie",
                "Drink",
                "Blend the banana and milk until smooth. Serve immediately.",
                new IngredientData("Banana", "banana", 1, "piece"),
                new IngredientData("Milk", "milk", 250, "ml")
        );
        addRecipe(
                database,
                "Tomato Egg Scramble",
                "Breakfast",
                "Cook the chopped tomato briefly, add the beaten eggs and stir until fully cooked.",
                new IngredientData("Tomato", "tomato", 2, "piece"),
                new IngredientData("Eggs", "egg", 3, "piece")
        );
        addRecipe(
                database,
                "Cheesy Baked Potato",
                "Dinner",
                "Bake or boil the potatoes until tender. Top with cheese and heat until the cheese melts.",
                new IngredientData("Potatoes", "potato", 400, "g"),
                new IngredientData("Cheese", "cheese", 100, "g")
        );
    }
    private static void addRecipe(
            AppDatabase database,
            String name,
            String category,
            String instructions,
            IngredientData... ingredients
    ) {
        Recipe recipe = new Recipe(
                name,
                category,
                instructions
        );
        long recipeId =
                database.recipeDao().insert(recipe);
        for (IngredientData ingredient : ingredients) {
            RecipeIngredient recipeIngredient =
                    new RecipeIngredient(
                            (int) recipeId,
                            ingredient.name,
                            ingredient.normalizedName,
                            ingredient.quantity,
                            ingredient.unit
                    );
            database.recipeIngredientDao().insert(
                    recipeIngredient
            );
        }
    }
    private static class IngredientData {
        private final String name;
        private final String normalizedName;
        private final double quantity;
        private final String unit;
        private IngredientData(
                String name,
                String normalizedName,
                double quantity,
                String unit
        ) {
            this.name = name;
            this.normalizedName = normalizedName;
            this.quantity = quantity;
            this.unit = unit;
        }
    }
}
