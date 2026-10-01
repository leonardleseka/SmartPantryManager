package com.leonardleseka.smartpantrymanager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.leonardleseka.smartpantrymanager.adapter.RecipeAdapter;
import com.leonardleseka.smartpantrymanager.database.AppDatabase;
import com.leonardleseka.smartpantrymanager.database.entity.Recipe;
import com.leonardleseka.smartpantrymanager.service.RecipeMatchingService;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class SuggestedRecipesActivity extends AppCompatActivity
        implements RecipeAdapter.RecipeItemListener {
    public static final String EXTRA_RECIPE_ID =
            "recipe_id";
    private RecyclerView recyclerViewRecipes;
    private TextView textViewNoRecipes;
    private ProgressBar progressBarRecipes;
    private RecipeAdapter recipeAdapter;
    private AppDatabase appDatabase;
    private ExecutorService databaseExecutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        appDatabase = AppDatabase.getInstance(
                getApplicationContext()
        );
        databaseExecutor = Executors.newSingleThreadExecutor();
        initialiseViews();
        configureRecyclerView();
    }
    private void initialiseViews() {
        recyclerViewRecipes = findViewById(
                R.id.recyclerViewRecipes
        );
        textViewNoRecipes = findViewById(
                R.id.textViewNoRecipes
        );
        progressBarRecipes = findViewById(
                R.id.progressBarRecipes
        );
    }
    private void configureRecyclerView() {
        recipeAdapter = new RecipeAdapter(this);
        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );
        recyclerViewRecipes.setAdapter(recipeAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadMatchingRecipes();
    }
    private void loadMatchingRecipes() {
        showLoadingState();
        databaseExecutor.execute(() -> {
            try {
                List<Recipe> matchingRecipes =
                        RecipeMatchingService.findMatchingRecipes(
                                appDatabase
                        );
                runOnUiThread(() -> {
                    recipeAdapter.setRecipes(matchingRecipes);
                    showRecipeResults(matchingRecipes);
                });
            } catch (Exception exception) {
                runOnUiThread(() -> {
                    progressBarRecipes.setVisibility(View.GONE);
                    recyclerViewRecipes.setVisibility(View.GONE);
                    textViewNoRecipes.setVisibility(View.VISIBLE);
                    Toast.makeText(
                            SuggestedRecipesActivity.this,
                            "Unable to load suggested recipes",
                            Toast.LENGTH_LONG
                    ).show();
                });
            }
        });
    }
    private void showLoadingState() {
        progressBarRecipes.setVisibility(View.VISIBLE);
        recyclerViewRecipes.setVisibility(View.GONE);
        textViewNoRecipes.setVisibility(View.GONE);
    }
    private void showRecipeResults(
            List<Recipe> matchingRecipes
    ) {
        progressBarRecipes.setVisibility(View.GONE);
        boolean hasMatchingRecipes =
                matchingRecipes != null
                        && !matchingRecipes.isEmpty();
        if (hasMatchingRecipes) {
            recyclerViewRecipes.setVisibility(View.VISIBLE);
            textViewNoRecipes.setVisibility(View.GONE);
        } else {
            recyclerViewRecipes.setVisibility(View.GONE);
            textViewNoRecipes.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onViewRecipeClick(Recipe recipe) {
        Intent recipeDetailIntent = new Intent(
                SuggestedRecipesActivity.this,
                RecipeDetailActivity.class
        );
        recipeDetailIntent.putExtra(
                EXTRA_RECIPE_ID,
                recipe.getId()
        );
        startActivity(recipeDetailIntent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseExecutor != null) {
            databaseExecutor.shutdown();
        }
    }
}
