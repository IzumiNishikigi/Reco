package com.reco.applock.ui.password;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.reco.applock.R;
import com.reco.applock.services.IntruderCaptureService;
import com.reco.applock.ui.security.SecurityQuestionAuthenticationActivity;

import java.util.List;

public class PatternActivity extends AppCompatActivity {

    private PatternLockView patternLockView;
    private SharedPreferences preferences;
    private boolean isSetupMode;
    private int failedAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Determine mode and set layout
        isSetupMode = getIntent().getBooleanExtra("IS_SETUP_MODE", false);
        setContentView(isSetupMode ? R.layout.activity_pattern_setup : R.layout.activity_pattern_input);

        // Initialize views
        patternLockView = findViewById(R.id.pattern_lock_view);
        TextView instructions = findViewById(R.id.instructions_text);
        Button forgotPasswordButton = findViewById(R.id.forgot_password_button);

        preferences = getSharedPreferences("AppLockPrefs", MODE_PRIVATE);

        // Update instructions text
        instructions.setText(isSetupMode ? getString(R.string.set_your_new_pattern) : getString(R.string.draw_your_pattern_to_unlock));

        // Add pattern listener
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String enteredPattern = PatternLockUtils.patternToString(patternLockView, pattern);
                if (isSetupMode) {
                    savePattern(enteredPattern);
                } else {
                    validatePattern(enteredPattern);
                }
            }

            @Override
            public void onCleared() {}
        });

        // Forgot password feature
        if (!isSetupMode) {
            forgotPasswordButton.setOnClickListener(v -> navigateToForgotPassword());
        }

        // Handle back press using OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Custom back press logic
                finish(); // or any other logic you want to execute
            }
        });
    }

    private void savePattern(@NonNull String pattern) {
        preferences.edit().putString("SAVED_PATTERN", pattern).apply();
        Toast.makeText(this, "Pattern Saved Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void validatePattern(@NonNull String pattern) {
        String savedPattern = preferences.getString("SAVED_PATTERN", "");
        if (pattern.equals(savedPattern)) {
            grantAccess();
        } else {
            failedAttempts++;
            preferences.edit().putInt("FAILED_ATTEMPTS", failedAttempts).apply();
            if (failedAttempts >= 3) {
                triggerIntruderCapture();
            }
            Toast.makeText(this, "Incorrect Pattern", Toast.LENGTH_SHORT).show();
            patternLockView.clearPattern();
        }
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

    private void grantAccess() {
        Toast.makeText(this, "Access Granted!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
