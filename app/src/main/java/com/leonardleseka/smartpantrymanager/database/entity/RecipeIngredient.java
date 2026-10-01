package com.leonardleseka.smartpantrymanager.database.entity;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
@Entity(
        tableName = "recipe_ingredients",
        foreignKeys = {
                @ForeignKey(
                        entity = Recipe.class,
                        parentColumns = "id",
                        childColumns = "recipeId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = "recipeId"),
                @Index(
                        value = {"recipeId", "normalizedName"},
                        unique = true
                )
        }
)
public class RecipeIngredient {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int recipeId;
    private String ingredientName;
    private String normalizedName;
    private double requiredQuantity;
    private String unit;
    public RecipeIngredient(
            int recipeId,
            String ingredientName,
            String normalizedName,
            double requiredQuantity,
            String unit
    ) {
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.normalizedName = normalizedName;
        this.requiredQuantity = requiredQuantity;
        this.unit = unit;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
    public String getIngredientName() {
        return ingredientName;
    }
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
    public String getNormalizedName() {
        return normalizedName;
    }
    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }
    public double getRequiredQuantity() {
        return requiredQuantity;
    }
    public void setRequiredQuantity(double requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
