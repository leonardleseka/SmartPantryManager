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
import java.util.Calendar;
import java.util.Locale;
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
        initialiseViews();
        configureUnitSpinner();
        configureExpiryDatePicker();
        configureButtons();
    }
    private void initialiseViews() {
        layoutIngredientName = findViewById(R.id.layoutIngredientName);
        layoutQuantity = findViewById(R.id.layoutQuantity);
        layoutExpiryDate = findViewById(R.id.layoutExpiryDate);
        editTextIngredientName = findViewById(R.id.editTextIngredientName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        buttonSaveIngredient = findViewById(R.id.buttonSaveIngredient);
        buttonCancelIngredient = findViewById(R.id.buttonCancelIngredient);
    }
    private void configureUnitSpinner() {
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
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
        editTextExpiryDate.setOnClickListener(view -> showDatePicker());
    }
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format(
                            Locale.getDefault(),
                            "%04d-%02d-%02d",
                            selectedYear,
                            selectedMonth + 1,
                            selectedDay
                    );
                    editTextExpiryDate.setText(selectedDate);
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
        buttonSaveIngredient.setOnClickListener(view -> validateForm());
        buttonCancelIngredient.setOnClickListener(view -> finish());
    }
    private void validateForm() {
        clearValidationErrors();
        String ingredientName = getText(editTextIngredientName);
        String quantityText = getText(editTextQuantity);
        boolean isValid = true;
        if (ingredientName.isEmpty()) {
            layoutIngredientName.setError("Ingredient name is required");
            isValid = false;
        }
        if (quantityText.isEmpty()) {
            layoutQuantity.setError("Quantity is required");
            isValid = false;
        } else {
            try {
                double quantity = Double.parseDouble(quantityText);
                if (quantity <= 0) {
                    layoutQuantity.setError(
                            "Quantity must be greater than zero"
                    );
                    isValid = false;
                }
            } catch (NumberFormatException exception) {
                layoutQuantity.setError("Enter a valid quantity");
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
        Toast.makeText(
                this,
                "Ingredient details are valid",
                Toast.LENGTH_SHORT
        ).show();
    }
    private void clearValidationErrors() {
        layoutIngredientName.setError(null);
        layoutQuantity.setError(null);
        layoutExpiryDate.setError(null);
    }
    private String getText(TextInputEditText editText) {
        if (editText.getText() == null) {
            return "";
        }
        return editText.getText().toString().trim();
    }
}
