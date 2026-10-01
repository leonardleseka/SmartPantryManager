package com.leonardleseka.smartpantrymanager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
public class SettingsActivity extends AppCompatActivity {
    private static final String PREFERENCES_NAME =
            "smart_pantry_preferences";
    private static final String KEY_EXPIRY_ALERTS =
            "expiry_alerts_enabled";
    private SwitchCompat switchExpiryAlerts;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getSharedPreferences(
                PREFERENCES_NAME,
                MODE_PRIVATE
        );
        initialiseViews();
        loadSavedSettings();
        configureExpiryAlertsSwitch();
    }
    private void initialiseViews() {
        switchExpiryAlerts = findViewById(
                R.id.switchExpiryAlerts
        );
    }
    private void loadSavedSettings() {
        boolean expiryAlertsEnabled =
                sharedPreferences.getBoolean(
                        KEY_EXPIRY_ALERTS,
                        false
                );
        switchExpiryAlerts.setChecked(
                expiryAlertsEnabled
        );
    }
    private void configureExpiryAlertsSwitch() {
        switchExpiryAlerts.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    saveExpiryAlertsSetting(isChecked);
                    String message = isChecked
                            ? "Expiry alerts enabled"
                            : "Expiry alerts disabled";
                    Toast.makeText(
                            SettingsActivity.this,
                            message,
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );
    }
    private void saveExpiryAlertsSetting(
            boolean isEnabled
    ) {
        sharedPreferences
                .edit()
                .putBoolean(
                        KEY_EXPIRY_ALERTS,
                        isEnabled
                )
                .apply();
    }
}
