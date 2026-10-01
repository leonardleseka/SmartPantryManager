package com.leonardleseka.smartpantrymanager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.leonardleseka.smartpantrymanager.database.AppDatabase;
import com.leonardleseka.smartpantrymanager.database.entity.Recipe;
import com.leonardleseka.smartpantrymanager.database.entity.RecipeIngredient;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class RecipeDetailActivity extends AppCompatActivity {
    private ProgressBar progressBarRecipeDetail;
    private LinearLayout layoutRecipeDetailContent;
    private TextView textViewRecipeDetailName;
    private TextView textViewRecipeDetailCategory;
    private TextView textViewRecipeIngredients;
    private TextView textViewRecipeInstructions;
    private TextView textViewRecipeDetailError;
    private Button buttonBackToRecipes;
    private AppDatabase appDatabase;
    private ExecutorService databaseExecutor;
    private int recipeId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        appDatabase = AppDatabase.getInstance(
                getApplicationContext()
        );
        databaseExecutor = Executors.newSingleThreadExecutor();
        initialiseViews();
        configureBackButton();
        readRecipeId();
    }
    private void initialiseViews() {
        progressBarRecipeDetail = findViewById(
                R.id.progressBarRecipeDetail
        );
        layoutRecipeDetailContent = findViewById(
                R.id.layoutRecipeDetailContent
        );
        textViewRecipeDetailName = findViewById(
                R.id.textViewRecipeDetailName
        );
        textViewRecipeDetailCategory = findViewById(
                R.id.textViewRecipeDetailCategory
        );
        textViewRecipeIngredients = findViewById(
                R.id.textViewRecipeIngredients
        );
        textViewRecipeInstructions = findViewById(
                R.id.textViewRecipeInstructions
        );
        textViewRecipeDetailError = findViewById(
                R.id.textViewRecipeDetailError
        );
        buttonBackToRecipes = findViewById(
                R.id.buttonBackToRecipes
        );
    }
    private void configureBackButton() {
        buttonBackToRecipes.setOnClickListener(
                view -> finish()
        );
    }
    private void readRecipeId() {
        recipeId = getIntent().getIntExtra(
                SuggestedRecipesActivity.EXTRA_RECIPE_ID,
                -1
        );
        if (recipeId == -1) {
            showErrorState();
            return;
        }
        loadRecipeDetails();
    }
    private void loadRecipeDetails() {
        showLoadingState();
        databaseExecutor.execute(() -> {
            Recipe recipe = appDatabase
                    .recipeDao()
                    .getById(recipeId);
            List<RecipeIngredient> recipeIngredients =
                    appDatabase
                            .recipeIngredientDao()
                            .getForRecipe(recipeId);
            runOnUiThread(() -> {
                if (recipe == null
                        || recipeIngredients == null
                        || recipeIngredients.isEmpty()) {
                    showErrorState();
                    return;
                }
                displayRecipeDetails(
                        recipe,
                        recipeIngredients
                );
            });
        });
    }
    private void displayRecipeDetails(
            Recipe recipe,
            List<RecipeIngredient> recipeIngredients
    ) {
        textViewRecipeDetailName.setText(
                recipe.getName()
        );
        textViewRecipeDetailCategory.setText(
                "Category: " + recipe.getCategory()
        );
        textViewRecipeIngredients.setText(
                buildIngredientList(recipeIngredients)
        );
        textViewRecipeInstructions.setText(
                recipe.getInstructions()
        );
        progressBarRecipeDetail.setVisibility(View.GONE);
        textViewRecipeDetailError.setVisibility(View.GONE);
        layoutRecipeDetailContent.setVisibility(View.VISIBLE);
    }
    private String buildIngredientList(
            List<RecipeIngredient> recipeIngredients
    ) {
        StringBuilder ingredientList =
                new StringBuilder();
        for (int index = 0;
             index < recipeIngredients.size();
             index++) {
            RecipeIngredient ingredient =
                    recipeIngredients.get(index);
            ingredientList.append("• ")
                    .append(formatQuantity(
                            ingredient.getRequiredQuantity()
                    ))
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" ")
                    .append(ingredient.getIngredientName());
            if (index < recipeIngredients.size() - 1) {
                ingredientList.append("\n");
            }
        }
        return ingredientList.toString();
    }
    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)) {
            return String.format(
                    Locale.getDefault(),
                    "%.0f",
                    quantity
            );
        }
        return String.format(
                Locale.getDefault(),
                "%.2f",
                quantity
        );
    }
    private void showLoadingState() {
        progressBarRecipeDetail.setVisibility(View.VISIBLE);
        layoutRecipeDetailContent.setVisibility(View.GONE);
        textViewRecipeDetailError.setVisibility(View.GONE);
    }
    private void showErrorState() {
        progressBarRecipeDetail.setVisibility(View.GONE);
        layoutRecipeDetailContent.setVisibility(View.GONE);
        textViewRecipeDetailError.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseExecutor != null) {
            databaseExecutor.shutdown();
        }
    }
}
