package com.reco.applock.ui.password;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.ob.pinlockviewapp.PinLockListener;
import com.ob.pinlockviewapp.PinLockView;
import com.reco.applock.R;
import com.reco.applock.services.IntruderCaptureService;
import com.reco.applock.ui.security.SecurityQuestionAuthenticationActivity;

public class PINInputActivity extends AppCompatActivity {

    private static final String PIN_PREFS = "AppLockPrefs";
    private static final String PIN_KEY = "PIN_PASSWORD";

    private String enteredPin = "";
    private SharedPreferences preferences;
    private int failedAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_input);

        PinLockView pinLockView = findViewById(R.id.pinLockViewInput);
        Button forgotPasswordButton = findViewById(R.id.forgot_password_button);
        preferences = getSharedPreferences(PIN_PREFS, MODE_PRIVATE);
        String savedPin = preferences.getString(PIN_KEY, "");

        pinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onPinEnter() {
                enteredPin = "";
            }

            @Override
            public void onPinComplete(boolean result) {
                if (enteredPin.equals(savedPin)) {
                    Toast.makeText(PINInputActivity.this, "Access Granted!", Toast.LENGTH_SHORT).show();
                    failedAttempts = 0;
                    preferences.edit().putInt("FAILED_ATTEMPTS", failedAttempts).apply();
                    finish();
                } else {
                    failedAttempts++;
                    preferences.edit().putInt("FAILED_ATTEMPTS", failedAttempts).apply();
                    if (failedAttempts >= 3) {
                        triggerIntruderCapture();
                    }
                    Toast.makeText(PINInputActivity.this, "Incorrect PIN. Try Again.", Toast.LENGTH_SHORT).show();
                    enteredPin = "";
                }
            }

            @Override
            public void onPinDelete() {}

            @Override
            public void onPinEmpty() {}
        });

        forgotPasswordButton.setOnClickListener(v -> navigateToForgotPassword());

        // Handle back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Disable back button for returning user authentication
                Toast.makeText(PINInputActivity.this, "Back button is disabled.", Toast.LENGTH_SHORT).show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void triggerIntruderCapture() {
        IntruderCaptureService intruderCaptureService = new IntruderCaptureService(this);
        intruderCaptureService.captureSelfie();
    }

    private void navigateToForgotPassword() {
        Intent intent = new Intent(this, SecurityQuestionAuthenticationActivity.class);
        intent.putExtra("RESET_TYPE", "PIN");
        startActivity(intent);
    }
}
