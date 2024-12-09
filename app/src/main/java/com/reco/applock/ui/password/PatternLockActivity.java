package com.reco.applock.ui.password;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.reco.applock.R;
import com.reco.applock.ui.security.SecurityQuestionAuthenticationActivity;

import java.util.List;

public class PatternLockActivity extends AppCompatActivity {

    private PatternLockView mPatternLockView;
    private SharedPreferences preferences;
    private boolean isSetupMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if it's setup or input mode
        isSetupMode = getIntent().getBooleanExtra("IS_SETUP_MODE", false);
        setContentView(R.layout.activity_pattern_input); // Use a single layout

        // Initialize views and preferences
        mPatternLockView = findViewById(R.id.pattern_lock_view);
        preferences = getSharedPreferences("AppLockPrefs", MODE_PRIVATE);

        TextView instructions = findViewById(R.id.instructions_text);
        instructions.setText(isSetupMode ? "Set your new pattern" : "Draw your pattern to unlock");

        Button forgotPasswordButton = findViewById(R.id.forgot_password_button);
        if (!isSetupMode) {
            forgotPasswordButton.setVisibility(View.VISIBLE);
            forgotPasswordButton.setOnClickListener(v -> navigateToForgotPassword());
        }

        // Add the pattern listener
        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {
            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {
            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String patternString = PatternLockUtils.patternToString(mPatternLockView, pattern);
                if (isSetupMode) {
                    savePattern(patternString);
                } else {
                    validatePattern(patternString);
                }
            }

            @Override
            public void onCleared() {}
        });

        // Handle back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isSetupMode) {
                    // Disable back button during setup
                    Toast.makeText(PatternLockActivity.this, "Please complete the setup.", Toast.LENGTH_SHORT).show();
                } else {
                    // Allow back navigation in input mode
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void savePattern(@NonNull String pattern) {
        preferences.edit().putString("SAVED_PATTERN", pattern).apply();
        Toast.makeText(this, "Pattern Saved Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void validatePattern(@NonNull String pattern) {
        String savedPattern = preferences.getString("SAVED_PATTERN", "");
        if (savedPattern.equals(pattern)) {
            grantAccess();
        } else {
            Toast.makeText(this, "Incorrect Pattern", Toast.LENGTH_SHORT).show();
            mPatternLockView.clearPattern();
        }
    }

    private void grantAccess() {
        Toast.makeText(this, "Access Granted!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void navigateToForgotPassword() {
        Intent intent = new Intent(this, SecurityQuestionAuthenticationActivity.class);
        intent.putExtra("RESET_TYPE", "PATTERN");
        startActivity(intent);
    }
}
