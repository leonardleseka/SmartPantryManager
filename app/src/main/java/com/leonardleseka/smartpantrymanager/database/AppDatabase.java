package com.leonardleseka.smartpantrymanager.database;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.leonardleseka.smartpantrymanager.database.dao.PantryDao;
import com.leonardleseka.smartpantrymanager.database.dao.RecipeDao;
import com.leonardleseka.smartpantrymanager.database.dao.RecipeIngredientDao;
import com.leonardleseka.smartpantrymanager.database.entity.PantryItem;
import com.leonardleseka.smartpantrymanager.database.entity.Recipe;
import com.leonardleseka.smartpantrymanager.database.entity.RecipeIngredient;
@Database(
        entities = {
                PantryItem.class,
                Recipe.class,
                RecipeIngredient.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME =
            "smart_pantry_database";
    private static volatile AppDatabase instance;
    public abstract PantryDao pantryDao();
    public abstract RecipeDao recipeDao();
    public abstract RecipeIngredientDao recipeIngredientDao();
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    ).build();
                }
            }
        }
        return instance;
    }
}
