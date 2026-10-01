package com.leonardleseka.smartpantrymanager;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.leonardleseka.smartpantrymanager.database.AppDatabase;
import com.leonardleseka.smartpantrymanager.database.entity.PantryItem;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class IngredientFormActivity extends AppCompatActivity {
    private TextInputLayout layoutIngredientName;
    private TextInputLayout layoutQuantity;
    private TextInputLayout layoutExpiryDate;
    private TextInputEditText editTextIngredientName;
    private TextInputEditText editTextQuantity;
    private TextInputEditText editTextExpiryDate;
    private Spinner spinnerUnit;
    private Button buttonSaveIngredient;
    private Button buttonCancelIngredient;
    private AppDatabase appDatabase;
    private ExecutorService databaseExecutor;
    private final String[] units = {
            "Select unit",
            "g",
            "kg",
            "ml",
            "l",
            "piece",
            "tsp",
            "tbsp",
            "cup"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_form);
        appDatabase = AppDatabase.getInstance(
                getApplicationContext()
        );
        databaseExecutor = Executors.newSingleThreadExecutor();
        initialiseViews();
        configureUnitSpinner();
        configureExpiryDatePicker();
        configureButtons();
    }
    private void initialiseViews() {
        layoutIngredientName = findViewById(
                R.id.layoutIngredientName
        );
        layoutQuantity = findViewById(
                R.id.layoutQuantity
        );
        layoutExpiryDate = findViewById(
                R.id.layoutExpiryDate
        );
        editTextIngredientName = findViewById(
                R.id.editTextIngredientName
        );
        editTextQuantity = findViewById(
                R.id.editTextQuantity
        );
        editTextExpiryDate = findViewById(
                R.id.editTextExpiryDate
        );
        spinnerUnit = findViewById(
                R.id.spinnerUnit
        );
        buttonSaveIngredient = findViewById(
                R.id.buttonSaveIngredient
        );
        buttonCancelIngredient = findViewById(
                R.id.buttonCancelIngredient
        );
    }
    private void configureUnitSpinner() {
        ArrayAdapter<String> unitAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        units
                );
        unitAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerUnit.setAdapter(unitAdapter);
    }
    private void configureExpiryDatePicker() {
        editTextExpiryDate.setOnClickListener(
                view -> showDatePicker()
        );
    }
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (datePicker,
                         selectedYear,
                         selectedMonth,
                         selectedDay) -> {
                            String selectedDate =
                                    String.format(
                                            Locale.getDefault(),
                                            "%04d-%02d-%02d",
                                            selectedYear,
                                            selectedMonth + 1,
                                            selectedDay
                                    );
                            editTextExpiryDate.setText(
                                    selectedDate
                            );
                            layoutExpiryDate.setError(null);
                        },
                        year,
                        month,
                        day
                );
        datePickerDialog.getDatePicker().setMinDate(
                System.currentTimeMillis() - 1000
        );
        datePickerDialog.show();
    }
    private void configureButtons() {
        buttonSaveIngredient.setOnClickListener(
                view -> validateAndSaveIngredient()
        );
        buttonCancelIngredient.setOnClickListener(
                view -> finish()
        );
    }
    private void validateAndSaveIngredient() {
        clearValidationErrors();
        String ingredientName =
                getText(editTextIngredientName);
        String quantityText =
                getText(editTextQuantity);
        String expiryDate =
                getText(editTextExpiryDate);
        boolean isValid = true;
        if (ingredientName.isEmpty()) {
            layoutIngredientName.setError(
                    "Ingredient name is required"
            );
            isValid = false;
        }
        double quantity = 0;
        if (quantityText.isEmpty()) {
            layoutQuantity.setError(
                    "Quantity is required"
            );
            isValid = false;
        } else {
            try {
                quantity = Double.parseDouble(
                        quantityText
                );
                if (quantity <= 0) {
                    layoutQuantity.setError(
                            "Quantity must be greater than zero"
                    );
                    isValid = false;
                }
            } catch (NumberFormatException exception) {
                layoutQuantity.setError(
                        "Enter a valid quantity"
                );
                isValid = false;
            }
        }
        if (spinnerUnit.getSelectedItemPosition() == 0) {
            Toast.makeText(
                    this,
                    "Please select a unit",
                    Toast.LENGTH_SHORT
            ).show();
            isValid = false;
        }
        if (!isValid) {
            return;
        }
        String selectedUnit =
                spinnerUnit.getSelectedItem().toString();
        String normalizedName =
                normalizeIngredientName(ingredientName);
        PantryItem pantryItem = new PantryItem(
                ingredientName,
                normalizedName,
                quantity,
                selectedUnit,
                expiryDate,
                System.currentTimeMillis()
        );
        saveIngredient(pantryItem);
    }
    private void saveIngredient(PantryItem pantryItem) {
        setSaveButtonEnabled(false);
        databaseExecutor.execute(() -> {
            try {
                appDatabase.pantryDao().insert(pantryItem);
                runOnUiThread(() -> {
                    Toast.makeText(
                            IngredientFormActivity.this,
                            "Ingredient saved successfully",
                            Toast.LENGTH_SHORT
                    ).show();
                    setResult(RESULT_OK);
                    finish();
                });
            } catch (Exception exception) {
                runOnUiThread(() -> {
                    setSaveButtonEnabled(true);
                    Toast.makeText(
                            IngredientFormActivity.this,
                            "This ingredient and unit already exist",
                            Toast.LENGTH_LONG
                    ).show();
                });
            }
        });
    }
    private String normalizeIngredientName(String name) {
        String normalizedName =
                name.trim()
                        .toLowerCase(Locale.ROOT)
                        .replaceAll("[^a-z0-9 ]", "")
                        .replaceAll("\\s+", " ");
        if (normalizedName.endsWith("ies")
                && normalizedName.length() > 3) {
            normalizedName =
                    normalizedName.substring(
                            0,
                            normalizedName.length() - 3
                    ) + "y";
        } else if (normalizedName.endsWith("oes")
                && normalizedName.length() > 3) {
            normalizedName =
                    normalizedName.substring(
                            0,
                            normalizedName.length() - 2
                    );
        } else if (normalizedName.endsWith("s")
                && !normalizedName.endsWith("ss")
                && normalizedName.length() > 1) {
            normalizedName =
                    normalizedName.substring(
                            0,
                            normalizedName.length() - 1
                    );
        }
        return normalizedName;
    }
    private void setSaveButtonEnabled(boolean enabled) {
        buttonSaveIngredient.setEnabled(enabled);
        if (enabled) {
            buttonSaveIngredient.setText(
                    "Save Ingredient"
            );
        } else {
            buttonSaveIngredient.setText(
                    "Saving..."
            );
        }
    }
    private void clearValidationErrors() {
        layoutIngredientName.setError(null);
        layoutQuantity.setError(null);
        layoutExpiryDate.setError(null);
    }
    private String getText(
            TextInputEditText editText
    ) {
        if (editText.getText() == null) {
            return "";
        }
        return editText.getText()
                .toString()
                .trim();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseExecutor != null) {
            databaseExecutor.shutdown();
        }
    }
}
