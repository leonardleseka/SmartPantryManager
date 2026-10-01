package com.leonardleseka.smartpantrymanager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.leonardleseka.smartpantrymanager.adapter.PantryAdapter;
import com.leonardleseka.smartpantrymanager.database.AppDatabase;
import com.leonardleseka.smartpantrymanager.database.entity.PantryItem;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class PantryListActivity extends AppCompatActivity
        implements PantryAdapter.PantryItemListener {
    public static final String EXTRA_PANTRY_ITEM_ID =
            "pantry_item_id";
    private RecyclerView recyclerViewPantry;
    private TextView textViewEmptyPantry;
    private Button buttonAddIngredient;
    private PantryAdapter pantryAdapter;
    private AppDatabase appDatabase;
    private ExecutorService databaseExecutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);
        appDatabase = AppDatabase.getInstance(
                getApplicationContext()
        );
        databaseExecutor = Executors.newSingleThreadExecutor();
        initialiseViews();
        configureRecyclerView();
        configureAddButton();
    }
    private void initialiseViews() {
        recyclerViewPantry = findViewById(
                R.id.recyclerViewPantry
        );
        textViewEmptyPantry = findViewById(
                R.id.textViewEmptyPantry
        );
        buttonAddIngredient = findViewById(
                R.id.buttonAddIngredient
        );
    }
    private void configureRecyclerView() {
        pantryAdapter = new PantryAdapter(this);
        recyclerViewPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );
        recyclerViewPantry.setAdapter(pantryAdapter);
    }
    private void configureAddButton() {
        buttonAddIngredient.setOnClickListener(view -> {
            Intent ingredientFormIntent = new Intent(
                    PantryListActivity.this,
                    IngredientFormActivity.class
            );
            startActivity(ingredientFormIntent);
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems();
    }
    private void loadPantryItems() {
        databaseExecutor.execute(() -> {
            List<PantryItem> pantryItems =
                    appDatabase.pantryDao().getAll();
            runOnUiThread(() -> {
                pantryAdapter.setPantryItems(pantryItems);
                updateEmptyState(pantryItems.isEmpty());
            });
        });
    }
    private void updateEmptyState(boolean isEmpty) {
        if (isEmpty) {
            textViewEmptyPantry.setVisibility(View.VISIBLE);
            recyclerViewPantry.setVisibility(View.GONE);
        } else {
            textViewEmptyPantry.setVisibility(View.GONE);
            recyclerViewPantry.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onEditClick(PantryItem pantryItem) {
        Intent editIngredientIntent = new Intent(
                PantryListActivity.this,
                IngredientFormActivity.class
        );
        editIngredientIntent.putExtra(
                EXTRA_PANTRY_ITEM_ID,
                pantryItem.getId()
        );
        startActivity(editIngredientIntent);
    }
    @Override
    public void onDeleteClick(PantryItem pantryItem) {
        showDeleteConfirmation(pantryItem);
    }
    private void showDeleteConfirmation(PantryItem pantryItem) {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage(
                        "Are you sure you want to delete "
                                + pantryItem.getName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) ->
                                deletePantryItem(pantryItem)
                )
                .setNegativeButton(
                        "Cancel",
                        (dialog, which) -> dialog.dismiss()
                )
                .show();
    }
    private void deletePantryItem(PantryItem pantryItem) {
        databaseExecutor.execute(() -> {
            int deletedRows =
                    appDatabase.pantryDao().delete(pantryItem);
            runOnUiThread(() -> {
                if (deletedRows > 0) {
                    Toast.makeText(
                            PantryListActivity.this,
                            "Ingredient deleted",
                            Toast.LENGTH_SHORT
                    ).show();
                    loadPantryItems();
                } else {
                    Toast.makeText(
                            PantryListActivity.this,
                            "Unable to delete ingredient",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseExecutor != null) {
            databaseExecutor.shutdown();
        }
    }
}
