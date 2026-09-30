package com.leonardleseka.smartpantrymanager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class PantryListActivity extends AppCompatActivity {
    private Button buttonAddIngredient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);
        buttonAddIngredient = findViewById(R.id.buttonAddIngredient);
        buttonAddIngredient.setOnClickListener(view -> {
            Intent ingredientFormIntent =
                    new Intent(PantryListActivity.this, IngredientFormActivity.class);
            startActivity(ingredientFormIntent);
        });
    }
}
