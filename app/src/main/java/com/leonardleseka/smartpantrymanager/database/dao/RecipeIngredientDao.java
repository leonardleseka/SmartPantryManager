package com.leonardleseka.smartpantrymanager.database.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.leonardleseka.smartpantrymanager.database.entity.RecipeIngredient;
import java.util.List;
@Dao
public interface RecipeIngredientDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(RecipeIngredient recipeIngredient);
    @Insert(onConflict = OnConflictStrategy.ABORT)
    List<Long> insertAll(List<RecipeIngredient> recipeIngredients);
    @Query(
            "SELECT * FROM recipe_ingredients " +
                    "WHERE recipeId = :recipeId " +
                    "ORDER BY ingredientName ASC"
    )
    List<RecipeIngredient> getForRecipe(int recipeId);
    @Query(
            "SELECT * FROM recipe_ingredients " +
                    "ORDER BY recipeId ASC, ingredientName ASC"
    )
    List<RecipeIngredient> getAll();
    @Query(
            "DELETE FROM recipe_ingredients " +
                    "WHERE recipeId = :recipeId"
    )
    void deleteForRecipe(int recipeId);
    @Query("DELETE FROM recipe_ingredients")
    void deleteAll();
}

