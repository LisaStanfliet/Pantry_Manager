
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantry_manager.R;

public class SettingsActivity extends AppCompatActivity{
    private static final String PREFS_NAME = "settings_prefs";
    private static final String KEY_EXPIRY_NOTIFICATION = "expiry_notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch expirySwitch = findViewById(R.id.expirySwitch);
        Button back = findViewById(R.id.backSettings);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        //Restore previous saved preferences
        expirySwitch.setChecked(prefs.getBoolean(KEY_EXPIRY_NOTIFICATION, true));

        //Saves the preferences when switch is toggled
        expirySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_EXPIRY_NOTIFICATION, isChecked).apply();
        });

        //Return to previous screen
        back.setOnClickListener(v -> finish());
    }
}