package com.reco.applock.ui.password;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.reco.applock.R;
import com.reco.applock.services.IntruderCaptureService;
import com.reco.applock.ui.security.SecurityQuestionAuthenticationActivity;

import java.util.List;

public class PatternInputActivity extends AppCompatActivity {

    private PatternLockView patternLockView;
    private String lockedPackageName;
    private SharedPreferences preferences;
    private int failedAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_input);

        patternLockView = findViewById(R.id.pattern_lock_view);
        Button forgotPasswordButton = findViewById(R.id.forgot_password_button);
        preferences = getSharedPreferences("AppLockPrefs", MODE_PRIVATE);

        lockedPackageName = getIntent().getStringExtra("LOCKED_PACKAGE_NAME");
        String savedPattern = preferences.getString("SAVED_PATTERN", "");

        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String enteredPattern = PatternLockUtils.patternToString(patternLockView, pattern);
                if (enteredPattern.equals(savedPattern)) {
                    grantAccess();
                    failedAttempts = 0;
                    preferences.edit().putInt("FAILED_ATTEMPTS", failedAttempts).apply();
                } else {
                    failedAttempts++;
                    preferences.edit().putInt("FAILED_ATTEMPTS", failedAttempts).apply();
                    if (failedAttempts >= 3) {
                        triggerIntruderCapture();
                    }
                    Toast.makeText(PatternInputActivity.this, "Incorrect Pattern", Toast.LENGTH_SHORT).show();
                    patternLockView.clearPattern();
                }
            }

            @Override
            public void onCleared() {}
        });

        forgotPasswordButton.setOnClickListener(v -> navigateToForgotPassword());

        // Handle back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Disable back button for returning user authentication
                // You can show a message or simply do nothing
                // For example, show a toast message
                Toast.makeText(PatternInputActivity.this, "Back button is disabled.", Toast.LENGTH_SHORT).show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void grantAccess() {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(lockedPackageName);
        if (launchIntent != null) {
            startActivity(launchIntent);
        }
        finish();
    }

    private void triggerIntruderCapture() {
        IntruderCaptureService intruderCaptureService = new IntruderCaptureService(this);
        intruderCaptureService.captureSelfie();
    }

    private void navigateToForgotPassword() {
        Intent intent = new Intent(this, SecurityQuestionAuthenticationActivity.class);
        intent.putExtra("RESET_TYPE", "PATTERN");
        startActivity(intent);
    }
}
