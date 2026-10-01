package com.leonardleseka.smartpantrymanager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.leonardleseka.smartpantrymanager.database.AppDatabase;
import com.leonardleseka.smartpantrymanager.database.RecipeSeeder;
public class MainActivity extends AppCompatActivity {
    private Button buttonMyPantry;
    private Button buttonSuggestedRecipes;
    private Button buttonSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seedRecipeCollection();
        buttonMyPantry = findViewById(R.id.buttonMyPantry);
        buttonSuggestedRecipes = findViewById(
                R.id.buttonSuggestedRecipes
        );
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonMyPantry.setOnClickListener(view -> {
            Intent pantryIntent = new Intent(
                    MainActivity.this,
                    PantryListActivity.class
            );
            startActivity(pantryIntent);
        });
        buttonSuggestedRecipes.setOnClickListener(view -> {
            Intent recipesIntent = new Intent(
                    MainActivity.this,
                    SuggestedRecipesActivity.class
            );
            startActivity(recipesIntent);
        });
        buttonSettings.setOnClickListener(view -> {
            Intent settingsIntent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );
            startActivity(settingsIntent);
        });
    }
    private void seedRecipeCollection() {
        AppDatabase database = AppDatabase.getInstance(
                getApplicationContext()
        );
        RecipeSeeder.seedIfRequired(
                database,
                null
        );
    }
}
