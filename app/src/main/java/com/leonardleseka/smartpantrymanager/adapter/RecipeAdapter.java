package com.leonardleseka.smartpantrymanager.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.leonardleseka.smartpantrymanager.R;
import com.leonardleseka.smartpantrymanager.database.entity.Recipe;
import java.util.ArrayList;
import java.util.List;
public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    public interface RecipeItemListener {
        void onViewRecipeClick(Recipe recipe);
    }
    private final List<Recipe> recipes =
            new ArrayList<>();
    private final RecipeItemListener listener;
    public RecipeAdapter(RecipeItemListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_recipe,
                        parent,
                        false
                );
        return new RecipeViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position
    ) {
        Recipe recipe = recipes.get(position);
        holder.textViewRecipeName.setText(
                recipe.getName()
        );
        holder.textViewRecipeCategory.setText(
                "Category: " + recipe.getCategory()
        );
        holder.textViewRecipeMatchStatus.setText(
                "All required ingredients are available"
        );
        holder.buttonViewRecipe.setOnClickListener(
                view -> listener.onViewRecipeClick(recipe)
        );
    }
    @Override
    public int getItemCount() {
        return recipes.size();
    }
    public void setRecipes(List<Recipe> updatedRecipes) {
        recipes.clear();
        if (updatedRecipes != null) {
            recipes.addAll(updatedRecipes);
        }
        notifyDataSetChanged();
    }
    static class RecipeViewHolder
            extends RecyclerView.ViewHolder {
        private final TextView textViewRecipeName;
        private final TextView textViewRecipeCategory;
        private final TextView textViewRecipeMatchStatus;
        private final Button buttonViewRecipe;
        public RecipeViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);
            textViewRecipeName = itemView.findViewById(
                    R.id.textViewRecipeName
            );
            textViewRecipeCategory = itemView.findViewById(
                    R.id.textViewRecipeCategory
            );
            textViewRecipeMatchStatus = itemView.findViewById(
                    R.id.textViewRecipeMatchStatus
            );
            buttonViewRecipe = itemView.findViewById(
                    R.id.buttonViewRecipe
            );
        }
    }
}
