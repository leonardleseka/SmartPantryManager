package com.leonardleseka.smartpantrymanager.database.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.leonardleseka.smartpantrymanager.database.entity.Recipe;
import java.util.List;
@Dao
public interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(Recipe recipe);
    @Insert(onConflict = OnConflictStrategy.ABORT)
    List<Long> insertAll(List<Recipe> recipes);
    @Query("SELECT * FROM recipes ORDER BY name ASC")
    List<Recipe> getAll();
    @Query("SELECT * FROM recipes WHERE id = :recipeId LIMIT 1")
    Recipe getById(int recipeId);
    @Query("SELECT COUNT(*) FROM recipes")
    int getCount();
    @Query("DELETE FROM recipes")
    void deleteAll();
}

