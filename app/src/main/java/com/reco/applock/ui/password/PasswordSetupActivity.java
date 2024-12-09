package com.reco.applock.ui.password;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.reco.applock.R;

public class PasswordSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_setup);

        // Bind buttons from the layout
        Button setPatternButton = findViewById(R.id.set_pattern_button);
        Button setPinButton = findViewById(R.id.set_pin_button);

        // Set up click listeners
        setPatternButton.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordSetupActivity.this, PatternActivity.class);
            intent.putExtra("IS_SETUP_MODE", true); // Indicate setup mode for pattern
            startActivity(intent);
        });

        setPinButton.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordSetupActivity.this, PinSetupActivity.class);
            startActivity(intent);
        });

        // Handle back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Disable back button during first-time setup
                // You can show a message or simply do nothing
                // For example, show a toast message
                Toast.makeText(PasswordSetupActivity.this, "Please complete the setup.", Toast.LENGTH_SHORT).show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
