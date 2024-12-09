package com.reco.applock.ui.password;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;

import com.ob.pinlockviewapp.PinLockListener;
import com.ob.pinlockviewapp.PinLockView;
import com.reco.applock.R;

public class PinSetupActivity extends AppCompatActivity {

    private static final String PIN_PREFS = "AppLockPrefs";
    private static final String PIN_KEY = "PIN_PASSWORD";

    private String newPin = ""; // Tracks the new PIN during setup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_setup);

        PinLockView pinLockView = findViewById(R.id.pinLockViewSetup);
        pinLockView.setPinLockListener(new PinLockListener() {

            @Override
            public void onPinEnter() {
                // Reset entered PIN if needed
                newPin = "";
            }

            @Override
            public void onPinComplete(boolean result) {
                Toast.makeText(PinSetupActivity.this, "PIN Entered!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPinDelete() {
                if (!newPin.isEmpty()) {
                    newPin = newPin.substring(0, newPin.length() - 1); // Remove the last digit
                    Toast.makeText(PinSetupActivity.this, "PIN Digit Deleted!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPinEmpty() {
                newPin = ""; // Reset PIN
                Toast.makeText(PinSetupActivity.this, "PIN Cleared!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.save_pin_button).setOnClickListener(v -> {
            if (newPin != null && newPin.length() == 4) {
                savePin(newPin);
                Toast.makeText(this, "PIN Saved Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Please enter a valid 4-digit PIN.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle back press using OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Custom back press logic
                finish(); // or any other logic you want to execute
            }
        });
    }

    private void savePin(String pin) {
        SharedPreferences sharedPreferences = getSharedPreferences(PIN_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PIN_KEY, pin);
        editor.apply();
    }
}
